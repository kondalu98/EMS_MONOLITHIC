package com.event.service;

import com.event.entity.Ticket;

import java.util.List;

public interface TicketService {

    Ticket bookTicket(Long eventId, Long userId); // throws EventNotFoundException, UserNotFoundException

    Ticket getTicketById(Long ticketId); // throws TicketNotFoundException

    List<Ticket> getTicketsByUserId(Long userId);

    Ticket cancelTicket(Long ticketId); // throws TicketNotFoundException, TicketCancellationException

    List<Ticket> getAllTickets();
}
