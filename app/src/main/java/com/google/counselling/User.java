package com.google.counselling;

public class User {

    public String uid;

    public String email;

    //public String firebaseToken;

    public String userName;

    public User() {}

    public User(String uid, String email, /*String firebaseToken, */String userName) {

        this.uid = uid;
        this.email = email;
        //this.firebaseToken = firebaseToken;
        this.userName = userName;

    }

}


