// File: 'samples/app_poll/src/main/java/com/example/app_poll/forms/AddClassFragment.java'
package com.example.app_poll.forms;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.app_poll.R;
import com.example.app_poll.database.AppDatabaseHelper;

public class AddClassFragment extends Fragment {

    private AppDatabaseHelper dbHelper;
    private EditText nameEditText;
    private EditText sectionEditText;
    private EditText semesterEditText;
    private EditText yearEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_class, container, false);

        dbHelper = new AppDatabaseHelper(requireContext());

        nameEditText = view.findViewById(R.id.editTextClassName);
        sectionEditText = view.findViewById(R.id.editTextClassSection);
        semesterEditText = view.findViewById(R.id.editTextClassSemester);
        yearEditText = view.findViewById(R.id.editTextClassYear);
        Button addButton = view.findViewById(R.id.buttonAddClass);

        addButton.setOnClickListener(v -> addClassEntry());

        return view;
    }

    private void addClassEntry() {
        String name = nameEditText.getText().toString().trim();
        String section = sectionEditText.getText().toString().trim();
        String semester = semesterEditText.getText().toString().trim();
        String year = yearEditText.getText().toString().trim();

        if (name.isEmpty() || section.isEmpty() || semester.isEmpty() || year.isEmpty()) {
            Toast.makeText(requireContext(), "All fields are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        long id = dbHelper.insertClass(name, section, semester, year);
        if (id == -1) {
            Toast.makeText(requireContext(), "Save failed.", Toast.LENGTH_SHORT).show();
            return;
        }

        nameEditText.setText("");
        sectionEditText.setText("");
        semesterEditText.setText("");
        yearEditText.setText("");

        Toast.makeText(requireContext(), "Class saved.", Toast.LENGTH_SHORT).show();
    }
}
