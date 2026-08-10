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
            CREATE TABLE IF NOT EXISTS players (
                uuid UUID PRIMARY KEY,
                username VARCHAR(16) NOT NULL,
                first_join BIGINT NOT NULL,
                last_join BIGINT NOT NULL,
                playtime BIGINT DEFAULT 0
            )
        """);

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS ranks (
                uuid UUID PRIMARY KEY,
                rank VARCHAR(32) NOT NULL
            )
        """);

            LOGGER.info("Database tables ready");

        } catch (SQLException e) {
            LOGGER.error("Failed to setup database tables", e);
        }
    }
}