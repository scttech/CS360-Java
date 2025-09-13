package com.example.app_poll.apps;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_poll.R;
import com.example.app_poll.database.AppDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class AppListFragment extends Fragment {

    private RecyclerView recyclerView;
    private AppAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.appRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new AppAdapter(new ArrayList<>(), appId -> {
            // TODO: Replace with navigation to edit fragment
            // Example: findNavController().navigate(R.id.action_to_editFragment, bundleWithAppId);
        });
        recyclerView.setAdapter(adapter);
        loadApps();
    }

    private void loadApps() {
        new Thread(() -> {
            List<AppItem> apps = new ArrayList<>();
            AppDatabaseHelper helper = new AppDatabaseHelper(requireContext().getApplicationContext());
            SQLiteDatabase db = null;
            Cursor c = null;
            try {
                db = helper.getReadableDatabase();
                c = db.rawQuery("SELECT id, name, description FROM apps ORDER BY name ASC", null);
                while (c.moveToNext()) {
                    apps.add(new AppItem(
                            c.getInt(0),
                            c.getString(1),
                            c.getString(2)
                    ));
                }
            } finally {
                if (c != null) c.close();
                if (db != null && db.isOpen()) db.close();
            }
            requireActivity().runOnUiThread(() -> adapter.setApps(apps));
        }).start();
    }

    static class AppItem {
        final int id;
        final String name;
        final String description;
        AppItem(int id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }
    }
}
