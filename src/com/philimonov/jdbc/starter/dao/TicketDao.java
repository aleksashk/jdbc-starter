package com.philimonov.jdbc.starter.dao;

public class TicketDao {

    private static TicketDao INSTANCE = new TicketDao();
    private TicketDao(){}

    public static TicketDao getINSTANCE() {
        return INSTANCE;
    }

}
