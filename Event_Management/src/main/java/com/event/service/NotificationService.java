package com.event.service;

import com.event.entity.Notification;

import java.util.List;

public interface NotificationService {

    public Notification sendNotification(Long userId, Long eventId, String message);
    public List<Notification> getUserNotifications(Long userId);
}
