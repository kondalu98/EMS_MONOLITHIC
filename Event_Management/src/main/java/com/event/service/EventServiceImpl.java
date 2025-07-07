package com.event.service;

import com.event.entity.Event;
import com.event.exception.EventNotFoundException;
import com.event.exception.EventCategoryNotFoundException;
import com.event.exception.EventDateNotFoundException;
import com.event.exception.EventLocationNotFoundException;
import com.event.repo.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public Event getEventById(Long eventID) {
        return eventRepository.findById(eventID)
                .orElseThrow(() -> new EventNotFoundException(eventID));
    }

    public List<Event> findByCategory(String category) {
        List<Event> events = eventRepository.findByCategory(category);
        if (events.isEmpty()) {
            throw new EventCategoryNotFoundException(category);
        }
        return events;
    }

    public List<Event> findByLocation(String location) {
        List<Event> events = eventRepository.findByLocation(location);
        if (events.isEmpty()) {
            throw new EventLocationNotFoundException(location);
        }
        return events;
    }

    public List<Event> findByDate(LocalDate date) {
        List<Event> events = eventRepository.findByDate(date);
        if (events.isEmpty()) {
            throw new EventDateNotFoundException(date);
        }
        return events;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event updateEvent(Long eventID, Event eventDetails) {
        Event existingEvent = eventRepository.findById(eventID)
                .orElseThrow(() -> new EventNotFoundException(eventID));

        existingEvent.setName(eventDetails.getName());
        existingEvent.setCategory(eventDetails.getCategory());
        existingEvent.setLocation(eventDetails.getLocation());
        existingEvent.setDate(eventDetails.getDate());
        existingEvent.setOrganizerID(eventDetails.getOrganizerID());

        return eventRepository.save(existingEvent);
    }

    public String deleteEvent(Long eventID) {
        if (!eventRepository.existsById(eventID)) {
            throw new EventNotFoundException(eventID);
        }
        eventRepository.deleteById(eventID);

        return "Event with ID " + eventID + " has been successfully deleted.";
    }
}
