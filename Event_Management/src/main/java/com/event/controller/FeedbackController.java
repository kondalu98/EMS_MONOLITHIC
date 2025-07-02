package com.event.controller;

import com.event.entity.Feedback;
import com.event.service.FeedbackService;
import com.event.service.FeedbackServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private  final FeedbackService feedbackService;
    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<Feedback> submitFeedback(@RequestParam Long userId,
                                                   @RequestParam Long eventId,
                                                   @RequestParam int rating,
                                                   @RequestParam String comments) {
        Feedback feedback = feedbackService.submitFeedback(userId, eventId, rating, comments);
        return new ResponseEntity<>(feedback, HttpStatus.CREATED); // 201 Created
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<Feedback>> getEventFeedback(@PathVariable Long eventId) {
        List<Feedback> feedbackList = feedbackService.getEventFeedback(eventId);
        if (feedbackList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        }
        return new ResponseEntity<>(feedbackList, HttpStatus.OK); // 200 OK
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Feedback>> getUserFeedback(@PathVariable Long userId) {
        List<Feedback> feedbackList = feedbackService.getUserFeedback(userId);
        if (feedbackList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        }
        return new ResponseEntity<>(feedbackList, HttpStatus.OK); // 200 OK
    }

    @GetMapping("/event-rating/{eventId}")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long eventId) {
        double avgRating = feedbackService.calculateAverageRating(eventId);
        return new ResponseEntity<>(avgRating, HttpStatus.OK); // 200 OK
    }
}
