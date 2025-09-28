package com.example.restful_sample.net;

import com.example.restful_sample.models.CreatePostRequest;
import com.example.restful_sample.models.Post;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonPlaceholderService {
    @GET("posts")
    Call<List<Post>> getPosts();

    @POST("posts")
    Call<Post> createPost(@Body CreatePostRequest req);
}
