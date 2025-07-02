package com.event.service;

import com.event.entity.Event;
import com.event.entity.Feedback;
import com.event.entity.User;
import com.event.exception.EventNotFoundException;
import com.event.exception.UserNotFoundException;
import com.event.repo.EventRepository;
import com.event.repo.FeedbackRepository;
import com.event.repo.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceImplTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private FeedbackServiceImpl feedbackService;

    private User user;
    private Event event;
    private Feedback feedback;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        event = new Event();
        event.setEventID(1L);
        event.setName("Sample Event");

        feedback = new Feedback();
        feedback.setFeedbackID(1L);
        feedback.setUser(user);
        feedback.setEvent(event);
        feedback.setRating(4);
        feedback.setComments("Good event");
        feedback.setSubmittedTimestamp(LocalDateTime.now());
    }

    @Test
    void testSubmitFeedback() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);

        Feedback savedFeedback = feedbackService.submitFeedback(1L, 1L, 4, "Good event");

        assertThat(savedFeedback).isNotNull();
        assertThat(savedFeedback.getRating()).isEqualTo(4);
        assertThat(savedFeedback.getComments()).isEqualTo("Good event");

        verify(userRepository).findById(1L);
        verify(eventRepository).findById(1L);
        verify(feedbackRepository).save(any(Feedback.class));
    }

    @Test
    void testSubmitFeedback_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> feedbackService.submitFeedback(1L, 1L, 5, "Great"))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void testSubmitFeedback_EventNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> feedbackService.submitFeedback(1L, 1L, 5, "Great"))
                .isInstanceOf(EventNotFoundException.class);
    }

    @Test
    void testGetEventFeedback() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event)); // ✅ Add this
        when(feedbackRepository.findByEventEventID(1L)).thenReturn(List.of(feedback));

        List<Feedback> feedbackList = feedbackService.getEventFeedback(1L);

        assertThat(feedbackList).hasSize(1);
        assertThat(feedbackList.get(0).getEvent().getEventID()).isEqualTo(1L);

        verify(eventRepository).findById(1L);
        verify(feedbackRepository).findByEventEventID(1L);
    }

    @Test
    void testGetUserFeedback() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user)); // ✅ Add this
        when(feedbackRepository.findByUserId(1L)).thenReturn(List.of(feedback));

        List<Feedback> feedbackList = feedbackService.getUserFeedback(1L);

        assertThat(feedbackList).hasSize(1);
        assertThat(feedbackList.get(0).getUser().getId()).isEqualTo(1L);

        verify(userRepository).findById(1L);
        verify(feedbackRepository).findByUserId(1L);
    }


    @Test
    void testCalculateAverageRating() {
        Feedback f1 = new Feedback();
        f1.setRating(4);
        Feedback f2 = new Feedback();
        f2.setRating(5);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event)); // ✅ Add this
        when(feedbackRepository.findByEventEventID(1L)).thenReturn(List.of(f1, f2));

        double avg = feedbackService.calculateAverageRating(1L);

        assertThat(avg).isEqualTo(4.5);
        verify(eventRepository).findById(1L);
        verify(feedbackRepository).findByEventEventID(1L);
    }


    @Test
    void testCalculateAverageRating_NoFeedback() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event)); // 👈 add this
        when(feedbackRepository.findByEventEventID(1L)).thenReturn(List.of());

        double avg = feedbackService.calculateAverageRating(1L);

        assertThat(avg).isEqualTo(0.0);
    }
    
    @Test
    void testGetEventFeedback_EventNotFound() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> feedbackService.getEventFeedback(1L))
                .isInstanceOf(EventNotFoundException.class);
    }

    @Test
    void testGetUserFeedback_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> feedbackService.getUserFeedback(1L))
                .isInstanceOf(UserNotFoundException.class);
    }
}
