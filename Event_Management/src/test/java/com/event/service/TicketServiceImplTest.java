package com.event.service;

import com.event.entity.Event;
import com.event.entity.Ticket;
import com.event.entity.User;
import com.event.exception.EventNotFoundException;
import com.event.exception.TicketCancellationException;
import com.event.exception.TicketNotFoundException;
import com.event.exception.UserNotFoundException;
import com.event.repo.EventRepository;
import com.event.repo.TicketRepository;
import com.event.repo.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private User user;
    private Event event;
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        event = new Event();
        event.setEventID(1L);
        event.setName("Sample Event");

        ticket = new Ticket();
        ticket.setTicketId(1L);
        ticket.setUser(user);
        ticket.setEvent(event);
        ticket.setBookingDate(LocalDateTime.now());
        ticket.setStatus(Ticket.TicketStatus.CONFIRMED);
    }

    @Test
    void testBookTicket() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        Ticket booked = ticketService.bookTicket(1L, 1L);

        assertNotNull(booked);
        assertEquals(1L, booked.getEvent().getEventID());
        assertEquals(1L, booked.getUser().getId());
        assertEquals(Ticket.TicketStatus.CONFIRMED, booked.getStatus());

        verify(eventRepository).findById(1L);
        verify(userRepository).findById(1L);
        verify(ticketRepository).save(any(Ticket.class));
    }

    @Test
    void testBookTicket_EventNotFound() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> ticketService.bookTicket(1L, 1L));
    }

    @Test
    void testBookTicket_UserNotFound() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> ticketService.bookTicket(1L, 1L));
    }

    @Test
    void testGetTicketById() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        Ticket found = ticketService.getTicketById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getTicketId());

        verify(ticketRepository).findById(1L);
    }

    @Test
    void testGetTicketById_NotFound() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> ticketService.getTicketById(1L));
    }

    @Test
    void testGetTicketsByUserId() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(ticketRepository.findByUser_Id(1L)).thenReturn(List.of(ticket));

        List<Ticket> tickets = ticketService.getTicketsByUserId(1L);

        assertNotNull(tickets);
        assertEquals(1, tickets.size());
        assertEquals(1L, tickets.get(0).getUser().getId());

        verify(userRepository).findById(1L);
        verify(ticketRepository).findByUser_Id(1L);
    }

    @Test
    void testCancelTicket() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        Ticket cancelled = ticketService.cancelTicket(1L);

        assertEquals(Ticket.TicketStatus.CANCELED, cancelled.getStatus());

        verify(ticketRepository).findById(1L);
        verify(ticketRepository).save(any(Ticket.class));
    }

    @Test
    void testCancelTicket_NotFound() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> ticketService.cancelTicket(1L));
    }

    @Test
    void testCancelTicket_InvalidStatus() {
        ticket.setStatus(Ticket.TicketStatus.CANCELED);
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        assertThrows(TicketCancellationException.class, () -> ticketService.cancelTicket(1L));

        verify(ticketRepository).findById(1L);
    }

    @Test
    void testGetAllTickets() {
        when(ticketRepository.findAll()).thenReturn(List.of(ticket));

        List<Ticket> tickets = ticketService.getAllTickets();

        assertNotNull(tickets);
        assertEquals(1, tickets.size());

        verify(ticketRepository).findAll();
    }
}
