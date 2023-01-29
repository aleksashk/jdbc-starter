package com.philimonov.jdbc.starter;

import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcRunner {
    public static void main(String[] args) {
        Class<Driver> driverClass = Driver.class;
        String url = "jdbc:postgresql://localhost:5432/flight_repository";
        String username = "postgres";
        String password = "password";
        try (Connection connection = DriverManager.getConnection(url, username, password)){
            System.out.println(connection.getTransactionIsolation());
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
