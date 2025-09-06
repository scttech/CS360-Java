package com.example.hello_database;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String NEW_USER_NAME = "New User";

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

        //Ensure UserDatabase is created and add sample data
        UserDatabase db = new UserDatabase(this);
        db.addSampleData();

        // Set up button click listeners
        Button btnSelectRecords = findViewById(R.id.btnSelectRecords);
        btnSelectRecords.setOnClickListener(v -> onSelectRecordsClicked(db));

        Button btnAddRecord = findViewById(R.id.btnAddRecord);
        btnAddRecord.setOnClickListener(v -> onAddRecordClicked(db));

        Button btnUpdateRecord = findViewById(R.id.btnUpdateRecord);
        btnUpdateRecord.setOnClickListener(v -> onUpdateRecordClicked(db));

        Button btnDeleteRecord = findViewById(R.id.btnDeleteRecord);
        btnDeleteRecord.setOnClickListener(v -> onDeleteRecordClicked(db));
    }

    /**
     * Deletes a user with a predefined name from the database.
     * If the user does not exist, it prompts to add the user first.
     *
     * @param db The UserDatabase instance to interact with.
     */
    private void onDeleteRecordClicked(UserDatabase db) {
        // Check if user exists
        User existingUser = db.getUserByName(NEW_USER_NAME);

        String message;
        if (existingUser != null) {
            // Delete user if exists
            db.deleteUserByName(NEW_USER_NAME);
            message = NEW_USER_NAME + " deleted.";
        } else {
            message = NEW_USER_NAME + " does not exist. Please add the user first.";
        }

        // Show an alert dialog with the result
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Delete Record")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    /**
     * Updates the age of a user with a predefined name by incrementing it by 1.
     * If the user does not exist, it prompts to add the user first.
     *
     * @param db The UserDatabase instance to interact with.
     */
    private void onUpdateRecordClicked(UserDatabase db) {
        // Check if user exists
        User existingUser = db.getUserByName(NEW_USER_NAME);

        String message;
        if (existingUser != null) {
            // Update age if user exists
            db.updateUserAge(NEW_USER_NAME, existingUser.getAge() + 1);
            message = NEW_USER_NAME + "'s age updated to " + (existingUser.getAge() + 1) + ".";
        } else {
            message = NEW_USER_NAME + " does not exist. Please add the user first.";
        }

        // Show an alert dialog with the result
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Update Record")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    /**
     * Adds a new user record with a predefined name and age of 0.
     * If the user already exists, it does not add a duplicate.
     *
     * @param db The UserDatabase instance to interact with.
     */
    private void onAddRecordClicked(UserDatabase db) {
        // Check if user was already added
        User existingUser = db.getUserByName(NEW_USER_NAME);

        // Only add if not already present
        if (existingUser == null) {
            db.addUser(NEW_USER_NAME, 0);
        }

        // Show an alert dialog with the result
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Add Record")
                .setMessage(existingUser == null ? NEW_USER_NAME + " added." : NEW_USER_NAME + " already exists.")
                .setPositiveButton("OK", null)
                .show();

    }

    /**
     * Displays all user records in an alert dialog.
     *
     * @param db The UserDatabase instance to query.
     */
    private void onSelectRecordsClicked(UserDatabase db) {
        StringBuilder records = new StringBuilder();
        for (User user : db.getAllUsers()) {
            records.append(user.toString()).append("\n").append("-----------------\n");
        }
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("User Records")
                .setMessage(records.toString())
                .setPositiveButton("OK", null)
                .show();
    }
}