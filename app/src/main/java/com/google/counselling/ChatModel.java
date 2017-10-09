package com.google.counselling;

public class ChatModel {
    String uId, message;

    public ChatModel() {}

    public ChatModel(String uId, String message) {
        this.uId = uId;
        this.message = message;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
