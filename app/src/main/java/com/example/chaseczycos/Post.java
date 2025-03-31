package com.example.chaseczycos;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

public class Post {
    String title, username, description;
    Timestamp timestamp;
    GeoPoint location;

    public Post() {
    }

    public Post(String title, String username, String description, Timestamp timestamp, GeoPoint location) {
        this.title = title;
        this.username = username;
        this.description = description;
        this.timestamp = timestamp;
        this.location = location;
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

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }
}
