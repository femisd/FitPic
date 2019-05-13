package com.example.offlinemaps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Remember to implement  GalleryAdapter.GalleryAdapterCallBacks to activity for communication of Activity and Gallery Adapter
public class GalleryActivity extends AppCompatActivity implements GalleryAdapter.GalleryAdapterCallBacks {

    private static final int RC_SIGN_IN = 1;

    //Firebase storage folder where you want to put the images
    private static DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");
    private static StorageReference imagesRef = FirebaseStorage.getInstance().getReference().child("Selfies");

    //Deceleration of list of  GalleryItems
    public List<GalleryItem> galleryItems = new ArrayList<>();
    GalleryAdapter mGalleryAdapter;
    private ProgressDialog mProgress;

    //List of login methods.
    private List<AuthUI.IdpConfig> mProviders = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build()
    );

    //fields for nav view.
    private DrawerLayout mDrawer;
    private androidx.appcompat.widget.Toolbar toolbar;
    private NavigationView mNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        //setup RecyclerView
        RecyclerView recyclerViewGallery = findViewById(R.id.recyclerViewGallery);
        recyclerViewGallery.setLayoutManager(new GridLayoutManager(this, 2));
        //Create RecyclerView Adapter
        mGalleryAdapter = new GalleryAdapter(this);
        //set adapter to RecyclerView
        recyclerViewGallery.setAdapter(mGalleryAdapter);

        // set up the progress dialog
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Fetching images...");
        mProgress.show();

        //Inflate the navigation drawer.
        mDrawer = findViewById(R.id.drawer_layout);
        mNavView = findViewById(R.id.nav_gallery);

        setupDrawerContent(mNavView);

        final User user = (User) getIntent().getExtras().get("user");

        Log.d("GalleryActivity", "User gallery for: " + user.toString());

        //Get images
        userRef.child(user.getmUid()).child("Selfies").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
                            mProgress.dismiss();
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

    /**
     * Setup the navigation drawer.
     *
     * @param navigationView
     */
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });
    }

    /**
     * Select an item from menu and perform an action.
     *
     * @param menuItem
     */
    public void selectDrawerItem(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_friends:
                //Go to leader board activity.
                Intent friends = new Intent(GalleryActivity.this, FriendsUI.class);
                startActivity(friends);
                finish();
                break;
            case R.id.nav_logout:
                //Go to main activity.
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                finish();
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setAvailableProviders(mProviders)
                                .build(),
                        RC_SIGN_IN);
                break;
            case R.id.nav_profile:
                Intent profile = new Intent(GalleryActivity.this, ProfileUI.class);
                startActivity(profile);
                finish();
                break;
            case R.id.nav_shop:
                Intent shop = new Intent(GalleryActivity.this, ShopActivity.class);
                startActivity(shop);
                finish();
                break;
            case R.id.nav_feed:
                Intent feed = new Intent(GalleryActivity.this, FeedActivity.class);
                startActivity(feed);
                finish();
                break;
            case R.id.nav_leaderboard:
                Intent leaderboard =  new Intent(GalleryActivity.this, Leaderboard.class);
                startActivity(leaderboard);
                finish();
        }
        menuItem.setChecked(true);
        mDrawer.closeDrawers();
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