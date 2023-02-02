package com.philimonov.jdbc.starter;

import com.philimonov.jdbc.starter.dao.TicketDao;
import com.philimonov.jdbc.starter.dto.TicketFilter;
import com.philimonov.jdbc.starter.entity.Ticket;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class DaoRunner {
    public static void main(String[] args) {
        TicketFilter ticketFilter = new TicketFilter(3, 2);
        List<Ticket> tickets = TicketDao.getINSTANCE().findAll(ticketFilter);
        System.out.println(tickets);

//        List<Ticket> tickets = TicketDao.getINSTANCE().findAll();
//        System.out.println(tickets);
//        updateTest();
//        saveTest();
//        TicketDao ticketDao = TicketDao.getINSTANCE();
//        boolean deleteResult = ticketDao.delete(58L);
//        System.out.println(deleteResult);
    }

    private static void updateTest() {
        TicketDao ticketDao = TicketDao.getINSTANCE();
        Optional<Ticket> maybeTicket = ticketDao.findById(2L);
        System.out.println(maybeTicket);
        maybeTicket.ifPresent(ticket -> {
            ticket.setCost(BigDecimal.valueOf(188.88));
            ticketDao.update(ticket);
        });
    }

    private static void saveTest() {
        TicketDao ticketDao = TicketDao.getINSTANCE();
        Ticket ticket = new Ticket();
        ticket.setPassengerNo("1234567");
        ticket.setPassengerName("Test");
        ticket.setFlightId(3L);
        ticket.setSeatNo("B3");
        ticket.setCost(BigDecimal.TEN);

        Ticket saveTicket = ticketDao.save(ticket);
        System.out.println(saveTicket);
    }
}
