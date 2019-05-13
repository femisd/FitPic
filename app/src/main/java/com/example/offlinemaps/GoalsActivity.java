package com.example.offlinemaps;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GoalsActivity extends AppCompatActivity {


    //Final fields
    private static final int RC_SIGN_IN = 1;
    public int currentPoints;
    private int currentStep;
    private RecyclerView goalsRecyclerView;
    private ChallengesAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button refreshBtn;
    private TextView timerText;
    private CountDownTimer countDownTimer;
    private long timeLeftinMs = 36000000;
    //Firebase fields.
    private DatabaseReference userRef;
    private String mCurrentUser = FirebaseAuth.getInstance().getUid();
    private User currentUser;
    private ArrayList<Challenges> challengesList;
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
        setContentView(R.layout.content_goals);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        currentStep = bundle.getInt("currentSteps");

        timerText = findViewById(R.id.timer_textView);

        refreshBtn = findViewById(R.id.btn_refresh);

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshChallenges();
            }
        });

        mDrawer = findViewById(R.id.drawer_layout);
        mNavView = findViewById(R.id.nav_goals);
        setupDrawerContent(mNavView);

        challengesList = new ArrayList<>();

        goalsRecyclerView = findViewById(R.id.goalsRecyclerView);
        goalsRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new ChallengesAdapter(challengesList);

        goalsRecyclerView.setLayoutManager(layoutManager);
        goalsRecyclerView.setAdapter(adapter);


        populateChallenges();


        startTimer();
        updateTimer();
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
                Intent leaderboard = new Intent(GoalsActivity.this, Leaderboard.class);
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
                Intent profile = new Intent(GoalsActivity.this, ProfileUI.class);
                startActivity(profile);
                finish();
                break;
            case R.id.nav_shop:
                Intent shop = new Intent(GoalsActivity.this, ShopActivity.class);
                startActivity(shop);
                finish();
                break;
        }
        menuItem.setChecked(true);
        mDrawer.closeDrawers();
    }

    public int getCurrentStep() {
        return currentStep;
    }


    public void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftinMs, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftinMs = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {

            }

        }.start();

    }

    public void updateTimer() {

        int seconds = (int) (timeLeftinMs / 1000);
        int minutes = (seconds / 60);
        int hours = (minutes / 60);

        minutes = minutes - hours * 60;

        seconds = seconds - (minutes * 60) - (hours * 60 * 60);

        String timeLeftText;
        timeLeftText = "Next Automatic Refresh is in " + hours;
        timeLeftText += ":";
        if (minutes < 10) timeLeftText += "0";
        timeLeftText += minutes;
        timeLeftText += ":";
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        timerText.setText(timeLeftText);

    }

    public int getCurrentPoints() {
        return currentPoints;
    }


    public void populateChallenges() {


        challengesList.add(new Challenges(getCurrentStep(), "Walk 20 Steps!", 20, "Steps", 20));
        challengesList.add(new Challenges(getCurrentStep(), "Walk 50 Steps!", 50, "Steps", 50));
        challengesList.add(new Challenges(getCurrentStep(), "Walk 100 Steps!", 100, "Steps", 100));
        challengesList.add(new Challenges(0, "Walk 100 Meters!", 10, "Meters", 20));
        challengesList.add(new Challenges(getCurrentStep() / 20, "Burn 10 calories!", 10, "Calories", 20));


    }


    public void refreshChallenges() {
        challengesList.clear();
        adapter.notifyDataSetChanged();


        populateChallenges();
        adapter.notifyDataSetChanged();
        startActivity(getIntent());
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.search:
                Intent search = new Intent(GoalsActivity.this, FollowersSearch.class);
                startActivity(search);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        return true;

    }
}


