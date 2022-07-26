package com.example.sharingapp;

import java.util.UUID;

public class Contact extends Observable{

    private String username;
    private String email;
    private String id;

    public Contact(String username, String email, String id){
        this.username = username;
        this.email = email;


        if (id == null){
            setId();
        }else {
            updateId(id);
        }
        notifyObservers();
    }

    public void updateId(String id) {
        this.id = id;
        notifyObservers();
    }

    public void setId() {
        this.id = UUID.randomUUID().toString();
        notifyObservers();
    }

    public String getId(){
        return id;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyObservers();
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyObservers();
    }

    public String getUsername() {
        return username;
    }
}
