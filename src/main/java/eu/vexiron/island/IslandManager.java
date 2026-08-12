package eu.vexiron.island;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.timer.TaskSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public final class IslandManager {

    public static final Pos SPAWN = new Pos(0.5, 65, 0.5, 0, 0);

    private static final Logger LOGGER = LoggerFactory.getLogger(IslandManager.class);
    private static final long UNLOAD_AFTER_MS = 5 * 60 * 1000; // 5 minutes

    private final IslandRepository repository;
    private final Map<UUID, Island> islands = new ConcurrentHashMap<>();
    private final ExecutorService dbExecutor = Executors.newFixedThreadPool(2, r -> {
        Thread t = new Thread(r, "IslandManager-DB");
        t.setDaemon(true);
        return t;
    });

    public IslandManager(IslandRepository repository) {
        this.repository = repository;
        scheduleAutoUnload();
    }

    // ─── Public API ──────────────────────────────────────

    /**
     * Checks if a player has an island (in DB or memory).
     * Non-blocking.
     */
    public CompletableFuture<Boolean> hasIsland(UUID owner) {
        if (islands.containsKey(owner)) return CompletableFuture.completedFuture(true);
        return CompletableFuture.supplyAsync(() -> repository.exists(owner), dbExecutor);
    }

    /**
     * Gets an island by owner, loading it if needed.
     * Returns empty if the player has no island yet.
     */
    public CompletableFuture<Optional<Island>> getIsland(UUID owner) {
        Island cached = islands.get(owner);
        if (cached != null && cached.isLoaded()) {
            return CompletableFuture.completedFuture(Optional.of(cached));
        }

        return CompletableFuture.supplyAsync(() -> {
            Optional<Island> loaded = repository.load(owner);
            loaded.ifPresent(this::loadInstance);
            return loaded;
        }, dbExecutor);
    }

    /**
     * Creates a new island for a player. Called when they pick a type in the GUI.
     */
    public CompletableFuture<Island> createIsland(UUID owner, IslandType type) {
        long now = System.currentTimeMillis();
        Island island = new Island(owner, type, now, now);

        return CompletableFuture.supplyAsync(() -> {
            repository.save(island);
            loadInstance(island);
            IslandGenerator.generate(island.instance(), type);
            LOGGER.info("Created new {} island for {}", type, owner);
            return island;
        }, dbExecutor);
    }

    /**
     * Teleports a player to an island, loading it if needed.
     */
    public CompletableFuture<Void> teleportTo(Player player, Island island) {
        if (island.isLoaded()) {
            return doTeleport(player, island);
        }

        return CompletableFuture.runAsync(() -> loadInstance(island), dbExecutor)
                .thenCompose(v -> doTeleport(player, island));
    }

    private CompletableFuture<Void> doTeleport(Player player, Island island) {
        island.touch();
        return player.setInstance(island.instance(), SPAWN);
    }

    // ─── Saving ──────────────────────────────────────────

    public CompletableFuture<Void> save(UUID owner) {
        Island island = islands.get(owner);
        if (island == null) return CompletableFuture.completedFuture(null);

        return CompletableFuture.runAsync(() -> {
            repository.save(island);
            if (island.isLoaded()) {
                IslandStorage.save(island.instance(), owner);
            }
        }, dbExecutor);
    }

    public CompletableFuture<Void> saveAll() {
        LOGGER.info("Saving {} loaded islands...", islands.size());
        CompletableFuture<?>[] futures = islands.keySet().stream()
                .map(this::save)
                .toArray(CompletableFuture[]::new);

        return CompletableFuture.allOf(futures)
                .thenRun(() -> LOGGER.info("All islands saved"));
    }

    // ─── Deletion ────────────────────────────────────────

    public CompletableFuture<Void> deleteIsland(UUID owner) {
        return CompletableFuture.runAsync(() -> {
            Island island = islands.remove(owner);
            if (island != null && island.isLoaded()) {
                MinecraftServer.getInstanceManager().unregisterInstance(island.instance());
            }
            repository.delete(owner);
            IslandStorage.delete(owner);
            LOGGER.info("Deleted island for {}", owner);
        }, dbExecutor);
    }

    // ─── Loading / Unloading ─────────────────────────────

    private void loadInstance(Island island) {
        if (island.isLoaded()) return;
        InstanceContainer instance = IslandStorage.createOrLoad(island.owner());
        island.setInstance(instance);
        islands.put(island.owner(), island);
    }

    private void unloadIfIdle(Island island) {
        if (!island.isLoaded()) return;

        // Don't unload if anyone is still inside
        if (!island.instance().getPlayers().isEmpty()) return;

        long idleTime = System.currentTimeMillis() - island.lastVisited();
        if (idleTime < UNLOAD_AFTER_MS) return;

        save(island.owner()).thenRun(() -> {
            MinecraftServer.getInstanceManager().unregisterInstance(island.instance());
            island.setInstance(null);
            LOGGER.info("Unloaded idle island for {}", island.owner());
        });
    }

    private void scheduleAutoUnload() {
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            for (Island island : islands.values()) {
                unloadIfIdle(island);
            }
        }).repeat(TaskSchedule.minutes(1)).schedule();
    }

    // ─── Info ────────────────────────────────────────────

    public int loadedCount() {
        return (int) islands.values().stream().filter(Island::isLoaded).count();
    }

    public void shutdown() {
        saveAll().join();
        dbExecutor.shutdown();
    }
}