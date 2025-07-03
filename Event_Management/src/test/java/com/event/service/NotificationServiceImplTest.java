package com.event.service;

import com.event.entity.Event;
import com.event.entity.Notification;
import com.event.entity.User;
import com.event.exception.EventNotFoundException;
import com.event.exception.UserNotFoundException;
import com.event.repo.EventRepository;
import com.event.repo.NotificationRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private User user;
    private Event event;
    private Notification notification;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        event = new Event();
        event.setEventID(1L);
        event.setName("Sample Event");

        notification = new Notification();
        notification.setNotificationID(1L);
        notification.setUser(user);
        notification.setEvent(event);
        notification.setMessage("Test Notification");
        notification.setSentTimestamp(LocalDateTime.now());
    }

    @Test
    void testSendNotification() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        Notification result = notificationService.sendNotification(1L, 1L, "Test Notification");

        assertNotNull(result);
        assertEquals("Test Notification", result.getMessage());
        assertEquals(1L, result.getUser().getId());
        assertEquals(1L, result.getEvent().getEventID());

        verify(userRepository).findById(1L);
        verify(eventRepository).findById(1L);
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void testSendNotification_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                notificationService.sendNotification(1L, 1L, "Test Notification"));

        verify(userRepository).findById(1L);
        verifyNoInteractions(eventRepository, notificationRepository);
    }

    @Test
    void testSendNotification_EventNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () ->
                notificationService.sendNotification(1L, 1L, "Test Notification"));

        verify(userRepository).findById(1L);
        verify(eventRepository).findById(1L);

    }

    @Test
    void testGetUserNotifications() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(notificationRepository.findByUser_Id(1L)).thenReturn(List.of(notification));

        List<Notification> notifications = notificationService.getUserNotifications(1L);

        assertNotNull(notifications);
        assertEquals(1, notifications.size());
        assertEquals("Test Notification", notifications.get(0).getMessage());
        assertEquals(1L, notifications.get(0).getUser().getId());

        verify(userRepository).findById(1L);
        verify(notificationRepository).findByUser_Id(1L);
    }
}
