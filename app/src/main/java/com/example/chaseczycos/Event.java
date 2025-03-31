package com.example.chaseczycos;

import com.google.firebase.Timestamp;

public class Event {
    String title, username, description;
    Timestamp timestamp;

    public Event() {
    }

    public Event(String title, String username, String description, Timestamp timestamp) {
        this.title = title;
        this.username = username;
        this.description = description;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

}
