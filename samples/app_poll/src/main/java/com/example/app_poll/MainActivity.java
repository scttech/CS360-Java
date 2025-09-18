package com.example.app_poll;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.app_poll.apps.AppListFragment;
import com.example.app_poll.classes.ClassListFragment;
import com.example.app_poll.dashboards.Top10AppsFragment;
import com.example.app_poll.database.AppDatabaseHelper;
import com.example.app_poll.forms.AddAppFragment;
import com.example.app_poll.forms.EntryFormsFragment;
import com.github.mikephil.charting.BuildConfig;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private boolean initialNavHandled = false;

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
    private BottomNavigationView.OnItemSelectedListener getBottomNavListener() {
        return item -> {
            int id = item.getItemId();
            if (id == R.id.nav_dashboards) {
                showFragment(new Top10AppsFragment());
                return true;
            } else if (id == R.id.nav_apps_list) {
                showFragment(new AppListFragment());
                return true;
            } else if (id == R.id.nav_classes_list) {
                showFragment(new ClassListFragment());
                return true;
            }
            return false;
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        try {
            AppDatabaseHelper dbHelper = new AppDatabaseHelper(this);
            dbHelper.addSampleData();
        } catch (Exception e) {
            Log.e("MainActivity", "Error initializing database", e);
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(getBottomNavListener());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (!initialNavHandled) {
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
            // Set the default view
            bottomNavigationView.setSelectedItemId(R.id.nav_dashboards);
            initialNavHandled = true;
        }
    }
}