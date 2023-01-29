package com.philimonov.jdbc.starter;

import com.philimonov.jdbc.starter.util.ConnectionManager;
import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.SQLException;

public class JdbcRunner {
    public static void main(String[] args) {
        Class<Driver> driverClass = Driver.class;
        try (Connection connection = ConnectionManager.open()) {
            System.out.println(connection.getTransactionIsolation());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
