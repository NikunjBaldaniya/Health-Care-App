package com.example.healthcareapp.Model;

public class ChatMessage {
    public static final String SENT_BY_ME = "me";
    public static final String SENT_BY_BOT = "bot";
    public static final String SENT_BY_TYPING = "typing";

    private String message;
    private String sentBy;

    public ChatMessage(String message, String sentBy) {
        this.message = message;
        this.sentBy = sentBy;
    }

    public String getSentBy() {
        return sentBy;
    }

    public String getMessage() {
        return message;
    }
}
