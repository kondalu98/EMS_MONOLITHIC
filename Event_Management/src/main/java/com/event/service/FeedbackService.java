package com.event.service;

import com.event.entity.Feedback;

import java.util.List;

public interface FeedbackService {

    public Feedback submitFeedback(Long userId, Long eventId, int rating, String comments);
    public List<Feedback> getEventFeedback(Long eventId);
    public List<Feedback> getUserFeedback(Long userId);
    public double calculateAverageRating(Long eventId);

}
