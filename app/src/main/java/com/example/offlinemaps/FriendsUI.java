package com.example.offlinemaps;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

import java.util.ArrayList;

public class FriendsUI extends AppCompatActivity {

    //Fields.
    private boolean doubleBackToExitPressedOnce = false;
    private String mCurrentUser;

    //fields for nav view.
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView mNavView;

    //Firebase fields
    private DatabaseReference mDatabase;
    private DatabaseReference userRef;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_ui);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        //Firebase initialisation.
        mCurrentUser = FirebaseAuth.getInstance().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(mCurrentUser).child("mFollowedUsers");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ListView friends = (ListView) findViewById(R.id.lv_friends_list);
        final ArrayList<User> userList = new ArrayList<>();
        final FriendAdapterClass friendsAdapter = new FriendAdapterClass(this, userList);

//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                friendsAdapter.clear();
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    for (DataSnapshot userSnapshot : postSnapshot.getChildren()) {
//                        User user = userSnapshot.getValue(User.class);
//                        //Don't include users with no username.
//                        if (!userSnapshot.child("mUsername").getValue().toString().isEmpty()) {
//                            Log.d("USERNAME:", user.toString() + "");
//                            friendsAdapter.add(user);
//                        }
//                        friendsAdapter.notifyDataSetChanged();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    Log.d("USERS", user.toString());
                    friendsAdapter.add(user.getValue(User.class));
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Set the friends list adapter.
        friends.setAdapter(friendsAdapter);

        friends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = userList.get(position);
                Log.d("YOOO", user.toString());
                Intent i = new Intent(FriendsUI.this, ViewFriend.class);
                i.putExtra("user", user);
                startActivity(i);
            }
        });
        //Inflate the navigation drawer.
        mDrawer = findViewById(R.id.drawer_layout);

        mNavView = findViewById(R.id.nv_friends_list);
        setupDrawerContent(mNavView);
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
                Intent leaderboard = new Intent(FriendsUI.this, Leaderboard.class);
                startActivity(leaderboard);
                finish();
                break;
            case R.id.nav_logout:
                //Go to main activity.
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                finish();
                break;
            case R.id.nav_profile:
                Intent profile = new Intent(FriendsUI.this, ProfileUI.class);
                startActivity(profile);
                finish();
                break;
        }
        menuItem.setChecked(true);
        mDrawer.closeDrawers();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
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

    /*
     * Method used to invoke the user whether they would
     * like to quit the app with a confirmation before doing so.
     */

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
