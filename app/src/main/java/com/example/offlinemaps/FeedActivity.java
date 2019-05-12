package com.example.offlinemaps;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class FeedActivity extends AppCompatActivity {

    private RecyclerView feedRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        feedRecyclerView = findViewById(R.id.feedRecyclerView);






    }
}
