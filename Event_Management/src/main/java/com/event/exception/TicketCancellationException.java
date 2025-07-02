package com.event.exception;



public class TicketCancellationException extends RuntimeException {
    public TicketCancellationException(String message) {
        super(message);
    }
}
