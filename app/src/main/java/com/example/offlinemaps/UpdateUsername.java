package com.example.offlinemaps;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class UpdateUsername extends AppCompatActivity {

    //Final fields
    private static final int RC_SIGN_IN = 1;
    private DatabaseReference userRef;
    //fields for nav view.
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView mNavView;
    //List of login methods.
    private List<AuthUI.IdpConfig> mProviders = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build()
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_username);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Get reference to database.
        String currentUser = FirebaseAuth.getInstance().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser).child("mUsername");

        //Initialise fields.
        final EditText username = findViewById(R.id.et_update_username);
        Button update = findViewById(R.id.bt_update);

        //Update username on button click.
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(UpdateUsername.this, ProfileUI.class);
                startActivity(profile);
                userRef.setValue(username.getText().toString());
                finish();
            }
        });

        mDrawer = findViewById(R.id.drawer_layout);
        mNavView = findViewById(R.id.nav_update);
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
                Intent leaderboard = new Intent(UpdateUsername.this, Leaderboard.class);
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
                Intent profile = new Intent(UpdateUsername.this, ProfileUI.class);
                startActivity(profile);
                finish();
                break;
            case R.id.nav_shop:
                Intent shop = new Intent(UpdateUsername.this, ShopActivity.class);
                startActivity(shop);
                finish();
                break;
        }
        menuItem.setChecked(true);
        mDrawer.closeDrawers();
    }

    /**
     * Open the drawer when the nav icon is clicked.
     * Start search activity.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.search:
                Intent search = new Intent(UpdateUsername.this, FollowersSearch.class);
                startActivity(search);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Inflate the menu for search icon.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        return true;

    }

}
