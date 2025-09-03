package com.example.fragment_args;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements FormFragment.OnFormSubmitListener {

    private static final String ARG_NAME = "com.example.fragment_args.name";
    private static final String ARG_AGE = "com.example.fragment_args.age";
    @Override
    public void onFormSubmit(String name, String age) {
        // Create a bundle to pass the data
        Bundle bundle = new Bundle();
        bundle.putString(ARG_NAME, name);
        bundle.putString(ARG_AGE, age);

        // Create a new instance of MessageFragment and set the arguments
        MessageFragment messageFragment = new MessageFragment();
        messageFragment.setArguments(bundle);

        // Replace the current fragment with MessageFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, messageFragment)
                .addToBackStack(null)
                .commit();
    }
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
    }
}