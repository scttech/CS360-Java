package com.example.app_poll.classes;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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
            try (AppDatabaseHelper helper = new AppDatabaseHelper(requireContext().getApplicationContext())) {
                List<ClassItem> classes = new ArrayList<>(helper.getAllClasses());
                requireActivity().runOnUiThread(() -> adapter.setClasses(classes));
            } catch (Exception e) {
                Log.d("ClassListFragment", "Error loading classes", e);
            }

        }).start();
    }

}
