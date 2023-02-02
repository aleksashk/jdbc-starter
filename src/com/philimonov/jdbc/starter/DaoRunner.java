package com.philimonov.jdbc.starter;

import com.philimonov.jdbc.starter.dao.TicketDao;
import com.philimonov.jdbc.starter.dto.TicketFilter;
import com.philimonov.jdbc.starter.entity.Ticket;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class DaoRunner {
    public static void main(String[] args) {

        Optional<Ticket> ticket = TicketDao
                .getInstance().findById(5L);
        System.out.println(ticket);
    }

    private static void filterTest() {
        TicketFilter ticketFilter = new TicketFilter(3, 2);
        List<Ticket> tickets = TicketDao.getInstance().findAll(ticketFilter);
        System.out.println(tickets);
    }

    private static void updateTest() {
        TicketDao ticketDao = TicketDao.getInstance();
        Optional<Ticket> maybeTicket = ticketDao.findById(2L);
        System.out.println(maybeTicket);
        maybeTicket.ifPresent(ticket -> {
            ticket.setCost(BigDecimal.valueOf(188.88));
            ticketDao.update(ticket);
        });
    }

    private static void saveTest() {
        TicketDao ticketDao = TicketDao.getInstance();
        Ticket ticket = new Ticket();
        ticket.setPassengerNo("1234567");
        ticket.setPassengerName("Test");
//        ticket.setFlight(3L);
        ticket.setSeatNo("B3");
        ticket.setCost(BigDecimal.TEN);

        Ticket saveTicket = ticketDao.save(ticket);
        System.out.println(saveTicket);
    }
}
