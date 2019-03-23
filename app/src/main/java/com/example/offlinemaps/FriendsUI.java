package com.example.offlinemaps;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FriendsUI extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private boolean doubleBackToExitPressedOnce = false;
    private static boolean calledAlready;

    //Firebase fields
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private static final int RC_SIGN_IN = 1;
    private List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build()
    );
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_ui);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        if (!calledAlready) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Allow user to sign in if not already.
        signIn();

        ListView friends = (ListView) findViewById(R.id.lv_friends_list);
        ArrayList<User> userList = new ArrayList<>();
        final FriendAdapterClass friendsAdapter = new FriendAdapterClass(this, userList);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot userSnapshot : postSnapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        friendsAdapter.add(user);
                        Log.d("USER", user.toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Test data for list view
//        userList.add(new User(R.drawable.joy, "Mike", "Bournemouth, UK"));
//        userList.add(new User(R.drawable.common_google_signin_btn_text_dark_normal_background, "Ross", "London, UK"));
//        userList.add(new User(R.drawable.fui_ic_check_circle_black_128dp, "Femi", "Guildford, UK"));
//        userList.add(new User(R.drawable.googleg_standard_color_18, "Kai", "London, UK"));
//        userList.add(new User(R.drawable.common_google_signin_btn_icon_dark, "Vytenis", "Guildford, UK"));
//        userList.add(new User(R.drawable.common_google_signin_btn_icon_light, "Rayan", "Guildford, UK"));

        friends.setAdapter(friendsAdapter);

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
                        }
                        return true;
                    }
                });

    }

    /**
     * Method used to create a sign in page for the user and allow for them to sign themselves in
     * using either their google account or email address.
     */
    private void signIn() {
        //Authentication for user to begin using app
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //user signed in.
                    Toast.makeText(FriendsUI.this, "Signed in!", Toast.LENGTH_SHORT).show();
                } else {
                    //user is signed out.
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);

    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show(); //Successful sign in.
                return;

            } else {
                //permission granted
            }
        } else if (resultCode == RESULT_CANCELED) {
            finish();
            Toast.makeText(this, "Sign in cancelled", Toast.LENGTH_SHORT).show(); //Users exits mid sign in.
        }
        super.onActivityResult(requestCode, resultCode, data);
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
