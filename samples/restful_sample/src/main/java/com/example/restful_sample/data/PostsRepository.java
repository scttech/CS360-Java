package com.example.restful_sample.data;

import androidx.annotation.MainThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.restful_sample.models.CreatePostRequest;
import com.example.restful_sample.models.Post;
import com.example.restful_sample.net.ApiClient;
import com.example.restful_sample.net.JsonPlaceholderService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsRepository {

    private final JsonPlaceholderService api;
    private final MutableLiveData<List<Post>> posts = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public PostsRepository() {
        api = ApiClient.get().create(JsonPlaceholderService.class);
    }

    public LiveData<List<Post>> posts() { return posts; }
    public LiveData<String> error() { return error; }

    @MainThread
    public void loadPosts() {
        api.getPosts().enqueue(new Callback<List<Post>>() {
            @Override public void onResponse(Call<List<Post>> call, Response<List<Post>> resp) {
                if (resp.isSuccessful()) posts.postValue(resp.body());
                else error.postValue("Load failed: " + resp.code());
            }
            @Override public void onFailure(Call<List<Post>> call, Throwable t) {
                error.postValue("Network error: " + t.getMessage());
            }
        });
    }

    public void createPost(CreatePostRequest req) {
        api.createPost(req).enqueue(new Callback<Post>() {
            @Override public void onResponse(Call<Post> call, Response<Post> resp) {
                if (!resp.isSuccessful()) {
                    error.postValue("Create failed: " + resp.code());
                    return;
                }
                // naive: prepend created item into list
                List<Post> current = posts.getValue();
                if (current != null) {
                    current.add(0, resp.body());
                    posts.postValue(current);
                }
            }
            @Override public void onFailure(Call<Post> call, Throwable t) {
                error.postValue("Network error: " + t.getMessage());
            }
        });
    }
}
