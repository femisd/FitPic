package com.example.offlinemaps;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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

public class FriendsUI extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private boolean doubleBackToExitPressedOnce = false;
    private static boolean calledAlready;
    private String mCurrentUser;

    //Firebase fields
    private DatabaseReference mDatabase;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_ui);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        //LOOK BACK AT THIS!
//        if (!calledAlready) {
//            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//            calledAlready = true;
//        }

        mCurrentUser = FirebaseAuth.getInstance().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(mCurrentUser);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ListView friends = (ListView) findViewById(R.id.lv_friends_list);
        ArrayList<User> userList = new ArrayList<>();
        final FriendAdapterClass friendsAdapter = new FriendAdapterClass(this, userList);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot userSnapshot : postSnapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        //Don't include users with no username.
                        if (!userSnapshot.child("mUsername").getValue().toString().isEmpty()) {
                            Log.d("USERNAME:", userSnapshot.child("mUsername").getValue().toString());
                            friendsAdapter.add(user);
                        }
                        friendsAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Set the friends list adapter.
        friends.setAdapter(friendsAdapter);

        //Inflate the navigation drawer.
        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nv_friends_list);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);

                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();

                        //Update the UI based on the item selected
                        switch (menuItem.getItemId()) {
                            case R.id.nav_map:
                                //Go to map activity.
                                Intent map = new Intent(FriendsUI.this, MapsActivity.class);
                                startActivity(map);
                                break;
                            case R.id.nav_leaderboard:
                                //Go to leader board activity.
                                Intent leaderboard = new Intent(FriendsUI.this, Leaderboard.class);
                                startActivity(leaderboard);
                                finish();
                                break;
                            case R.id.nav_home:
                                //Go to main activity.
                                break;
                                //Got to profile activity.
                            case R.id.nav_profile:
                                Intent profile = new Intent(FriendsUI.this, ProfileUI.class);
                                startActivity(profile);
                                finish();
                        }
                        return true;
                    }
                });


        /**
        Test feature
         */
//        Button friend = (Button) findViewById(R.id.add_friend);
//        friend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                User user = new User("", "Dan", "Bournemouth, UK", 0, 0, 0, 0,0);
//                userRef.setValue(user);
//            }
//        });
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
                drawerLayout.openDrawer(GravityCompat.START);
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
