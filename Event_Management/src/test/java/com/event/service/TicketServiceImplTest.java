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

import static org.assertj.core.api.Assertions.*;
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

        assertThat(booked).isNotNull();
        assertThat(booked.getEvent().getEventID()).isEqualTo(1L);
        assertThat(booked.getUser().getId()).isEqualTo(1L);
        assertThat(booked.getStatus()).isEqualTo(Ticket.TicketStatus.CONFIRMED);

        verify(eventRepository).findById(1L);
        verify(userRepository).findById(1L);
        verify(ticketRepository).save(any(Ticket.class));
    }

    @Test
    void testBookTicket_EventNotFound() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.bookTicket(1L, 1L))
                .isInstanceOf(EventNotFoundException.class);
    }

    @Test
    void testBookTicket_UserNotFound() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.bookTicket(1L, 1L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void testGetTicketById() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        Ticket found = ticketService.getTicketById(1L);

        assertThat(found).isNotNull();
        assertThat(found.getTicketId()).isEqualTo(1L);

        verify(ticketRepository).findById(1L);
    }

    @Test
    void testGetTicketById_NotFound() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.getTicketById(1L))
                .isInstanceOf(TicketNotFoundException.class);
    }

    @Test
    void testGetTicketsByUserId() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(ticketRepository.findByUser_Id(1L)).thenReturn(List.of(ticket));

        List<Ticket> tickets = ticketService.getTicketsByUserId(1L);

        assertThat(tickets).hasSize(1);
        assertThat(tickets.get(0).getUser().getId()).isEqualTo(1L);

        verify(userRepository).findById(1L);
        verify(ticketRepository).findByUser_Id(1L);
    }
    @Test
    void testCancelTicket() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        Ticket cancelled = ticketService.cancelTicket(1L);

        assertThat(cancelled.getStatus()).isEqualTo(Ticket.TicketStatus.CANCELED);

        verify(ticketRepository).findById(1L);
        verify(ticketRepository).save(any(Ticket.class));
    }

    @Test
    void testCancelTicket_NotFound() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.cancelTicket(1L))
                .isInstanceOf(TicketNotFoundException.class);
    }

    @Test
    void testCancelTicket_InvalidStatus() {
        ticket.setStatus(Ticket.TicketStatus.CANCELED);
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        assertThatThrownBy(() -> ticketService.cancelTicket(1L))
                .isInstanceOf(TicketCancellationException.class);

        verify(ticketRepository).findById(1L);
    }

    @Test
    void testGetAllTickets() {
        when(ticketRepository.findAll()).thenReturn(List.of(ticket));

        List<Ticket> tickets = ticketService.getAllTickets();

        assertThat(tickets).hasSize(1);
        verify(ticketRepository).findAll();
    }
}
