package com.event.service;

import com.event.entity.Event;
import com.event.exception.EventNotFoundException;
import com.event.repo.EventRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    private Event event;

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
    void testCreateEvent() {
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        Event created = eventService.createEvent(event);

        assertNotNull(created);
        assertEquals("Annual Fest", created.getName());

        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void testGetEventById() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Event foundEvent = eventService.getEventById(1L);

        assertNotNull(foundEvent);
        assertEquals("Annual Fest", foundEvent.getName());
        verify(eventRepository).findById(1L);
    }

    @Test
    void testGetEventById_NotFound() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> eventService.getEventById(1L));
    }

    @Test
    void testFindByCategory() {
        when(eventRepository.findByCategory("Cultural")).thenReturn(List.of(event));

        List<Event> events = eventService.findByCategory("Cultural");

        assertNotNull(events);
        assertEquals(1, events.size());
        verify(eventRepository).findByCategory("Cultural");
    }

    @Test
    void testFindByLocation() {
        when(eventRepository.findByLocation("Hyderabad")).thenReturn(List.of(event));

        List<Event> events = eventService.findByLocation("Hyderabad");

        assertNotNull(events);
        assertEquals(1, events.size());
        verify(eventRepository).findByLocation("Hyderabad");
    }

    @Test
    void testFindByDate() {
        LocalDate date = LocalDate.of(2025, 7, 1);
        when(eventRepository.findByDate(date)).thenReturn(List.of(event));

        List<Event> events = eventService.findByDate(date);

        assertNotNull(events);
        assertEquals(1, events.size());
        verify(eventRepository).findByDate(date);
    }

    @Test
    void testGetAllEvents() {
        when(eventRepository.findAll()).thenReturn(Arrays.asList(event));

        List<Event> events = eventService.getAllEvents();

        assertNotNull(events);
        assertEquals(1, events.size());
        verify(eventRepository).findAll();
    }

    @Test
    void testUpdateEvent() {
        Event updatedEvent = new Event();
        updatedEvent.setName("Updated Fest");
        updatedEvent.setCategory("Music");
        updatedEvent.setLocation("Bangalore");
        updatedEvent.setDate(LocalDate.of(2025, 8, 1));
        updatedEvent.setOrganizerID("ORG456");

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Event result = eventService.updateEvent(1L, updatedEvent);

        assertEquals("Updated Fest", result.getName());
        assertEquals("Music", result.getCategory());
        assertEquals("Bangalore", result.getLocation());

        verify(eventRepository).findById(1L);
        verify(eventRepository).save(event);
    }

    @Test
    void testUpdateEvent_NotFound() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> eventService.updateEvent(1L, event));
    }

    @Test
    void testDeleteEvent() {
        Long eventId = 1L;

        when(eventRepository.existsById(eventId)).thenReturn(true);

        eventService.deleteEvent(eventId);

        verify(eventRepository).existsById(eventId);
        verify(eventRepository).deleteById(eventId);
    }

    @Test
    void testDeleteEvent_NotFound() {
        when(eventRepository.existsById(1L)).thenReturn(false);

        assertThrows(EventNotFoundException.class, () -> eventService.deleteEvent(1L));
    }
}
