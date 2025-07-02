package com.event.exception;

public class EventCategoryNotFoundException extends RuntimeException {
    public EventCategoryNotFoundException(String category) {
        super("No events found in category: " + category);
    }
}