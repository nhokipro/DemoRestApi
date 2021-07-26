package com.DemoRestApi.ConnectDB;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {
    private static configDataSource configDataSource = new configDataSource();
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    static {
        config.setDriverClassName(configDataSource.getDB_DRIVER());
        config.setJdbcUrl(configDataSource.getDB_URL());
        config.setUsername(configDataSource.getUSER());
        config.setPassword(configDataSource.getPASS());
        config.setMaximumPoolSize(configDataSource.getMaximumPoolSize());
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);
    }

    public ConnectionPool() {
        super();
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}