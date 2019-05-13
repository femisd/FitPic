package com.example.offlinemaps;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShopActivity extends AppCompatActivity {

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
        setContentView(R.layout.content_shop);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        //Initialisation of fields.
        mDrawer = findViewById(R.id.drawer_layout);
        mNavView = findViewById(R.id.nav_shop);
        setupDrawerContent(mNavView);

        //Initialise ListView.
        final ListView shopItems = findViewById(R.id.lv_shop_items);
        final ArrayList<ShopItem> itemList = new ArrayList<>();
        final ShopActivtyAdapter shopAdapter = new ShopActivtyAdapter(this, itemList);

        //Shop items.
        ShopItem VIPMembership = new ShopItem("VIP Membership", 100);

        //Set up adapter.
        shopAdapter.add(VIPMembership);
        shopAdapter.notifyDataSetChanged();

        shopItems.setAdapter(shopAdapter);
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
                Intent leaderboard = new Intent(ShopActivity.this, Leaderboard.class);
                startActivity(leaderboard);
                finish();
                break;
            case R.id.nav_logout:
                //Go to main activity.
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                finish();
                //user is signed out.
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setAvailableProviders(mProviders)
                                .build(),
                        RC_SIGN_IN);
                break;
            case R.id.nav_friends:
                Intent friends = new Intent(ShopActivity.this, FriendsUI.class);
                startActivity(friends);
                finish();
                break;

            case R.id.nav_shop:
                Intent shop = new Intent(ShopActivity.this, ShopActivity.class);
                startActivity(shop);
                finish();
                break;

            case R.id.nav_profile:
                Intent profile = new Intent(ShopActivity.this, ProfileUI.class);
                startActivity(profile);
                finish();
                break;
        }
        menuItem.setChecked(true);
        mDrawer.closeDrawers();
    }

    /**
     * Open the drawers when the nav button is clicked.
     * Start the search activity.
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
