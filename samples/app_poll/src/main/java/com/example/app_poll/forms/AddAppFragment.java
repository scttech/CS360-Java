package com.example.app_poll.forms;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.app_poll.R;
import com.example.app_poll.database.AppDatabaseHelper;

public class AddAppFragment extends Fragment {

    private AppDatabaseHelper dbHelper;

    public void addAppEntry(View view) {
        EditText nameEditText = view.findViewById(R.id.editTextName);
        EditText urlEditText = view.findViewById(R.id.editTextUrl);
        EditText descEditText = view.findViewById(R.id.editTextDescription);

        String name = nameEditText.getText().toString();
        String url = urlEditText.getText().toString();
        String description = descEditText.getText().toString();

        dbHelper.insertApp(name, url, description);

        nameEditText.setText("");
        urlEditText.setText("");
        descEditText.setText("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_app, container, false);

        dbHelper = new AppDatabaseHelper(requireContext());

        Button addButton = view.findViewById(R.id.buttonAddApp);
        addButton.setOnClickListener(this::addAppEntry);

        return view;
    }
}
