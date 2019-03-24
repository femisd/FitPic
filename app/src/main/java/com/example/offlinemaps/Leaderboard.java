package com.example.offlinemaps;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Leaderboard extends AppCompatActivity {

    private DrawerLayout drawerLayout;

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
                        }
                        return true;
                    }
                });

        ListView leaderboard = findViewById(R.id.lv_leaderboard_list);
        ArrayList<User> userList = new ArrayList<>();
        final LeaderboardAdapterClass leaderboardAdapter = new LeaderboardAdapterClass(this, userList);

        userList.add(new User(R.drawable.common_google_signin_btn_icon_dark, "Vytenis", "Guildford, UK"));
        userList.add(new User(R.drawable.common_google_signin_btn_icon_light, "Rayan", "Guildford, UK"));

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
        super.onBackPressed();
    }
}
