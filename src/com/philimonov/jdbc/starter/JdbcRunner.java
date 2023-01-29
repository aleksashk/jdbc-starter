package com.philimonov.jdbc.starter;

import com.philimonov.jdbc.starter.util.ConnectionManager;
import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcRunner {
    public static void main(String[] args) throws SQLException{
        Class<Driver> driverClass = Driver.class;
        String firstRequest = """
                   insert into info(data) values ('Test1'),
                   ('Test2'), ('Test3'), ('Test4')""";
        try (Connection connection = ConnectionManager.open();
             Statement statement = connection.createStatement()) {
            System.out.println(statement.execute(firstRequest));
            System.out.println(connection.getSchema());
            System.out.println("****************");
            String sql = "select * from flight";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                System.out.println(resultSet.getString("flight_no") + " | " + resultSet.getString("departure_date") + " | " + resultSet.getString("departure_airport_code") + " | " + resultSet.getString("arrival_date") + " | " + resultSet.getString("arrival_airport_code") + " | " + resultSet.getInt("aircraft_id") + " | " + resultSet.getString("status"));
            }
        }

    }
}
