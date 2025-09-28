package com.example.restful_sample.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.restful_sample.databinding.ItemPostBinding;
import com.example.restful_sample.models.Post;
import java.util.ArrayList;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.VH> {

    private final List<Post> data = new ArrayList<>();

    public void submit(List<Post> posts) {
        data.clear();
        if (posts != null) data.addAll(posts);
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        ItemPostBinding b;
        VH(ItemPostBinding b) { super(b.getRoot()); this.b = b; }
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPostBinding b = ItemPostBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new VH(b);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        Post p = data.get(pos);
        h.b.tvTitle.setText(p.title);
        h.b.tvBody.setText(p.body);
    }

    @Override public int getItemCount() { return data.size(); }
}
