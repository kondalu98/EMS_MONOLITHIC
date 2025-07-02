package com.event.exception;

import java.time.LocalDate;

public class EventDateNotFoundException extends RuntimeException {
    public EventDateNotFoundException(LocalDate date) {
        super("No events found on date: " + date);
    }
}