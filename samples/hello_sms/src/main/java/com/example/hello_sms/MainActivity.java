package com.example.hello_sms;

import android.Manifest;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // Replace with a valid phone number for testing
    private static final String PHONE_NUMBER = "+11238675309";
    // Launcher for requesting SMS permission
    private ActivityResultLauncher<String> requestPermissionLauncher;

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

        // Initialize the permission launcher
        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    // Handle the permission result
                    if (Boolean.TRUE.equals(isGranted)) {
                        sendTextMessage();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission to send SMS not granted", Toast.LENGTH_LONG).show();
                    }
                });

        Button btnSendSms = findViewById(R.id.btnSendSms);
        btnSendSms.setOnClickListener(v -> checkForSmsPermission());
    }

    /**
     * Check for SMS permission and request if not granted.
     */
    private void checkForSmsPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS)
                == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            sendTextMessage();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission Required")
                    .setMessage("This app needs permission to send SMS messages.")
                    .setPositiveButton("OK", (dialogInterface, which) -> {
                        requestPermissionLauncher.launch(Manifest.permission.SEND_SMS);
                    })
                    .setNegativeButton("Cancel", (dialogInterface, which) -> dialogInterface.dismiss())
                    .show();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.SEND_SMS);
        }
    }

    /**
     * Send a text message to the specified phone number.
     */
    private void sendTextMessage() {
        try {
            SmsManager smsManager = getSystemService(SmsManager.class);
            smsManager.sendTextMessage(PHONE_NUMBER, null, "Hello from Android!", null, null);
            Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("MainActivity", "SMS failed to send", e);
            Toast.makeText(getApplicationContext(), "Message Failed to Send", Toast.LENGTH_LONG).show();
        }
    }
}