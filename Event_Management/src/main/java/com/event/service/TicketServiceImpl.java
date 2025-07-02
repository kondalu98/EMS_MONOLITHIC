package com.event.service;

import com.event.entity.Event;
import com.event.entity.Ticket;
import com.event.entity.User;
import com.event.exception.*;
import com.event.repo.EventRepository;
import com.event.repo.TicketRepository;
import com.event.repo.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository,
                             EventRepository eventRepository,
                             UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Ticket bookTicket(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Ticket newTicket = new Ticket();
        newTicket.setEvent(event);
        newTicket.setUser(user);
        newTicket.setBookingDate(LocalDateTime.now());
        newTicket.setStatus(Ticket.TicketStatus.CONFIRMED);

        return ticketRepository.save(newTicket);
    }

    public Ticket getTicketById(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));
    }

    public List<Ticket> getTicketsByUserId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return ticketRepository.findByUser_Id(userId);
    }
    @Transactional
    public Ticket cancelTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));

        if (ticket.getStatus() != Ticket.TicketStatus.CONFIRMED) {
            throw new TicketCancellationException("Only CONFIRMED tickets can be cancelled.");
        }

        ticket.setStatus(Ticket.TicketStatus.CANCELED);
        return ticketRepository.save(ticket);
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
}
