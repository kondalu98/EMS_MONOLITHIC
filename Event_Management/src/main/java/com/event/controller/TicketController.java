package com.event.controller;

import com.event.dto.BookingRequest;
import com.event.entity.Ticket;
import com.event.service.TicketService;
import com.event.service.TicketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }


    @PostMapping("/book")
    public ResponseEntity<?> bookTicket(@RequestBody BookingRequest request) {
        if (request.getEventId() == null || request.getUserId() == null) {
            return ResponseEntity.badRequest().body("Missing eventId or userId");
        }
        Ticket bookedTicket = ticketService.bookTicket(request.getEventId(), request.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(bookedTicket);
    }


    @GetMapping("/{ticketId}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long ticketId) {
        Ticket ticket = ticketService.getTicketById(ticketId);
        return new ResponseEntity<>(ticket, HttpStatus.OK); // 200
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Ticket>> getTicketsByUserId(@PathVariable Long userId) {
        List<Ticket> tickets = ticketService.getTicketsByUserId(userId);
        if (tickets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
        }
        return new ResponseEntity<>(tickets, HttpStatus.OK); // 200
    }

    @PatchMapping("/{ticketId}/cancel")
    public ResponseEntity<Ticket> cancelTicket(@PathVariable Long ticketId) {
        Ticket canceledTicket = ticketService.cancelTicket(ticketId);
        return new ResponseEntity<>(canceledTicket, HttpStatus.OK); // 200
    }


    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        List<Ticket> tickets = ticketService.getAllTickets();
        if (tickets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tickets, HttpStatus.OK); // 200
    }
}
