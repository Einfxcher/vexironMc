package eu.vexiron.rank;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class RankManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(RankManager.class);

    private final Map<UUID, Rank> ranks = new ConcurrentHashMap<>();
    private final RankStorage storage;
    private final RankTeams teams;

    public RankManager(RankStorage storage) {
        this.storage = storage;
        this.teams = new RankTeams();
        this.ranks.putAll(storage.loadAll());
        LOGGER.info("Loaded {} rank entries", ranks.size());
    }

    public Rank getRank(UUID uuid) {
        return ranks.getOrDefault(uuid, Rank.PLAYER);
    }

    public Rank getRank(Player player) {
        return getRank(player.getUuid());
    }

    public void setRank(UUID uuid, Rank rank) {
        if (rank == Rank.PLAYER) {
            ranks.remove(uuid);
            storage.delete(uuid);
        } else {
            ranks.put(uuid, rank);
            storage.save(uuid, rank);
        }
    }

    public void setRank(Player player, Rank rank) {
        Rank oldRank = getRank(player);
        setRank(player.getUuid(), rank);
        teams.move(player.getUsername(), oldRank, rank);
        player.refreshCommands();
    }

    public void onJoin(Player player) {
        Rank rank = getRank(player);
        MinecraftServer.getSchedulerManager().scheduleNextTick(() -> {
            teams.sync(player);
            teams.ensure(player.getUsername(), rank);
            player.refreshCommands();
        });
    }

    public boolean hasRank(CommandSender sender, Rank required) {
        return !(sender instanceof Player player) || getRank(player).isAtLeast(required);
    }

    public int size() {
        return ranks.size();
    }

    public void reload() {
        ranks.clear();
        storage.reload();
        ranks.putAll(storage.loadAll());
        LOGGER.info("Ranks reloaded — {} entries active", ranks.size());
    }
}