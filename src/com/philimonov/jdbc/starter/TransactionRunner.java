package com.philimonov.jdbc.starter;

import com.philimonov.jdbc.starter.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionRunner {
    public static void main(String[] args) throws SQLException {
        long flightId = 8;
        String deleteFlightSql = """
                delete from flight where id = ?;
                """;
        String deleteTicketsSql = """
                delete from ticket where flight_id = ?;
                """;
        Connection connection = null;
        PreparedStatement deleteFlightStatement = null;
        PreparedStatement deleteTicketsStatement = null;
        try {
            connection = ConnectionManager.get();
            deleteFlightStatement = connection.prepareStatement(deleteFlightSql);
            deleteTicketsStatement = connection.prepareStatement(deleteTicketsSql);
            connection.setAutoCommit(false);
            deleteFlightStatement.setLong(1, flightId);
            deleteTicketsStatement.setLong(1, flightId);
            deleteTicketsStatement.executeUpdate();
            if (true) {
                throw new RuntimeException("Something wrong!!");
            }
            deleteFlightStatement.executeUpdate();
            connection.commit();
        } catch (Exception e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (deleteTicketsStatement != null) {
                deleteTicketsStatement.close();
            }
            if (deleteFlightStatement != null) {
                deleteFlightStatement.close();
            }
        }
    }
}
