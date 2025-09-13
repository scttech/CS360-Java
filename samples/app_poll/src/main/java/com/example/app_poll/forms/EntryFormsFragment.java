package com.example.app_poll.forms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.app_poll.R;

public class EntryFormsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_entry_forms, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Button toAddApp = view.findViewById(R.id.buttonGoAddApp);
        Button toAddClass = view.findViewById(R.id.buttonGoAddClass);

        toAddApp.setOnClickListener(v ->
                navigateTo(new AddAppFragment(), "AddApp"));

        toAddClass.setOnClickListener(v ->
                navigateTo(new AddClassFragment(), "AddClass"));
    }

    private void navigateTo(Fragment destination, String backStackTag) {
        View root = requireView();
        ViewParent parent = root.getParent();
        int containerId = (parent instanceof ViewGroup) ? ((ViewGroup) parent).getId() : View.NO_ID;

        if (containerId == View.NO_ID) {
            // Fallback if parent has no ID; replace the activity content.
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(android.R.id.content, destination)
                    .addToBackStack(backStackTag)
                    .commit();
            return;
        }

        getParentFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(containerId, destination)
                .addToBackStack(backStackTag)
                .commit();
    }
}
