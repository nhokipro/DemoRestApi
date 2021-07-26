package com.DemoRestApi.ConnectDB;

import lombok.Data;

@Data
public class configDataSource {
    private final String DB_URL = "jdbc:mysql://localhost:3306/Demo";
    private final String USER = "root";
    private final String PASS = "root";
    public final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private final int maximumPoolSize = 10;

    public configDataSource() {
        super();
    }
}
