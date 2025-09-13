package com.example.app_poll.classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_poll.R;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {

    public interface OnClassClickListener {
        void onClassClick(int classId);
    }

    private List<ClassListFragment.ClassItem> classes;
    private final OnClassClickListener listener;

    public ClassAdapter(List<ClassListFragment.ClassItem> classes, OnClassClickListener listener) {
        this.classes = classes;
        this.listener = listener;
    }

    public void setClasses(List<ClassListFragment.ClassItem> newClasses) {
        this.classes = newClasses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_class_card, parent, false);
        return new ClassViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        ClassListFragment.ClassItem cls = classes.get(position);
        holder.name.setText(cls.name);
        holder.section.setText(cls.section);
        holder.semester.setText(cls.semester + " " + cls.year);
        holder.itemView.setOnClickListener(v -> listener.onClassClick(cls.id));
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    static class ClassViewHolder extends RecyclerView.ViewHolder {
        TextView name, section, semester;
        ClassViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.className);
            section = itemView.findViewById(R.id.section);
            semester = itemView.findViewById(R.id.semester);
        }
    }
}
