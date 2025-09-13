package com.example.app_poll.classes;

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

public class ClassListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ClassAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_class_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.appRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ClassAdapter(new ArrayList<>(), classId -> {
            // TODO: Navigate to edit fragment for class
        });
        recyclerView.setAdapter(adapter);
        loadClasses();
    }

    private void loadClasses() {
        new Thread(() -> {
            List<ClassItem> classes = new ArrayList<>();
            AppDatabaseHelper helper = new AppDatabaseHelper(requireContext().getApplicationContext());
            SQLiteDatabase db = null;
            Cursor c = null;
            try {
                db = helper.getReadableDatabase();
                c = db.rawQuery("SELECT id, name, section, semester, year FROM classes ORDER BY name ASC", null);
                while (c.moveToNext()) {
                    classes.add(new ClassItem(
                            c.getInt(0),
                            c.getString(1),
                            c.getString(2),
                            c.getString(3),
                            c.getString(4)
                    ));
                }
            } finally {
                if (c != null) c.close();
                if (db != null && db.isOpen()) db.close();
            }
            requireActivity().runOnUiThread(() -> adapter.setClasses(classes));
        }).start();
    }

    static class ClassItem {
        final int id;
        final String name;
        final String section;
        final String semester;
        final String year;
        ClassItem(int id, String name, String section, String semester, String year) {
            this.id = id;
            this.name = name;
            this.section = section;
            this.semester = semester;
            this.year = year;
        }
    }
}
