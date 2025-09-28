package com.example.restful_sample.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.restful_sample.data.PostsRepository;
import com.example.restful_sample.models.CreatePostRequest;
import com.example.restful_sample.models.Post;

import java.util.List;

public class PostsViewModel extends ViewModel {
    private final PostsRepository repo = new PostsRepository();

    public LiveData<List<Post>> posts() { return repo.posts(); }
    public LiveData<String> error() { return repo.error(); }

    public void load() { repo.loadPosts(); }
    public void createSample() {
        repo.createPost(new CreatePostRequest(123, "Hello from Android", "This is a demo POST."));
    }
}
