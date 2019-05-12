package com.example.offlinemaps;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class FeedActivity extends AppCompatActivity {

    /**
     * UI elements
     */
    private RecyclerView feedRecyclerView;


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

        feedRecyclerView = findViewById(R.id.feedRecyclerView);

        feed = new ArrayList<Feed>();
        populateFeed();


    }


    private void populateFeed(){
        mCurrentUser = FirebaseAuth.getInstance().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(mCurrentUser).child("mFollowedUsers");
        mDatabase = FirebaseDatabase.getInstance().getReference();



        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        User fren = user.getValue(User.class);
                        String frenuid = fren.getmUid();
                        selfies = FirebaseStorage.getInstance().getReference().child("selfies").child(frenuid);
                        Task selfieUrl = selfies.getDownloadUrl();
                        Object result = selfieUrl.getResult();
                        Toast.makeText(FeedActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    Log.d("USERS", user.toString());
                    User friends = user.getValue(User.class);
                    if (!friends.getmUid().equals(mCurrentUser)) {
                        //friendsAdapter.add(user.getValue(User.class));
                    }
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
