package com.example.offlinemaps;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewFriend extends AppCompatActivity {

    //Final fields
    private static final int RC_SIGN_IN = 1;
    private CircleImageView mProfilePicture;
    private User loggedIn;
    //fields for nav view.
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView mNavView;
    //Database references.
    private DatabaseReference userRef;
    private DatabaseReference followersRef;
    private DatabaseReference viewedUserRef;
    //List of login methods.
    private List<AuthUI.IdpConfig> mProviders = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build()
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        //Initialisation of fields.
        mDrawer = findViewById(R.id.drawer_layout);
        mNavView = findViewById(R.id.nav_view_friend);
        mProfilePicture = findViewById(R.id.cv_view_friends_picture);
        setupDrawerContent(mNavView);

        //Setup references.
        final String currentUser = FirebaseAuth.getInstance().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser);

        //Get user object from previous intent.
        final User user = (User) getIntent().getSerializableExtra("user");
        Log.d("VIEW_FRIEND_REQUEST", user.toString());

        followersRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser).child("mFollowedUsers");
        viewedUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(user.getmUid());

        String image = user.getmProfilePicture();
        Log.d("IMAGE", image);
        if (!image.isEmpty()) {
            Picasso.get().load(image).placeholder(R.drawable.ic_person_white_24dp).into(mProfilePicture);
        }
        //Username
        TextView username = findViewById(R.id.tv_view_friends_user);
        username.setText(user.getmUsername());
        setTitle(user.getmUsername());

        //Steps
        TextView steps = findViewById(R.id.tv_view_friends_steps);
        steps.setText(user.getmSteps() + "");

        //Calories
        TextView calories = findViewById(R.id.tv_view_friends_calories);
        calories.setText(user.getmCaloriesBurned() + "");

        //Photos
        TextView photos = findViewById(R.id.tv_view_friends_photos);
        photos.setText(user.getmPhotos() + "");

        //Photos box/button thingy
        LinearLayout photoBox = findViewById(R.id.photosBoxFriend);
        photoBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewFriend.this, GalleryActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        //Followers
        final TextView followers = findViewById(R.id.tv_view_friends_followers);
        followers.setText(user.getmFollowers() + "");

        //Following
        final TextView following = findViewById(R.id.tv_view_friends_following);
        following.setText(user.getmFollowing() + "");

        //Points
        TextView points = findViewById(R.id.tv_view_friends_points);
        points.setText(user.getmPoints() + "");

        final Button follow = findViewById(R.id.bt_follow);

        if (user.getmUid().equals(currentUser)) {
            follow.setVisibility(View.GONE);
        }

        viewedUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final User snapshotValue = dataSnapshot.getValue(User.class);
                followers.setText(snapshotValue.getmFollowers() + "");
                following.setText(snapshotValue.getmFollowing() + "");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*
            Query database to check if queried user exists in
            logged in users followers list and update button text.
         */
        followersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.exists()) {
                        User follower = snapshot.getValue(User.class);

                        if (user.getmUid().equals(follower.getmUid())) {
                            follow.setText("Unfollow");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loggedIn = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*
            Update button texts and add users to current users' followers.
            Increment counters.
        */
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (follow.getText().toString().equals("Follow")) {
                    Toast.makeText(ViewFriend.this, "Following " + user.getmUsername(), Toast.LENGTH_SHORT).show();
                    follow.setText("Unfollow");
                    userRef.child("mFollowedUsers").child(user.getmUid()).setValue(user);
                    userRef.child("mFollowing").setValue(loggedIn.getmFollowing() + 1);
                    viewedUserRef.child("mFollowers").setValue(user.getmFollowers() + 1);
                } else {
                    follow.setText("Follow");
                    userRef.child("mFollowedUsers").child(user.getmUid()).removeValue();
                    Toast.makeText(ViewFriend.this, "Unfollowed " + user.getmUsername(), Toast.LENGTH_SHORT).show();
                    //loggedIn.setmFollowing(loggedIn.getmFollowing() - 1);
                    userRef.child("mFollowing").setValue(loggedIn.getmFollowing() - 1);
                    viewedUserRef.child("mFollowers").setValue(user.getmFollowers());
                }

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
            case R.id.nav_leaderboard:
                //Go to leader board activity.
                Intent leaderboard = new Intent(ViewFriend.this, Leaderboard.class);
                startActivity(leaderboard);
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
            case R.id.nav_friends:
                Intent friends = new Intent(ViewFriend.this, FriendsUI.class);
                startActivity(friends);
                finish();
                break;
            case R.id.nav_profile:
                Intent profile = new Intent(ViewFriend.this, ProfileUI.class);
                startActivity(profile);
                finish();
                break;
            case R.id.nav_shop:
                Intent shop = new Intent(ViewFriend.this, ShopActivity.class);
                startActivity(shop);
                finish();
                break;
        }
        menuItem.setChecked(true);
        mDrawer.closeDrawers();
    }

    /**
     * Open the drawer when home icon is clicked.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
