package com.example.offlinemaps;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
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

import java.util.ArrayList;
import java.util.List;

//Remember to implement  GalleryAdapter.GalleryAdapterCallBacks to activity for communication of Activity and Gallery Adapter
public class GalleryActivity extends AppCompatActivity implements GalleryAdapter.GalleryAdapterCallBacks {
    //Deceleration of list of  GalleryItems
    public List<GalleryItem> galleryItems = new ArrayList<>();

    GalleryAdapter mGalleryAdapter;

    //Firebase storage folder where you want to put the images
    private static DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");

    private static StorageReference imagesRef = FirebaseStorage.getInstance().getReference().child("Selfies");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        //setup RecyclerView
        RecyclerView recyclerViewGallery = (RecyclerView) findViewById(R.id.recyclerViewGallery);
        recyclerViewGallery.setLayoutManager(new GridLayoutManager(this, 2));
        //Create RecyclerView Adapter
        mGalleryAdapter = new GalleryAdapter(this);
        //set adapter to RecyclerView
        recyclerViewGallery.setAdapter(mGalleryAdapter);

        final User user = (User) getIntent().getExtras().get("user");

        Log.d("GalleryActivity", "User gallery for: " + user.toString());

        //Get images
        userRef.child(user.getmUid()).child("Selfies").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Selfie selfie = snapshot.getValue(Selfie.class);
                    Log.d("GalleryActivity", "Selfie object:" + selfie.toString());
                    imagesRef.child(user.getmUid()).child(selfie.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUri = uri.toString();
                            Log.d("GalleryActivity", "downloadUri: " + downloadUri);
                            GalleryItem galleryItem = new GalleryItem(downloadUri, selfie.getId());
                            Log.d("GalleryActivity", "GalleryItem: " + galleryItem.toString());
                            galleryItems.add(galleryItem);
                            Log.d("GalleryActivity", galleryItems.toString());
                            // add images to gallery recyclerview using adapter
                            mGalleryAdapter.addGalleryItems(galleryItems);
                        }
                    });
                }
                mGalleryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onItemSelected(int position) {
        //create fullscreen SlideShowFragment dialog
        SlideShowFragment slideShowFragment = SlideShowFragment.newInstance(position);
        //setUp style for slide show fragment
        slideShowFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
        //finally show dialogue
        slideShowFragment.show(getSupportFragmentManager(), null);
    }

}