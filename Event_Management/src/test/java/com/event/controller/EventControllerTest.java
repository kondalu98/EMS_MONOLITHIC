package com.event.controller;

import com.event.entity.Event;
import com.event.service.EventService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;



import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventService eventService;

    private Event event;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        event = new Event();
        event.setEventID(1L);
        event.setName("Annual Fest");
        event.setCategory("Cultural");
        event.setLocation("Hyderabad");
        event.setDate(LocalDate.of(2025, 7, 1));
        event.setOrganizerID("ORG123");
    }

    @Test
    @WithMockUser(username = "admin@example.com")
    void testCreateEvent() throws Exception {
        when(eventService.createEvent(any(Event.class))).thenReturn(event);

        mockMvc.perform(post("/api/events/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.eventID").value(1L))
                .andExpect(jsonPath("$.name").value("Annual Fest"))
                .andExpect(jsonPath("$.category").value("Cultural"));
    }

    @Test
    void testGetEventById() throws Exception {
        when(eventService.getEventById(anyLong())).thenReturn(event);

        mockMvc.perform(get("/api/events/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventID").value(1L))
                .andExpect(jsonPath("$.name").value("Annual Fest"))
                .andExpect(jsonPath("$.location").value("Hyderabad"));
    }

    @Test
    void testGetAllEvents() throws Exception {
        List<Event> events = Arrays.asList(event);
        when(eventService.getAllEvents()).thenReturn(events);

        mockMvc.perform(get("/api/events")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eventID").value(1L))
                .andExpect(jsonPath("$[0].name").value("Annual Fest"));
    }

    @Test
    @WithMockUser(username = "admin@example.com")
    void testUpdateEvent() throws Exception {
        Event updatedEvent = new Event();
        updatedEvent.setEventID(1L);
        updatedEvent.setName("Updated Fest");
        updatedEvent.setCategory("Cultural");
        updatedEvent.setLocation("Hyderabad");
        updatedEvent.setDate(LocalDate.of(2025, 7, 2));
        updatedEvent.setOrganizerID("ORG123");

        when(eventService.updateEvent(eq(1L), any(Event.class))).thenReturn(updatedEvent);

        mockMvc.perform(put("/api/events/event/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEvent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Fest"))
                .andExpect(jsonPath("$.date").value("2025-07-02"));
    }

    @Test
    @WithMockUser(username = "admin@example.com")
    void testDeleteEvent() throws Exception {
        mockMvc.perform(delete("/api/events/event/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetByCategory() throws Exception {
        List<Event> events = Arrays.asList(event);
        when(eventService.findByCategory(eq("Cultural"))).thenReturn(events);

        mockMvc.perform(get("/api/events/category")
                        .param("category", "Cultural")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("Cultural"));
    }

    @Test
    void testGetByLocation() throws Exception {
        List<Event> events = Arrays.asList(event);
        when(eventService.findByLocation(eq("Hyderabad"))).thenReturn(events);

        mockMvc.perform(get("/api/events/location")
                        .param("location", "Hyderabad")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].location").value("Hyderabad"));
    }

    @Test
    void testGetByDate() throws Exception {
        List<Event> events = Arrays.asList(event);
        when(eventService.findByDate(eq(LocalDate.of(2025, 7, 1)))).thenReturn(events);

        mockMvc.perform(get("/api/events/date")
                        .param("date", "2025-07-01")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date").value("2025-07-01"));
    }
}
