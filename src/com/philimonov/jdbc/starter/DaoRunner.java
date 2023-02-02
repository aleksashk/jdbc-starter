package com.philimonov.jdbc.starter;

import com.philimonov.jdbc.starter.dao.TicketDao;
import com.philimonov.jdbc.starter.entity.Ticket;

import java.math.BigDecimal;

public class DaoRunner {
    public static void main(String[] args) {
//        saveTest();
        TicketDao ticketDao = TicketDao.getINSTANCE();
        boolean deleteResult = ticketDao.delete(58L);
        System.out.println(deleteResult);
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
