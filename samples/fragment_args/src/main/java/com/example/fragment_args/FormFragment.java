package com.example.fragment_args;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 */
public class FormFragment extends Fragment {

    public FormFragment() {
        // Required empty public constructor
    }

    public interface OnFormSubmitListener {
        void onFormSubmit(String name, String age);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_form, container, false);
        EditText nameEditText = view.findViewById(R.id.name);
        EditText ageEditText = view.findViewById(R.id.age);
        Button sendButton = view.findViewById(R.id.send);

        sendButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String age = ageEditText.getText().toString();
            if( getActivity() instanceof OnFormSubmitListener) {
                ((OnFormSubmitListener) getActivity()).onFormSubmit(name, age);
            }
        });

        return view;
    }
}