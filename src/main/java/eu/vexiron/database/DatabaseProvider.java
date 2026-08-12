package eu.vexiron.database;

import eu.vexiron.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseProvider.class);

    private final Database database;

    public DatabaseProvider(Config config) {
        this.database = new Database(config.database);
        setupTables();
    }

    public Database get() {
        return database;
    }

    private void setupTables() {
        try (Connection conn = database.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
    CREATE TABLE IF NOT EXISTS islands (
        owner_uuid UUID PRIMARY KEY REFERENCES players(uuid) ON DELETE CASCADE,
        type VARCHAR(32) NOT NULL,
        created_at BIGINT NOT NULL,
        last_visited BIGINT NOT NULL
    )
""");

            stmt.execute("""
    CREATE TABLE IF NOT EXISTS island_members (
        island_owner UUID NOT NULL REFERENCES islands(owner_uuid) ON DELETE CASCADE,
        member_uuid UUID NOT NULL REFERENCES players(uuid) ON DELETE CASCADE,
        PRIMARY KEY (island_owner, member_uuid)
    )
""");

            stmt.execute("""
    CREATE INDEX IF NOT EXISTS idx_island_members_member
        ON island_members(member_uuid)
""");

            LOGGER.info("Database tables ready");

        } catch (SQLException e) {
            LOGGER.error("Failed to setup database tables", e);
        }
    }
}