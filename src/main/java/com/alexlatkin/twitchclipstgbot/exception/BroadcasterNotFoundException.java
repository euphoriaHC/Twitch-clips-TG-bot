package com.alexlatkin.twitchclipstgbot.exception;



public class BroadcasterNotFoundException extends RuntimeException {
    public BroadcasterNotFoundException(String message) {
        super(message);
    }
}
