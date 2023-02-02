package com.philimonov.jdbc.starter.dao;

import com.philimonov.jdbc.starter.dto.TicketFilter;
import com.philimonov.jdbc.starter.entity.Flight;
import com.philimonov.jdbc.starter.entity.Ticket;
import com.philimonov.jdbc.starter.exception.DaoException;
import com.philimonov.jdbc.starter.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TicketDao {

    private static final TicketDao INSTANCE = new TicketDao();
    private static final String DELETE_SQL = """
                delete from ticket where id = ?
            """;
    private static final String SAVE_SQL = """
                insert into ticket(passenger_no, passenger_name, flight_id, seat_no, cost) 
                values (?,?,?,?,?)
            """;
    private static final String UPDATE_SQL = """
                UPDATE ticket 
                set passenger_no=?,
                passenger_name=?,
                flight_id=?,
                seat_no=?,
                cost=?
                where id=?
            """;

    private static final String FIND_ALL_SQL = """
             select ticket.id,
             passenger_no,
             passenger_name,
             flight_id,
             seat_no,
             cost,
             f.status,
             f.aircraft_id,
             f.arrival_airport_code,
             f.arrival_date,
             f.departure_airport_code,
             f.departure_date
             from ticket
             join flight f on ticket.flight_id = f.id
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            where ticket.id=?
            """;

    private TicketDao() {
    }

    public static TicketDao getInstance() {
        return INSTANCE;
    }

    public List<Ticket> findAll(TicketFilter filter) {
        List<Object> parameters = new ArrayList<>();
        parameters.add(filter.limit());
        parameters.add(filter.offset());
        String sql = FIND_ALL_SQL + """
                limit ?
                offset ?
                """;
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Ticket> tickets = new ArrayList<>();
            while (resultSet.next()) {
                tickets.add(buildTicket(resultSet));
            }
            return tickets;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    public List<Ticket> findAll() {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Ticket> tickets = new ArrayList<>();
            while (resultSet.next()) {
                tickets.add(buildTicket(resultSet));
            }
            return tickets;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    public Optional<Ticket> findById(Long id) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Ticket ticket = new Ticket();
            if (resultSet.next()) {
                ticket = buildTicket(resultSet);
            }
            return Optional.of(ticket);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private static Ticket buildTicket(ResultSet resultSet) throws SQLException {
        Flight flight = new Flight(
                resultSet.getLong("flight_id"),
                resultSet.getString("flight_no"),
                resultSet.getTimestamp("departure_date").toLocalDateTime(),
                resultSet.getString("departure_airport_code"),
                resultSet.getTimestamp("arrival_date").toLocalDateTime(),
                resultSet.getString("arrival_airport_code"),
                resultSet.getInt("aircraft_id"),
                resultSet.getString("status")
        );
        return new Ticket(
                resultSet.getLong("id"),
                resultSet.getString("passenger_no"),
                resultSet.getString("passenger_name"),
                flight,
                resultSet.getString("seat_no"),
                resultSet.getBigDecimal("cost")
        );
    }

    public void update(Ticket ticket) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, ticket.getPassengerNo());
            preparedStatement.setString(2, ticket.getPassengerName());
            preparedStatement.setLong(3, ticket.getFlight().id());
            preparedStatement.setString(4, ticket.getSeatNo());
            preparedStatement.setBigDecimal(5, ticket.getCost());
            preparedStatement.setLong(6, ticket.getId());

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new DaoException(e);
        }

    }

    public Ticket save(Ticket ticket) {
        try (Connection connection = ConnectionManager.get()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, ticket.getPassengerNo());
            preparedStatement.setString(2, ticket.getPassengerName());
            preparedStatement.setLong(3, ticket.getFlight().id());
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
