package com.example.offlinemaps;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity {

    private RecyclerView feedRecyclerView;

    private ArrayList<Feed> feedList;
    private RecyclerView.LayoutManager layoutManager;
    private FeedAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        feedRecyclerView = findViewById(R.id.feedRecyclerView);

        layoutManager = new LinearLayoutManager(this);
       // adapter = new ChallengesAdapter(feedList);

        feedRecyclerView.setLayoutManager(layoutManager);
        feedRecyclerView.setAdapter(adapter);

        feedList = new ArrayList<>();


        populateFeedList();

    }



    public void populateFeedList(){

    }



}
