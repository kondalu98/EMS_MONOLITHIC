package com.event.entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    @ManyToOne
    @JoinColumn(name = "eventID", nullable = false)
    private Event event; // Foreign Key to Event entity

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private User user; // Foreign Key to User entity

    @Column(nullable = false)
    private LocalDateTime bookingDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status;

    // Enum for ticket status
    public enum TicketStatus {
        CONFIRMED,
        CANCELED
    }

    // Constructors
    public Ticket() {
    }

    public Ticket(Event event, User user, LocalDateTime bookingDate, TicketStatus status) {
        this.event = event;
        this.user = user;
        this.bookingDate = bookingDate;
        this.status = status;
    }

    // Getters and Setters
    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketId +
                ", event=" + (event != null ? event.getEventID() : "null") + // Avoid stack overflow
                ", user=" + (user != null ? user.getId() : "null") +     // Avoid stack overflow
                ", bookingDate=" + bookingDate +
                ", status=" + status +
                '}';
    }
}
