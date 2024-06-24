package com.example.Maledetta_TreEst.post;

public class OfficialPost {
    private final String title;
    private final String timestamp;
    private final String description;


    public OfficialPost(String title, String timestamp, String description) {
        this.title = title;
        this.timestamp = timestamp;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getTimestamp() {
        return timestamp.substring(0, 16);
    }

    public String getDescription() {
        return description;
    }
}
