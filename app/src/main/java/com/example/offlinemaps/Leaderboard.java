package com.example.offlinemaps;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
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
import java.util.Collections;
import java.util.Comparator;

public class Leaderboard extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private boolean doubleBackToExitPressedOnce;

    //fields for nav view.
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView mNavView;

    //Firebase fields
    private DatabaseReference mDatabase;
    private String currentUser;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        //Initialise fields.
        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getUid();

        final ListView leaderboard = findViewById(R.id.lv_leaderboard_list);
        final ArrayList<User> userList = new ArrayList<>();
        final LeaderboardAdapterClass leaderboardAdapter = new LeaderboardAdapterClass(this, userList);

        //Set adapter.
        leaderboard.setAdapter(leaderboardAdapter);

        mDrawer = findViewById(R.id.drawer_layout);
        mNavView = findViewById(R.id.nv_leaderboard);

        setupDrawerContent(mNavView);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                leaderboardAdapter.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot userSnapshot : postSnapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        //Don't include users with no username.
                        if (!userSnapshot.child("mUsername").getValue().toString().isEmpty() && !userList.contains(user)) {
                            leaderboardAdapter.add(user);
                        }
                    }
                }

                //Sort by most points.
                Collections.sort(userList, new Comparator<User>() {
                    @Override
                    public int compare(User o1, User o2) {
                        int comparePoints = o1.getmPoints();
                        return o2.getmPoints() - comparePoints;
                    }
                });

                //leaderboardAdapter.addAll(userList);
                leaderboardAdapter.notifyDataSetChanged();
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
            case R.id.nav_map:
                //Go to map activity.
                Intent map = new Intent(Leaderboard.this, MapsActivity.class);
                startActivity(map);
                break;
            case R.id.nav_friends:
                //Go to leader board activity.
                Intent friends = new Intent(Leaderboard.this, FriendsUI.class);
                startActivity(friends);
                finish();
                break;
            case R.id.nav_logout:
                //Go to main activity.
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                break;
            case R.id.nav_profile:
                Intent profile = new Intent(Leaderboard.this, ProfileUI.class);
                startActivity(profile);
                finish();
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

    /**
     * Prompt user for confirmation before closing the app.
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
