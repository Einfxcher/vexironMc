package eu.vexiron.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import eu.vexiron.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public final class Database {

    private static final Logger LOGGER = LoggerFactory.getLogger(Database.class);

    private final HikariDataSource dataSource;

    public Database(Config.Database config) {
        HikariConfig hikari = new HikariConfig();
        hikari.setJdbcUrl("jdbc:postgresql://" + config.host + ":" + config.port + "/" + config.name);
        hikari.setUsername(config.user);
        hikari.setPassword(config.password);
        hikari.setMaximumPoolSize(10);
        hikari.setPoolName("VexironPool");
        hikari.setConnectionTimeout(10_000);

        this.dataSource = new HikariDataSource(hikari);
        LOGGER.info("Connected to PostgreSQL at {}:{}", config.host, config.port);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}