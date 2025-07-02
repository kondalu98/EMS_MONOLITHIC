package com.event.exception;

public class EventLocationNotFoundException extends RuntimeException {
    public EventLocationNotFoundException(String location) {
        super("No events found at location: " + location);
    }
}
