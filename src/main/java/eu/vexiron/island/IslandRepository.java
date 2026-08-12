package eu.vexiron.island;

import eu.vexiron.database.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public final class IslandRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(IslandRepository.class);

    private final Database database;

    public IslandRepository(Database database) {
        this.database = database;
    }

    public Optional<Island> load(UUID owner) {
        String sql = "SELECT type, created_at, last_visited FROM islands WHERE owner_uuid = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, owner);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) return Optional.empty();

                Island island = new Island(
                        owner,
                        IslandType.valueOf(rs.getString("type")),
                        rs.getLong("created_at"),
                        rs.getLong("last_visited")
                );
                island.members().addAll(loadMembers(owner));
                return Optional.of(island);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to load island for {}", owner, e);
            return Optional.empty();
        }
    }

    public void save(Island island) {
        String sql = """
            INSERT INTO islands (owner_uuid, type, created_at, last_visited)
            VALUES (?, ?, ?, ?)
            ON CONFLICT (owner_uuid) DO UPDATE SET
                type = EXCLUDED.type,
                last_visited = EXCLUDED.last_visited
            """;

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, island.owner());
            stmt.setString(2, island.type().name());
            stmt.setLong(3, island.createdAt());
            stmt.setLong(4, island.lastVisited());
            stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error("Failed to save island for {}", island.owner(), e);
        }
    }

    public void delete(UUID owner) {
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM islands WHERE owner_uuid = ?")) {

            stmt.setObject(1, owner);
            stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error("Failed to delete island for {}", owner, e);
        }
    }

    public boolean exists(UUID owner) {
        String sql = "SELECT 1 FROM islands WHERE owner_uuid = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, owner);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to check island existence for {}", owner, e);
            return false;
        }
    }

    // ─── Members ─────────────────────────────────────

    private Set<UUID> loadMembers(UUID owner) throws SQLException {
        Set<UUID> members = new HashSet<>();
        String sql = "SELECT member_uuid FROM island_members WHERE island_owner = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, owner);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    members.add((UUID) rs.getObject("member_uuid"));
                }
            }
        }
        return members;
    }

    public void addMember(UUID owner, UUID member) {
        String sql = """
            INSERT INTO island_members (island_owner, member_uuid)
            VALUES (?, ?)
            ON CONFLICT DO NOTHING
            """;

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, owner);
            stmt.setObject(2, member);
            stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error("Failed to add member {} to island {}", member, owner, e);
        }
    }

    public void removeMember(UUID owner, UUID member) {
        String sql = "DELETE FROM island_members WHERE island_owner = ? AND member_uuid = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, owner);
            stmt.setObject(2, member);
            stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error("Failed to remove member {} from island {}", member, owner, e);
        }
    }
}