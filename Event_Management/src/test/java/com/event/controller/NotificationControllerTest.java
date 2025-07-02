package com.event.controller;

import com.event.entity.Event;
import com.event.entity.Notification;
import com.event.entity.User;
import com.event.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificationService notificationService;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private Notification notification;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(1L);

        Event event = new Event();
        event.setEventID(1L);

        notification = new Notification(
                1L,
                user,
                event,
                "Event Reminder",
                LocalDateTime.of(2025, 7, 1, 12, 0)
        );
    }

    @Test
    @WithMockUser(username = "admin@example.com")
    void testCreateNotification() throws Exception {
        when(notificationService.sendNotification(anyLong(), anyLong(), eq("Event Reminder")))
                .thenReturn(notification);

        mockMvc.perform(post("/api/notifications/notification")
                        .param("userId", "1")
                        .param("eventId", "1")
                        .param("message", "Event Reminder")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.notificationID").value(1L))
                .andExpect(jsonPath("$.message").value("Event Reminder"))
                .andExpect(jsonPath("$.event.eventID").value(1L))
                .andExpect(jsonPath("$.user.id").value(1L));
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void testGetNotifications() throws Exception {
        when(notificationService.getUserNotifications(anyLong()))
                .thenReturn(List.of(notification));

        mockMvc.perform(get("/api/notifications/alerts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].notificationID").value(1L))
                .andExpect(jsonPath("$[0].message").value("Event Reminder"))
                .andExpect(jsonPath("$[0].event.eventID").value(1L))
                .andExpect(jsonPath("$[0].user.id").value(1L));
    }
}
