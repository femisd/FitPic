package com.example.offlinemaps;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewFriend extends AppCompatActivity {

    private CircleImageView mProfilePicture;

    //fields for nav view.
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView mNavView;

    private DatabaseReference userRef;
    private DatabaseReference followersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavView = (NavigationView) findViewById(R.id.nav_view_friend);
        setupDrawerContent(mNavView);

        String currentUser = FirebaseAuth.getInstance().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser);
        followersRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser).child("mFollowedUsers");

        mProfilePicture = (CircleImageView) findViewById(R.id.cv_view_friends_picture);

        final User user = (User) getIntent().getSerializableExtra("user");
        Log.d("VIEW_FRIEND_REQUEST", user.toString());
        String image = user.getmProfilePicture();
        Log.d("IMAGE", image);
        if (!image.isEmpty()) {
            Picasso.get().load(image).placeholder(R.drawable.ic_person_white_24dp).into(mProfilePicture);
        }
        //Username
        TextView username = (TextView) findViewById(R.id.tv_view_friends_user);
        username.setText(user.getmUsername());

        //Steps
        TextView steps = (TextView) findViewById(R.id.tv_view_friends_steps);
        steps.setText(user.getmSteps() + "");

        //Calories
        TextView calories = (TextView) findViewById(R.id.tv_view_friends_calories);
        calories.setText(user.getmCaloriesBurned() + "");

        //Photos
        TextView photos = (TextView) findViewById(R.id.tv_view_friends_photos);
        photos.setText(user.getmPhotos() + "");

        //Followers
        TextView followers = (TextView) findViewById(R.id.tv_view_friends_followers);
        followers.setText(user.getmFollowers() + "");

        //Following
        TextView following = (TextView) findViewById(R.id.tv_view_friends_following);
        following.setText(user.getmFollowing() + "");

        //Points
        TextView points = (TextView) findViewById(R.id.tv_view_friends_points);
        points.setText(user.getmPoints() + "");

        final Button follow = (Button) findViewById(R.id.bt_follow);

        final ArrayList<String> userUIDs = new ArrayList<>();
        followersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    userUIDs.add(snapshot.getValue(User.class).getmUid());

                    if (snapshot.getValue(User.class).getmUid().equals(user.getmUid())) {
                        follow.setText("Unfollow");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (follow.getText().toString().equals("Follow")) {

                    Toast.makeText(ViewFriend.this, "Following " + user.getmUsername(), Toast.LENGTH_SHORT).show();
                    follow.setText("Unfollow");
                    userRef.child("mFollowedUsers").child(user.getmUid()).setValue(user);
                } else {
                    follow.setText("Follow");
                    userRef.child("mFollowedUsers").child(user.getmUid()).removeValue();
                    Toast.makeText(ViewFriend.this, "Unfollowed " + user.getmUsername(), Toast.LENGTH_SHORT).show();
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
        }
        menuItem.setChecked(true);
        mDrawer.closeDrawers();
    }

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
