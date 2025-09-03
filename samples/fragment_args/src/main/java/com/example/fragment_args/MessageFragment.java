package com.example.fragment_args;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageFragment extends Fragment {

    private static final String ARG_NAME = "com.example.fragment_args.name";
    private static final String ARG_AGE = "com.example.fragment_args.age";

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            String name = args.getString(ARG_NAME);
            String age = args.getString(ARG_AGE);
            // Use the name and age as needed
            String message = "Hello, " + name + "! You are " + age + " years old.";
            TextView messageTextView = view.findViewById(R.id.message);
            messageTextView.setText(message);
        }
    }
}