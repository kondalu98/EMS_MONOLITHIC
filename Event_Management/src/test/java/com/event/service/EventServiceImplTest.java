package com.event.service;

import com.event.entity.Event;
import com.event.exception.EventNotFoundException;
import com.event.repo.EventRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
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

        assertThat(created).isNotNull();
        assertThat(created.getName()).isEqualTo("Annual Fest");
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void testGetEventById() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Event foundEvent = eventService.getEventById(1L);

        assertThat(foundEvent).isNotNull();
        assertThat(foundEvent.getName()).isEqualTo("Annual Fest");
        verify(eventRepository).findById(1L);
    }

    @Test
    void testGetEventById_NotFound() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.getEventById(1L))
                .isInstanceOf(EventNotFoundException.class);
    }

    @Test
    void testFindByCategory() {
        when(eventRepository.findByCategory("Cultural")).thenReturn(List.of(event));

        List<Event> events = eventService.findByCategory("Cultural");

        assertThat(events).hasSize(1);
        verify(eventRepository).findByCategory("Cultural");
    }

    @Test
    void testFindByLocation() {
        when(eventRepository.findByLocation("Hyderabad")).thenReturn(List.of(event));

        List<Event> events = eventService.findByLocation("Hyderabad");

        assertThat(events).hasSize(1);
        verify(eventRepository).findByLocation("Hyderabad");
    }

    @Test
    void testFindByDate() {
        LocalDate date = LocalDate.of(2025, 7, 1);
        when(eventRepository.findByDate(date)).thenReturn(List.of(event));

        List<Event> events = eventService.findByDate(date);

        assertThat(events).hasSize(1);
        verify(eventRepository).findByDate(date);
    }

    @Test
    void testGetAllEvents() {
        when(eventRepository.findAll()).thenReturn(Arrays.asList(event));

        List<Event> events = eventService.getAllEvents();

        assertThat(events).hasSize(1);
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

        assertThat(result.getName()).isEqualTo("Updated Fest");
        assertThat(result.getCategory()).isEqualTo("Music");
        assertThat(result.getLocation()).isEqualTo("Bangalore");
        verify(eventRepository).findById(1L);
        verify(eventRepository).save(event);
    }

    @Test
    void testUpdateEvent_NotFound() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.updateEvent(1L, event))
                .isInstanceOf(EventNotFoundException.class);
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

        assertThatThrownBy(() -> eventService.deleteEvent(1L))
                .isInstanceOf(EventNotFoundException.class);
    }
}
