package com.example.sharingapp;

import java.util.UUID;

public class Contact {

    private String username;
    private String email;
    private String id;

    public void Contact(String username, String email, String id){
        this.username = username;
        this.email = email;


        if (id == null){
            setId();
        }else {
            updateId();
        }
    }

    public void updateId(String id) {
        this.id = id;
    }

    public void setId() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId(){
        return this.id;
    }
}
