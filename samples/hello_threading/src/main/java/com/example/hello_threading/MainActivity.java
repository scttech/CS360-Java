package com.example.hello_threading;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private static final int SLEEP_TIME = 5000;
    private Button btnStartBackground;
    private Button btnStartMainThread;
    private int counter = 0;
    private TextView tvStatus;
    private TextView tvCounter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnIncrement;
        btnStartBackground = findViewById(R.id.btnStartBackgroundThread);
        btnStartMainThread = findViewById(R.id.btnStartMainThread);
        btnIncrement = findViewById(R.id.btnIncrement);

        tvStatus = findViewById(R.id.tvStatus);
        tvCounter = findViewById(R.id.tvCounter);
        progressBar = findViewById(R.id.progressBar);

        tvCounter.setText(getString(R.string.count_text, counter));

        btnStartBackground.setOnClickListener(v -> longRunningBackground());
        btnStartMainThread.setOnClickListener(v -> longRunningMain());
        btnIncrement.setOnClickListener(v -> incrementCounter());
    }

    private void incrementCounter() {
        counter++;
        tvCounter.setText(getString(R.string.count_text, counter));
    }


    private void setButtons(boolean enabled) {
        btnStartBackground.setEnabled(enabled);
        btnStartMainThread.setEnabled(enabled);
    }

    /**
     * Demonstrates a bad version of handling long tasks by running them on the
     * main UI thread.
     * This blocks the UI thread, preventing the progress bar from animating while
     * the task is running
     */
    private void longRunningMain() {
        tvStatus.setText(R.string.starting_long_task_on_main_thread);
        progressBar.setVisibility(View.VISIBLE);
        setButtons(false);

        try {
            Thread.sleep(SLEEP_TIME); // blocks main thread
        } catch (InterruptedException e) {
            Log.d("longRunningMain", "Thread was interrupted", e);
            Thread.currentThread().interrupt();
        }

        tvStatus.setText(R.string.done_no_ui_updates);
        progressBar.setVisibility(View.GONE);
        setButtons(true);
    }


    /**
     * Demonstrates a good version of handling long tasks by running them in a
     * background thread.
     * This keeps the UI responsive, allowing the progress bar to animate while
     * the task is running
     */
    private void longRunningBackground() {
        tvStatus.setText(R.string.working_in_background);
        progressBar.setVisibility(View.VISIBLE);
        setButtons(false);

        new Thread(() -> {
            try {
                Thread.sleep(SLEEP_TIME); // simulate long task
            } catch (InterruptedException e) {
                Log.d("longRunningBackground", "Thread was interrupted", e);
                Thread.currentThread().interrupt();
            }

            runOnUiThread(() -> {
                tvStatus.setText(R.string.done_ui_updates_work);
                progressBar.setVisibility(View.GONE);
                setButtons(true);
            });
        }).start();
    }
}