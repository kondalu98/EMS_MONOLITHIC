package com.event.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackID;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "eventID", nullable = false)
    private Event event;

    private int rating; // Example: 1 to 5 stars
    private String comments;
    private LocalDateTime submittedTimestamp;


    public Feedback(Long feedbackID, User user, Event event, int rating, String comments, LocalDateTime submittedTimestamp) {
        this.feedbackID = feedbackID;
        this.user = user;
        this.event = event;
        this.rating = rating;
        this.comments = comments;
        this.submittedTimestamp = submittedTimestamp;
    }

    public Feedback() {
    }

    public Long getFeedbackID() {
        return feedbackID;
    }

    public void setFeedbackID(Long feedbackID) {
        this.feedbackID = feedbackID;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDateTime getSubmittedTimestamp() {
        return submittedTimestamp;
    }

    public void setSubmittedTimestamp(LocalDateTime submittedTimestamp) {
        this.submittedTimestamp = submittedTimestamp;
    }
}
