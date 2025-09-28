package com.example.restful_sample.models;

public class CreatePostRequest {
    public int userId;
    public String title;
    public String body;

    public CreatePostRequest(int userId, String title, String body) {
        this.userId = userId;
        this.title = title;
        this.body = body;
    }
}
