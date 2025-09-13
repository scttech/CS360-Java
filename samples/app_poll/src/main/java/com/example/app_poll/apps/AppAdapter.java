package com.example.app_poll.apps;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_poll.R;

import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> {

    public interface OnAppClickListener {
        void onAppClick(int appId);
    }

    private List<AppListFragment.AppItem> apps;
    private final OnAppClickListener listener;

    public AppAdapter(List<AppListFragment.AppItem> apps, OnAppClickListener listener) {
        this.apps = apps;
        this.listener = listener;
    }

    public void setApps(List<AppListFragment.AppItem> newApps) {
        this.apps = newApps;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_app_card, parent, false);
        return new AppViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        AppListFragment.AppItem app = apps.get(position);
        holder.name.setText(app.name);
        holder.description.setText(app.description);
        holder.itemView.setOnClickListener(v -> listener.onAppClick(app.id));
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    static class AppViewHolder extends RecyclerView.ViewHolder {
        TextView name, description;
        AppViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.appName);
            description = itemView.findViewById(R.id.appDescription);
        }
    }
}
