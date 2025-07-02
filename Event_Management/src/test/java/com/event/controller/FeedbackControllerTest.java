package com.event.controller;

import com.event.entity.Event;
import com.event.entity.Feedback;
import com.event.entity.User;
import com.event.service.FeedbackService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FeedbackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FeedbackService feedbackService;

    private Feedback feedback;

    @BeforeEach
    void setUp() {
        // Fake User
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        // Fake Event
        Event event = new Event();
        event.setEventID(1L);
        event.setName("Sample Event");

        feedback = new Feedback();
        feedback.setFeedbackID(1L);
        feedback.setUser(user);
        feedback.setEvent(event);
        feedback.setRating(5);
        feedback.setComments("Amazing event!");
        feedback.setSubmittedTimestamp(LocalDateTime.now());
    }

    @Test
    void testSubmitFeedback() throws Exception {
        when(feedbackService.submitFeedback(anyLong(), anyLong(), anyInt(), anyString()))
                .thenReturn(feedback);

        mockMvc.perform(post("/api/feedback")
                        .param("userId", "1")
                        .param("eventId", "1")
                        .param("rating", "5")
                        .param("comments", "Amazing event!"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.feedbackID").value(1L))
                .andExpect(jsonPath("$.comments").value("Amazing event!"))
                .andExpect(jsonPath("$.user.id").value(1L))
                .andExpect(jsonPath("$.event.eventID").value(1L));
    }

    @Test
    @WithMockUser(username = "admin@example.com")
    void testGetEventFeedback() throws Exception {
        List<Feedback> feedbackList = Arrays.asList(feedback);
        when(feedbackService.getEventFeedback(anyLong())).thenReturn(feedbackList);

        mockMvc.perform(get("/api/feedback/event/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].feedbackID").value(1L))
                .andExpect(jsonPath("$[0].comments").value("Amazing event!"))
                .andExpect(jsonPath("$[0].user.id").value(1L))
                .andExpect(jsonPath("$[0].event.eventID").value(1L));
    }

    @Test
    void testGetUserFeedback() throws Exception {
        List<Feedback> feedbackList = Arrays.asList(feedback);
        when(feedbackService.getUserFeedback(anyLong())).thenReturn(feedbackList);

        mockMvc.perform(get("/api/feedback/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].feedbackID").value(1L))
                .andExpect(jsonPath("$[0].comments").value("Amazing event!"))
                .andExpect(jsonPath("$[0].user.id").value(1L))
                .andExpect(jsonPath("$[0].event.eventID").value(1L));
    }

    @Test
    @WithMockUser(username = "admin@example.com")
    void testGetAverageRating() throws Exception {
        when(feedbackService.calculateAverageRating(anyLong())).thenReturn(4.5);

        mockMvc.perform(get("/api/feedback/event-rating/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("4.5"));
    }
}
