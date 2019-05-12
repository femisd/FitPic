package com.example.offlinemaps;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    public static class FeedViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView usernameFeedText;
        public TextView dateFeedText;
        public TextView locationFeedText;


        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}


