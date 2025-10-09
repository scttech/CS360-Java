package com.example.using_recycler_view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.VH> {
    private final List<String> data;

    public SimpleAdapter(List<String> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.text.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * ViewHolder class for the adapter, we are using a simple built-in layout
     * "simple_list_item_1" which contains a single TextView with ID "text1".
     */
    public static class VH extends RecyclerView.ViewHolder {
        final TextView text;
        VH(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(android.R.id.text1);
        }
    }
}
