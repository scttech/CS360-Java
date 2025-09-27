package com.example.hello_testing;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView tvDetail = findViewById(R.id.tvDetail);
        String name = getIntent().getStringExtra("name");
        tvDetail.setText("Welcome, " + (name == null ? "Guest" : name));
    }
}
