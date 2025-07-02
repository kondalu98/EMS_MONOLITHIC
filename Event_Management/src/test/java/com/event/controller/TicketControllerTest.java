package com.event.controller;

import com.event.entity.Event;
import com.event.entity.Ticket;
import com.event.entity.User;
import com.event.service.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketService ticketService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Ticket ticket;
    private Event event;
    private User user;

    @BeforeEach
    void setUp() {
        event = new Event();
        event.setEventID(100L);

        user = new User();
        user.setId(200L);

        ticket = new Ticket();
        ticket.setTicketId(1L);
        ticket.setEvent(event);
        ticket.setUser(user);
        ticket.setBookingDate(LocalDateTime.now());
        ticket.setStatus(Ticket.TicketStatus.CONFIRMED);
    }

    @Test
    void testBookTicket() throws Exception {
        when(ticketService.bookTicket(anyLong(), anyLong())).thenReturn(ticket);

        Map<String, Long> request = new HashMap<>();
        request.put("eventId", 100L);
        request.put("userId", 200L);

        mockMvc.perform(post("/api/tickets/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ticketId").value(1L))
                .andExpect(jsonPath("$.event.eventID").value(100L))
                .andExpect(jsonPath("$.user.id").value(200L))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void testGetTicketById() throws Exception {
        when(ticketService.getTicketById(anyLong())).thenReturn(ticket);

        mockMvc.perform(get("/api/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketId").value(1L))
                .andExpect(jsonPath("$.event.eventID").value(100L))
                .andExpect(jsonPath("$.user.id").value(200L));
    }

    @Test
    void testGetTicketsByUserId() throws Exception {
        List<Ticket> tickets = Collections.singletonList(ticket);
        when(ticketService.getTicketsByUserId(anyLong())).thenReturn(tickets);

        mockMvc.perform(get("/api/tickets/user/200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ticketId").value(1L))
                .andExpect(jsonPath("$[0].event.eventID").value(100L))
                .andExpect(jsonPath("$[0].user.id").value(200L));
    }

    @Test
    void testCancelTicket() throws Exception {
        Ticket canceledTicket = new Ticket();
        canceledTicket.setTicketId(1L);
        canceledTicket.setEvent(event);
        canceledTicket.setUser(user);
        canceledTicket.setBookingDate(LocalDateTime.now());
        canceledTicket.setStatus(Ticket.TicketStatus.CANCELED);

        when(ticketService.cancelTicket(anyLong())).thenReturn(canceledTicket);

        mockMvc.perform(patch("/api/tickets/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELED"));
    }

    @Test
    void testGetAllTickets() throws Exception {
        List<Ticket> tickets = Collections.singletonList(ticket);
        when(ticketService.getAllTickets()).thenReturn(tickets);

        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ticketId").value(1L))
                .andExpect(jsonPath("$[0].event.eventID").value(100L))
                .andExpect(jsonPath("$[0].user.id").value(200L));
    }
}
