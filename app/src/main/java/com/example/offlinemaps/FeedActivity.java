package com.example.offlinemaps;

import android.content.Intent;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

public class FeedActivity extends AppCompatActivity {

    /**
     * UI elements
     */
    private RecyclerView feedRecyclerView;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;



    /**
     * Logical Elements
     */
    ArrayList<Feed> feed;
    //User[] followedUsers;

    public static ArrayList<User> followedUsers;
    public static ArrayList<Feed> feedList;
    public static HashMap<String, User> followedHashMap;
    //Firebase fields
    private DatabaseReference mDatabase;
    private DatabaseReference userRef;
    private DatabaseReference selfieRef;
    private StorageReference selfies;
    private FirebaseAuth firebaseAuth;

    private String mCurrentUser;

    //do u love me now
    private static StorageReference imagesRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        feedRecyclerView = findViewById(R.id.feedRecyclerView);

        layoutManager = new LinearLayoutManager(this);


        feedRecyclerView.setLayoutManager(layoutManager);
        feedRecyclerView.setAdapter(adapter);
        mCurrentUser = FirebaseAuth.getInstance().getUid();
        imagesRef = FirebaseStorage.getInstance().getReference().child("Selfies");
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(mCurrentUser);

        followedUsers = new ArrayList<User>();
        feedList = new ArrayList<Feed>();

        adapter = new FeedAdapter(feedList);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User me = dataSnapshot.getValue(User.class);
                    followedHashMap = me.getmFollowedUsers();
                    String[] keys = (String[]) me.getmFollowedUsers().keySet().toArray(new String[0]);
                    for(int i = 0; i < keys.length; i++){
                        String key = keys[i];
                        addToList(me.getmFollowedUsers().get(key));
                    }
                    step2();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FeedActivity.this, "卐 ", Toast.LENGTH_SHORT).show();
            }
        });
        //userRef.child(FirebaseAuth.getInstance().getUid()).child("Selfies").addListenerForSingleValueEvent(new ValueEventListener() {


        //User me = new User(userRef.child(FirebaseAuth.getInstance().getUid()));




    }

    public void step2(){
        Log.d("first", followedUsers.size() + "");
        firebaseAuth = FirebaseAuth.getInstance();

        for(int i = 0; i < followedUsers.size(); i++){


            final String userName = followedUsers.get(i).getmUid();
            Log.d("first", userName + "");
            FirebaseDatabase.getInstance().getReference().child("users").child(userName).child("Selfies").addListenerForSingleValueEvent
                    (new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("first", dataSnapshot + "");
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String username = "bee";
                        Geocoder meeee = new Geocoder(getInstance(), Locale.UK);
                        ///String username = followedUsers.get(i).getmUsername();


                        final Selfie selfie = snapshot.getValue(Selfie.class);

                        final String location;
                        String tempLocation = null;
                        try {
                            tempLocation = meeee.getFromLocation(selfie.getLatitude(), selfie.getLongitude(), 1).get(0).toString();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if(tempLocation != null){
                            location = tempLocation;
                        }else{
                            location = null;
                        }

                        final String date = new Date(Long.parseLong(selfie.getId())).toString();

                        Log.d("first", "Selfie object:" + selfie.toString());
                        imagesRef.child(userName).child(selfie.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String image = uri.toString();
                                Feed feed = new Feed(followedHashMap.get(userName).getmUsername(), date, location, image);
                                feedList.add(feed);
                                Log.d("first", feed.toString() + " AM I EVEN HERE");

                                //Log.d("GalleryActivity", "downloadUri: " + downloadUri);
                                //GalleryItem galleryItem = new GalleryItem(downloadUri, selfie.getId());
                                //Log.d("GalleryActivity", "GalleryItem: " + galleryItem.toString());
                                //galleryItems.add(galleryItem);
                                //Log.d("GalleryActivity", galleryItems.toString());
                                // add images to gallery recyclerview using adapter
                                //mGalleryAdapter.addGalleryItems(galleryItems);
                            }
                        });
                        //Feed feed = new Feed(followedHashMap.get(userName).getmUsername(), date, location, image[0]);
                        //Log.d("first", feed.toString() + " AM I EVEN HERE");
                        //feedList.add(feed);
                    }
                    //mGalleryAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        populateFeedList();
    }


    public FeedActivity getInstance(){
        return this;
    }


    public void populateFeedList(){
        Log.d("first", feedList.size() + "");
      ///feedList.add(new Feed("Shaq","13/05/2019","Guildford",R.drawable.test));
    }

    public static void addToList(User user){
        Log.d("first", user + "");
        User copy = new User(user);
        followedUsers.add(copy);
        Log.d("first", followedUsers.size()+"");
    }

}
