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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

public class FeedActivity extends AppCompatActivity {

    /**
     * UI elements
     */
    private RecyclerView feedRecyclerView;
    private final ArrayList<Feed> feedList = new ArrayList<Feed>();
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

    //do u love me now
    private static StorageReference imagesRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        feedRecyclerView = findViewById(R.id.feedRecyclerView);

        layoutManager = new LinearLayoutManager(this);
        adapter = new FeedAdapter(feedList);

        feedRecyclerView.setLayoutManager(layoutManager);
        feedRecyclerView.setAdapter(adapter);

        imagesRef = FirebaseStorage.getInstance().getReference().child("Selfies");
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(mCurrentUser);

        final ArrayList<User> followedUsers = new ArrayList<User>();


        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User me = dataSnapshot.getValue(User.class);
                    String[] keys = (String[]) me.getmFollowedUsers().keySet().toArray();
                    for(int i = 0; i < keys.length; i++){
                        String key = keys[i];
                        followedUsers.add(me.getmFollowedUsers().get(key));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FeedActivity.this, "卐 ", Toast.LENGTH_SHORT).show();
            }
        });
        //userRef.child(FirebaseAuth.getInstance().getUid()).child("Selfies").addListenerForSingleValueEvent(new ValueEventListener() {

        firebaseAuth = FirebaseAuth.getInstance();

        //User me = new User(userRef.child(FirebaseAuth.getInstance().getUid()));
        for(int i = 0; i < followedUsers.size(); i++){
            final String username = followedUsers.get(i).getmUid();
            userRef.child(username).child("Selfies").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Geocoder meeee = new Geocoder(getInstance(), Locale.UK);
                        ///String username = followedUsers.get(i).getmUsername();
                        String date;

                        final String[] image = new String[1];
                        final Selfie selfie = snapshot.getValue(Selfie.class);
                        String location = null;
                        try {
                            location = meeee.getFromLocation(selfie.getLatitude(), selfie.getLongitude(), 1).get(0).toString();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        date = (new java.util.Date((long)Integer.parseInt(selfie.getId())*1000)).toString();

                        Log.d("GalleryActivity", "Selfie object:" + selfie.toString());
                        imagesRef.child(FirebaseAuth.getInstance().getUid()).child(selfie.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                image[0] = uri.toString();
                                //Log.d("GalleryActivity", "downloadUri: " + downloadUri);
                                //GalleryItem galleryItem = new GalleryItem(downloadUri, selfie.getId());
                                //Log.d("GalleryActivity", "GalleryItem: " + galleryItem.toString());
                                //galleryItems.add(galleryItem);
                                //Log.d("GalleryActivity", galleryItems.toString());
                                // add images to gallery recyclerview using adapter
                                //mGalleryAdapter.addGalleryItems(galleryItems);
                            }
                        });
                        Feed feed = new Feed(username, date, location, image[0]);
                        feedList.add(feed);
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

      ///feedList.add(new Feed("Shaq","13/05/2019","Guildford",R.drawable.test));
    }


}
