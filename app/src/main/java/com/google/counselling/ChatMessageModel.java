package com.google.counselling;

public class ChatMessageModel {

    String message;

    public ChatMessageModel() {}

    public ChatMessageModel(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
