package com.event.service;

import com.event.entity.Event;
import com.event.entity.Notification;
import com.event.entity.User;
import com.event.exception.EventNotFoundException;
import com.event.exception.UserNotFoundException;
import com.event.repo.EventRepository;
import com.event.repo.NotificationRepository;
import com.event.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Autowired
    public NotificationServiceImpl(
            NotificationRepository notificationRepository,
            UserRepository userRepository,
            EventRepository eventRepository
    ) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    public Notification sendNotification(Long userId, Long eventId, String message) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setEvent(event);
        notification.setMessage(message);
        notification.setSentTimestamp(LocalDateTime.now());

        return notificationRepository.save(notification);
    }

    public List<Notification> getUserNotifications(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return notificationRepository.findByUser_Id(userId);
    }}
