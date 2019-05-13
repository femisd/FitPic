package com.example.offlinemaps;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FollowersSearch extends AppCompatActivity {
    //Final fields
    private static final int RC_SIGN_IN = 1;
    //fields for nav view.
    private DrawerLayout mDrawer;
    private NavigationView mNavView;
    //List of login methods.
    private List<AuthUI.IdpConfig> mProviders = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build()
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialisation of fields.
        mDrawer = findViewById(R.id.drawer_layout);
        mNavView = findViewById(R.id.nav_search_friends);
        setupDrawerContent(mNavView);
        Button search = findViewById(R.id.bt_search);

        //Give button click functionality.
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateSearchResults();
            }
        });
    }

    /**
     * Populate the listview with results from the firebase based on the
     * users written query. e.g. Mi -> Mike, ke > Mike
     */
    public void populateSearchResults() {
        EditText searchField = findViewById(R.id.et_search_user);
        final ListView displayResults = findViewById(R.id.lv_search_results);
        final ArrayList<User> userResults = new ArrayList<>();
        final FriendAdapterClass friendsAdapter = new FriendAdapterClass(this, userResults);
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        final String query = searchField.getText().toString();

        /*
            Loop through FireBase and return users that contain the search query.
         */
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                friendsAdapter.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user.getmUsername().toLowerCase().contains(query.toLowerCase()) && !query.equals("")) {
                        Log.d("SEARCHABLE", user.getmUsername());
                        friendsAdapter.add(user);
                        friendsAdapter.notifyDataSetChanged();
                    }
                }
                displayResults.setAdapter(friendsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Show profile of friend selected in list.
        displayResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = userResults.get(position);
                Intent viewFriend = new Intent(FollowersSearch.this, ViewFriend.class);
                viewFriend.putExtra("user", user);
                startActivity(viewFriend);
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
                Intent leaderboard = new Intent(FollowersSearch.this, Leaderboard.class);
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
            case R.id.nav_profile:
                Intent profile = new Intent(FollowersSearch.this, ProfileUI.class);
                startActivity(profile);
                finish();
                break;
            case R.id.nav_shop:
                Intent shop = new Intent(FollowersSearch.this, ShopActivity.class);
                startActivity(shop);
                finish();
        }
        menuItem.setChecked(true);
        mDrawer.closeDrawers();
    }

    /**
     * Open the drawer when nav icon is clicked.
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
