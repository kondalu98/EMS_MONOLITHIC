package com.event.service;

import com.event.entity.Event;

import java.time.LocalDate;
import java.util.List;

public interface EventService {

    Event createEvent(Event event);

    Event getEventById(Long eventID); // throws EventNotFoundException if not found

    List<Event> findByCategory(String category);

    List<Event> findByLocation(String location);

    List<Event> findByDate(LocalDate date);

    List<Event> getAllEvents();

    Event updateEvent(Long eventID, Event eventDetails); // throws EventNotFoundException

    String deleteEvent(Long eventID); // throws EventNotFoundException
}
