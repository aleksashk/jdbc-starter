package com.philimonov.jdbc.starter.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static final String URL = "jdbc:postgresql://localhost:5432/flight_repository";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "password";

    private ConnectionManager(){

    }

    public static Connection open(){
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
