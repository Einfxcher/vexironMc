package eu.vexiron.island;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.anvil.AnvilLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.UUID;

/**
 * Handles chunk persistence on the filesystem.
 * Each island's world data lives in data/islands/{uuid}/
 */
public final class IslandStorage {

    private static final Logger LOGGER = LoggerFactory.getLogger(IslandStorage.class);
    private static final Path ROOT = Path.of("data", "islands");

    private IslandStorage() {}

    public static InstanceContainer createOrLoad(UUID owner) {
        Path folder = ROOT.resolve(owner.toString());
        try {
            Files.createDirectories(folder);
        } catch (IOException e) {
            LOGGER.error("Failed to create island folder for {}", owner, e);
        }
        @SuppressWarnings("removal")
        InstanceContainer instance = MinecraftServer.getInstanceManager()
                .createInstanceContainer(new AnvilLoader(folder));
        instance.setChunkSupplier(LightingChunk::new);
        return instance;
    }

    public static void save(InstanceContainer instance, UUID owner) {
        try {
            instance.saveChunksToStorage().join();
            LOGGER.debug("Saved chunks for island {}", owner);
        } catch (Exception e) {
            LOGGER.error("Failed to save chunks for island {}", owner, e);
        }
    }

    public static void delete(UUID owner) {
        Path folder = ROOT.resolve(owner.toString());
        if (!Files.exists(folder)) return;

        try (var paths = Files.walk(folder)) {
            paths.sorted(Comparator.reverseOrder())
                    .forEach(p -> {
                        try { Files.delete(p); }
                        catch (IOException e) { LOGGER.error("Failed to delete {}", p, e); }
                    });
            LOGGER.info("Deleted island folder for {}", owner);
        } catch (IOException e) {
            LOGGER.error("Failed to walk island folder for {}", owner, e);
        }
    }
}