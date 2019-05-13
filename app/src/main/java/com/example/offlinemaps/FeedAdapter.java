package com.example.offlinemaps;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    private ArrayList<Feed> mFeedList;

    public static class FeedViewHolder extends RecyclerView.ViewHolder{
            public ImageView imageView;
            public TextView usernameText;
            public TextView locationText;
            public TextView dateText;
        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.feedImage);
            usernameText = itemView.findViewById(R.id.usernameFeedText);
            locationText = itemView.findViewById(R.id.locationFeedText);
            dateText = itemView.findViewById(R.id.dateFeedText);


        }
    }

    public FeedAdapter(ArrayList<Feed> feedList){

        mFeedList = feedList;

    }




    public void addFeedItem(ArrayList<Feed> mFeedList){
        int previousSize  = mFeedList.size();
        this.mFeedList.clear();
        this.mFeedList.addAll(mFeedList);
        notifyItemRangeChanged(previousSize,mFeedList.size());

    }

    public void abrakadabra(User userToAdd){
        int previousSize  = mFeedList.size();
    }


    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_element,parent,false);
        FeedViewHolder feedViewHolder = new FeedViewHolder(v);
        return feedViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {

        Feed currentFeed = mFeedList.get(position);

        Picasso.get().load(currentFeed.getImage()).into(holder.imageView);
        holder.usernameText.setText(currentFeed.getUsername());
        holder.dateText.setText(currentFeed.getDate());
        holder.locationText.setText(currentFeed.getLocation());

    }

    @Override
    public int getItemCount() {
        return mFeedList.size();
    }
}


