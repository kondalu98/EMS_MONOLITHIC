package com.event.controller;

import com.event.entity.Event;
import com.event.service.EventService;
import com.event.service.EventServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/events")

public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {

        this.eventService = eventService;
    }

    @PostMapping("/event")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event createdEvent = eventService.createEvent(event);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Event event = eventService.getEventById(id);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @PutMapping("/event/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
        Event updatedEvent = eventService.updateEvent(id, eventDetails);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/event/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long id) {
        String result = eventService.deleteEvent(id);
        return new ResponseEntity<>(result, HttpStatus.OK); // 200 OK with message
    }

    @GetMapping("/category")
    public ResponseEntity<List<Event>> getByCategory(@RequestParam String category) {
        return ResponseEntity.ok(eventService.findByCategory(category));
    }

    @GetMapping("/location")
    public ResponseEntity<List<Event>> getByLocation(@RequestParam String location) {
        return ResponseEntity.ok(eventService.findByLocation(location));
    }

    @GetMapping("/date")
    public ResponseEntity<List<Event>> getByDate(@RequestParam LocalDate date) {
        return ResponseEntity.ok(eventService.findByDate(date));
    }
}
