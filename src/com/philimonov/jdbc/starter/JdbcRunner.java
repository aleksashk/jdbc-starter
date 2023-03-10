package com.philimonov.jdbc.starter;

import com.philimonov.jdbc.starter.util.ConnectionManager;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcRunner {
    public static void main(String[] args) throws SQLException {
//        List<Long> flightsBetween = getFlightsBetween(LocalDate.of(2020, 1, 1).atStartOfDay(), LocalDateTime.now());
//        System.out.println(flightsBetween);
//        System.out.println("----------------------------------");
//
//        Long flightId = 3L;
//        List<Long> result = getTicketsByFlightId(flightId);
//        System.out.println(result);

        try {
            checkMetaData();
        } finally {
            ConnectionManager.closePool();
        }

    }

    private static void checkMetaData() throws SQLException {
        try (Connection connection = ConnectionManager.get()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet catalogs = metaData.getCatalogs();
            while (catalogs.next()) {
                System.out.println(catalogs.getString(1));

                ResultSet schemas = metaData.getSchemas();
                while (schemas.next()) {
                    System.out.println(schemas.getString("TABLE_SCHEM"));
                    ResultSet tables = metaData.getTables(null, null, "%", null);
                    while (tables.next()) {
                        System.out.println(tables.getString("TABLE_NAME"));

                    }
                }
            }
        }
    }

    private static List<Long> getFlightsBetween(LocalDateTime start, LocalDateTime end) {
        String sql = """
                select id from flight where departure_date between ? and ?
                """;
        List<Long> result = new ArrayList<>();
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            System.out.println(preparedStatement);
            preparedStatement.setTimestamp(1, Timestamp.valueOf(start));
            System.out.println(preparedStatement);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(end));
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(resultSet.getLong("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;

    }

    private static List<Long> getTicketsByFlightId(Long flightId) throws SQLException {
        String sql = """
                select * from ticket where flight_id = ?
                """;
        List<Long> result = new ArrayList<>();
        try (Connection connection = ConnectionManager.get();
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
