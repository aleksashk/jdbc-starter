package com.philimonov.jdbc.starter;

import com.philimonov.jdbc.starter.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class TransactionRunner {
    public static void main(String[] args) {
        long flightId = 9;
        String deleteFlightSql = """
                    delete from flight where id = ?;
                    """;
        String deleteTicketsSql = """
                    delete from ticket where flight_id = ?;
                    """;
        try (Connection connection = ConnectionManager.open();
             PreparedStatement deleteFlightStatement = connection.prepareStatement(deleteFlightSql);
             PreparedStatement deleteTicketsStatement = connection.prepareStatement(deleteTicketsSql)){
            deleteFlightStatement.setLong(1, flightId);
            deleteTicketsStatement.setLong(1, flightId);
            deleteTicketsStatement.executeUpdate();
            if(true){
                throw new RuntimeException("Something wrong!!");
            }
            deleteFlightStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
