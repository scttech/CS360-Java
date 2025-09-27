package com.example.hello_testing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvStatus;
    private Button btnHello;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStatus = findViewById(R.id.tvStatus);
        btnHello = findViewById(R.id.btnHello);
        btnNext = findViewById(R.id.btnNext);

        btnHello.setOnClickListener(v -> {
            tvStatus.setText("Clicked!");
        });

        btnNext.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, DetailActivity.class);
            i.putExtra("name", "Test Student");
            startActivity(i);
        });
    }
}
