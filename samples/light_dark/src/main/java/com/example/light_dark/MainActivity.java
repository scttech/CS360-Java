package com.example.light_dark;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS = "theme_prefs";
    private static final String KEY_MODE = "night_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply saved mode before view inflation
        AppCompatDelegate.setDefaultNightMode(readSavedMode(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView label = findViewById(R.id.themeLabel);
        ImageView image = findViewById(R.id.themeImage);
        Button toggle = findViewById(R.id.toggleButton);

        updateUiForTheme(label, image);

        toggle.setOnClickListener(v -> {
            boolean isDark = isDarkMode();
            int newMode = isDark ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES;
            saveMode(this, newMode);
            AppCompatDelegate.setDefaultNightMode(newMode); // Triggers Activity recreate
        });
    }

    private static void saveMode(Context ctx, int mode) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        sp.edit().putInt(KEY_MODE, mode).apply();
    }

    private void updateUiForTheme(TextView label, ImageView image) {
        if (isDarkMode()) {
            label.setText("Dark Theme");
            image.setImageResource(R.drawable.theme_dark); // provide this drawable
        } else {
            label.setText("Light Theme");
            image.setImageResource(R.drawable.theme_light); // provide this drawable
        }
    }

    private boolean isDarkMode() {
        int nightMask = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightMask == Configuration.UI_MODE_NIGHT_YES;
    }

    private static int readSavedMode(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        // Default follows system if nothing saved yet
        return sp.getInt(KEY_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }
}
