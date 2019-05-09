package com.example.offlinemaps;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class StepCounterActivity extends AppCompatActivity implements SensorEventListener {

    private TextView counterView;
    private int steps = 0;
    private Button goalsBtn;
    private SensorManager sensorManager;
    private Sensor stepSensor;
    private Button trackerBtn;

    private FirebaseAuth firebaseAuth;

    private boolean tracking;

    //fields for nav view.
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView mNavView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepcounter);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        trackerBtn = findViewById(R.id.trackerBtn);
        tracking = false;

        updateButton();
        counterView = findViewById(R.id.counterText);
        goalsBtn = findViewById(R.id.goalsBtn);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        counterView.setText(String.valueOf(steps));

        sensorManager.registerListener(this,stepSensor, SensorManager.SENSOR_DELAY_FASTEST);

       goalsBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getApplicationContext(), GoalsActivity.class);
               intent.putExtra("currentSteps",steps);
               startActivity(intent);
           }
       });

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavView = (NavigationView) findViewById(R.id.nv_nav);
        setupDrawerContent(mNavView);



        trackerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!tracking){
                    tracking = true;
                    Toast.makeText(StepCounterActivity.this, "CLick", Toast.LENGTH_SHORT).show();


                }

                else if(tracking){
                    tracking = false;

                    Toast.makeText(StepCounterActivity.this, "clock", Toast.LENGTH_SHORT).show();

                }
                updateButton();
            }
        });


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putBoolean("Tracker", tracking);
        outState.putInt("Steps", steps);
        Toast.makeText(this, "OOO" + steps, Toast.LENGTH_SHORT).show();
        super.onSaveInstanceState(outState);


    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //super.onRestoreInstanceState(savedInstanceState);

        tracking = savedInstanceState.getBoolean("Tracker");
        steps = savedInstanceState.getInt("Steps");
        Toast.makeText(this, "REE" + steps, Toast.LENGTH_SHORT).show();
        updateButton();
        updateSteps();
    }

    public void updateButton(){

        if(tracking){
          //  tracking = true;
            trackerBtn.setText("Stop Tracking");
            trackerBtn.setBackgroundResource(R.drawable.buttonstlye_dead);
        }

        else if(!tracking){
          //  tracking = false;
            trackerBtn.setText("Start Tracking");
            trackerBtn.setBackgroundResource(R.drawable.buttonstyle);
        }

    }

    public void updateSteps(){
        counterView.setText(Integer.toString(steps));

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

      if(tracking) {

          if (event.values[0] == 1.0f) {
              steps++;
          }

      }
        updateSteps();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_map:
                //Go to map activity.
                Intent map = new Intent(StepCounterActivity.this, MapsActivity.class);
                startActivity(map);
                break;
            case R.id.nav_leaderboard:
                //Go to leader board activity.
                Intent leaderboard = new Intent(StepCounterActivity.this, Leaderboard.class);
                startActivity(leaderboard);
                finish();
                break;
            case R.id.nav_logout:
                //Go to main activity.
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                break;
            case R.id.nav_friends:
                Intent friends = new Intent(StepCounterActivity.this, FriendsUI.class);
                startActivity(friends);
                finish();
                break;
            case R.id.nav_profile:
                Intent profile = new Intent(StepCounterActivity.this, ProfileUI.class);
                startActivity(profile);
                finish();
                break;
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
}
