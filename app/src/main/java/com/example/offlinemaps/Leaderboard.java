package com.example.offlinemaps;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Leaderboard extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private boolean doubleBackToExitPressedOnce;

    //Firebase fields
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nv_leaderboard);
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
                                Intent map = new Intent(Leaderboard.this, MapsActivity.class);
                                startActivity(map);
                                //finish();
                                break;
                            case R.id.nav_friends:
                                //Go to leader board activity.
                                Intent friends = new Intent(Leaderboard.this, FriendsUI.class);
                                startActivity(friends);
                                finish();
                                break;
                            case R.id.nav_home:
                                //Go to main activity.
                                break;
                            case R.id.nav_profile:
                                Intent profile = new Intent(Leaderboard.this, ProfileUI.class);
                                startActivity(profile);
                                finish();
                        }
                        return true;
                    }
                });

        ListView leaderboard = findViewById(R.id.lv_leaderboard_list);
        ArrayList<User> userList = new ArrayList<>();
        final LeaderboardAdapterClass leaderboardAdapter = new LeaderboardAdapterClass(this, userList);

       userList.add(new User("https://firebasestorage.googleapis.com/v0/b/fitpic-2f0fd.appspot.com/o/Profile%20Pictures%2FlryFgJBnDAhlEECsXwGuF5zMUqO2.jpg?alt=media&token=3b1172ad-27bf-41a9-b8e8-faf10c27f946", "Mike", "Bournemouth, UK",5476,296.7,0,0,0));
       userList.add(new User("https://firebasestorage.googleapis.com/v0/b/fitpic-2f0fd.appspot.com/o/Profile%20Pictures%2FPGeWAHVVV7U9yrEa4MrFc4y7cQQ2.jpg?alt=media&token=b6312e7b-80e3-466c-b099-809ebcdb20d5", "Vytenis", "Guildford, UK",894,40.8,0,0,0));

        leaderboard.setAdapter(leaderboardAdapter);
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
