package com.example.restful_sample;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.restful_sample.databinding.ActivityMainBinding;
import com.example.restful_sample.ui.PostsAdapter;
import com.example.restful_sample.ui.PostsViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding b;
    private PostsViewModel vm;
    private PostsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        adapter = new PostsAdapter();
        b.recycler.setLayoutManager(new LinearLayoutManager(this));
        b.recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        b.recycler.setAdapter(adapter);

        vm = new ViewModelProvider(this).get(PostsViewModel.class);
        vm.posts().observe(this, posts -> adapter.submit(posts));
        vm.error().observe(this, msg -> {
            if (msg != null) Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        });

        b.btnCreate.setOnClickListener(v -> vm.createSample());

        vm.load();
    }
}
