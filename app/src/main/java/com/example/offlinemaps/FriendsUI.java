package com.example.offlinemaps;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class FriendsUI extends AppCompatActivity {

    private ListView listView;
    private  FriendAdapterClass adapterClass;
    private DrawerLayout friendsDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_ui);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);


//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        listView = (ListView) findViewById(R.id.lv_friends_list);
        ArrayList<User> userList = new ArrayList<>();
        //common_google_signin_btn_icon_dark
        userList.add(new User(R.drawable.joy, "Mike" , "Bournemouth, UK"));
        userList.add(new User(R.drawable.common_google_signin_btn_text_dark_normal_background, "Ross" , "London, UK"));
        userList.add(new User(R.drawable.fui_ic_check_circle_black_128dp, "Femi" , "Guildford, UK"));
        userList.add(new User(R.drawable.googleg_standard_color_18, "Kai" , "London, UK"));
        userList.add(new User(R.drawable.common_google_signin_btn_icon_dark, "Vytenis" , "Guildford, UK"));
        userList.add(new User(R.drawable.common_google_signin_btn_icon_light, "Rayan" , "Guildford, UK"));


        adapterClass = new FriendAdapterClass(this, userList);
        listView.setAdapter(adapterClass);

        friendsDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nv_friends_list);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        friendsDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        if (menuItem.getItemId() == R.id.nav_map) {
                            Intent i = new Intent(FriendsUI.this, MapsActivity.class);
                            startActivity(i);
                        }

                        return true;
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                friendsDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
