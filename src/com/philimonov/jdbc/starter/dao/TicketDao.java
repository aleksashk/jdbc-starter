package com.philimonov.jdbc.starter.dao;

import com.philimonov.jdbc.starter.entity.Ticket;
import com.philimonov.jdbc.starter.exception.DaoException;
import com.philimonov.jdbc.starter.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class TicketDao {

    private static TicketDao INSTANCE = new TicketDao();
    private static final String DELETE_SQL = """
                delete from ticket where id = ?
            """;
    private static final String SAVE_SQL = """
                insert into ticket(passenger_no, passenger_name, flight_id, seat_no, cost) 
                values (?,?,?,?,?)
            """;

    private TicketDao() {
    }

    public static TicketDao getINSTANCE() {
        return INSTANCE;
    }

    public Ticket save(Ticket ticket) {
        try (Connection connection = ConnectionManager.get()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, ticket.getPassengerNo());
            preparedStatement.setString(2, ticket.getPassengerName());
            preparedStatement.setLong(3, ticket.getFlightId());
            preparedStatement.setString(4, ticket.getSeatNo());
            preparedStatement.setBigDecimal(5, ticket.getCost());

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                ticket.setId(generatedKeys.getLong("id"));
            }
            return ticket;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    public boolean delete(Long id) {

        try (Connection connection = ConnectionManager.get()) {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL);
            preparedStatement.setLong(1, id);

            return preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

}
