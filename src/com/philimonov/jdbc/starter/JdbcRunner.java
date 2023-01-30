package com.philimonov.jdbc.starter;

import com.philimonov.jdbc.starter.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcRunner {
    public static void main(String[] args) throws SQLException {
        Long flightId = 3L;
        List<Long> result = getTicketsByFlightId(flightId);
        System.out.println(result);
    }

    private static List<Long> getTicketsByFlightId(Long flightId) throws SQLException {
        String sql = """
                select * from ticket where flight_id = ?
                """;
        List<Long> result = new ArrayList<>();
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, flightId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(resultSet.getObject("id", Long.class));
            }
            return result;
        }
    }
}
