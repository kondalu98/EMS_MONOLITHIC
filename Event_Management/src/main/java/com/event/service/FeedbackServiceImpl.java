package com.event.service;

import com.event.entity.Feedback;
import com.event.entity.Event;
import com.event.entity.User;
import com.event.exception.EventNotFoundException;
import com.event.exception.UserNotFoundException;
import com.event.repo.FeedbackRepository;
import com.event.repo.EventRepository;
import com.event.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Autowired
    public FeedbackServiceImpl(FeedbackRepository feedbackRepository,
                               UserRepository userRepository,
                               EventRepository eventRepository) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    public Feedback submitFeedback(Long userId, Long eventId, int rating, String comments) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        Feedback feedback = new Feedback();
        feedback.setUser(user);
        feedback.setEvent(event);
        feedback.setRating(rating);
        feedback.setComments(comments);
        feedback.setSubmittedTimestamp(LocalDateTime.now());

        return feedbackRepository.save(feedback);
    }

    public List<Feedback> getEventFeedback(Long eventId) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        return feedbackRepository.findByEventEventID(eventId);
    }
    public List<Feedback> getUserFeedback(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return feedbackRepository.findByUserId(userId);
    }

    public double calculateAverageRating(Long eventId) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        List<Feedback> feedbackList = feedbackRepository.findByEventEventID(eventId);
        return feedbackList.stream().mapToInt(Feedback::getRating).average().orElse(0.0);
    }

}
