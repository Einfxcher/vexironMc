package eu.vexiron.profile;

import eu.vexiron.database.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public final class ProfileRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileRepository.class);

    private final Database database;

    public ProfileRepository(Database database) {
        this.database = database;
    }

    public Optional<PlayerProfile> load(UUID uuid) {
        String sql = "SELECT username, first_join, last_join, playtime FROM players WHERE uuid = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, uuid);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(new PlayerProfile(
                        uuid,
                        rs.getString("username"),
                        rs.getLong("first_join"),
                        rs.getLong("last_join"),
                        rs.getLong("playtime")
                ));
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to load profile for {}", uuid, e);
            return Optional.empty();
        }
    }

    public void save(PlayerProfile profile) {
        String sql = """
            INSERT INTO players (uuid, username, first_join, last_join, playtime)
            VALUES (?, ?, ?, ?, ?)
            ON CONFLICT (uuid) DO UPDATE SET
                username = EXCLUDED.username,
                last_join = EXCLUDED.last_join,
                playtime = EXCLUDED.playtime
            """;

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, profile.uuid());
            stmt.setString(2, profile.username());
            stmt.setLong(3, profile.firstJoin());
            stmt.setLong(4, profile.lastJoin());
            stmt.setLong(5, profile.playtime());
            stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error("Failed to save profile for {}", profile.uuid(), e);
        }
    }
}