package com.example.moviereviewapp.Models;

public class ChatMessage {
    private final String name;
    private final String message;
    public ChatMessage( String name, String message) {
        this.name = name;
        this.message = message;
    }
    public String getName() {
        return name;
    }
    public String getMessage() {
        return message;
    }
}
