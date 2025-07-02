package com.event.controller;

import com.event.entity.Notification;
import com.event.service.NotificationService;
import com.event.service.NotificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {


    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/notification")
    public ResponseEntity<Notification> createNotification(
            @RequestParam Long userId,
            @RequestParam Long eventId,
            @RequestParam String message) {
        Notification notification = notificationService.sendNotification(userId, eventId, message);
        return new ResponseEntity<>(notification, HttpStatus.CREATED); // 201 Created
    }

    @GetMapping("alerts/{userId}")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.getUserNotifications(userId);
        if (notifications.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        }
        return new ResponseEntity<>(notifications, HttpStatus.OK); // 200 OK
    }
}
