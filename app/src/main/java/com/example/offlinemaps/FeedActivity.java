package com.example.offlinemaps;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity {

    /**
     * UI elements
     */
    private RecyclerView feedRecyclerView;
    private ArrayList<Feed> feedList;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;



    /**
     * Logical Elements
     */
    ArrayList<Feed> feed;
    User[] followedUsers;

    //Firebase fields
    private DatabaseReference mDatabase;
    private DatabaseReference userRef;
    private DatabaseReference selfieRef;
    private StorageReference selfies;
    private FirebaseAuth firebaseAuth;

    private String mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        feedList = new ArrayList<>();
        populateFeedList();

        feedRecyclerView = findViewById(R.id.feedRecyclerView);

        layoutManager = new LinearLayoutManager(this);
        adapter = new FeedAdapter(feedList);

        feedRecyclerView.setLayoutManager(layoutManager);
        feedRecyclerView.setAdapter(adapter);






    }





    public void populateFeedList(){

      feedList.add(new Feed("Shaq","13/05/2019","Guildford",R.drawable.test));
    }


}
