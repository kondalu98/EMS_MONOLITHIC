package com.event.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationID;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "eventID", nullable = false)
    private Event event;

    private String message;
    private LocalDateTime sentTimestamp;


    public Long getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(Long notificationID) {
        this.notificationID = notificationID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getSentTimestamp() {
        return sentTimestamp;
    }

    public void setSentTimestamp(LocalDateTime sentTimestamp) {
        this.sentTimestamp = sentTimestamp;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notificationID=" + notificationID +
                ", user=" + user +
                ", event=" + event +
                ", message='" + message + '\'' +
                ", sentTimestamp=" + sentTimestamp +
                '}';
    }

    public Notification(Long notificationID, User user, Event event, String message, LocalDateTime sentTimestamp) {
        this.notificationID = notificationID;
        this.user = user;
        this.event = event;
        this.message = message;
        this.sentTimestamp = sentTimestamp;
    }

    public Notification() {
    }


}
