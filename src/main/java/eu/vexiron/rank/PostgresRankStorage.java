package eu.vexiron.rank;

import eu.vexiron.database.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class PostgresRankStorage implements RankStorage {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostgresRankStorage.class);

    private final Database database;
    private final Map<UUID, Rank> cache = new ConcurrentHashMap<>();

    public PostgresRankStorage(Database database) {
        this.database = database;
        cache.putAll(loadFromDatabase());
    }

    @Override
    public Map<UUID, Rank> loadAll() {
        return new HashMap<>(cache);
    }

    @Override
    public void save(UUID uuid, Rank rank) {
        cache.put(uuid, rank);

        String sql = """
            INSERT INTO ranks (uuid, rank)
            VALUES (?, ?)
            ON CONFLICT (uuid) DO UPDATE SET rank = EXCLUDED.rank
            """;

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, uuid);
            stmt.setString(2, rank.name());
            stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error("Failed to save rank for {}", uuid, e);
        }
    }

    @Override
    public void delete(UUID uuid) {
        cache.remove(uuid);

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM ranks WHERE uuid = ?")) {

            stmt.setObject(1, uuid);
            stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error("Failed to delete rank for {}", uuid, e);
        }
    }

    @Override
    public void saveAll(Map<UUID, Rank> ranks) {
        cache.clear();
        cache.putAll(ranks);

        try (Connection conn = database.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement clear = conn.prepareStatement("DELETE FROM ranks")) {
                clear.executeUpdate();
            }

            String sql = "INSERT INTO ranks (uuid, rank) VALUES (?, ?)";
            try (PreparedStatement insert = conn.prepareStatement(sql)) {
                for (Map.Entry<UUID, Rank> entry : ranks.entrySet()) {
                    insert.setObject(1, entry.getKey());
                    insert.setString(2, entry.getValue().name());
                    insert.addBatch();
                }
                insert.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            LOGGER.error("Failed to save all ranks", e);
        }
    }

    @Override
    public void reload() {
        cache.clear();
        cache.putAll(loadFromDatabase());
        LOGGER.info("Reloaded {} ranks from database", cache.size());
    }

    private Map<UUID, Rank> loadFromDatabase() {
        Map<UUID, Rank> result = new HashMap<>();

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT uuid, rank FROM ranks");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                UUID uuid = (UUID) rs.getObject("uuid");
                String rankName = rs.getString("rank");
                try {
                    result.put(uuid, Rank.fromString(rankName));
                } catch (IllegalArgumentException e) {
                    LOGGER.warn("Skipped invalid rank entry: {} -> {}", uuid, rankName);
                }
            }

        } catch (SQLException e) {
            LOGGER.error("Failed to load ranks from database", e);
        }

        return result;
    }
}