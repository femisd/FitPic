package com.example.offlinemaps;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class StepCounterActivity extends AppCompatActivity implements SensorEventListener, OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener {

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


    private GoogleMap mMap;
    private SupportMapFragment mapFrag;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private FusedLocationProviderClient mFusedLocationClient;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    // private LocationCallback;

    private Button locationstart;

    //widgets
    private AutoCompleteTextView mSearchText;

    private static final String TAG = "MapActivity";

    private static final float DEFAULT_ZOOM = 15f;

    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapater;

    private GoogleApiClient mGoogleApiClient;

    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168), new LatLng(71, 136));

    //Curren Location

    private Location currentLocation;
    private LatLng currentLatLng;

    private TextView locationText;

    //Firebase fields
    private DatabaseReference userRef;
    private String mCurrentUser = FirebaseAuth.getInstance().getUid();

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

        locationText = findViewById(R.id.locationText);

        updateButton();
        counterView = findViewById(R.id.counterText);
        goalsBtn = findViewById(R.id.goalsBtn);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        counterView.setText(String.valueOf(steps));

        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST);

        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(mCurrentUser);

        goalsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GoalsActivity.class);
                intent.putExtra("currentSteps", steps);
                startActivity(intent);
            }
        });

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavView = (NavigationView) findViewById(R.id.nv_nav);
        setupDrawerContent(mNavView);


        trackerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!tracking) {
                    tracking = true;
                    //    Toast.makeText(StepCounterActivity.this, "CLick", Toast.LENGTH_SHORT).show();


                } else if (tracking) {
                    tracking = false;
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User currentUser = dataSnapshot.getValue(User.class); //Get current user object.
                            int totalSteps = currentUser.getmSteps() + steps; //Calculate total steps.
                            userRef.child("mSteps").setValue(totalSteps);

                            double currentCalories = currentUser.getmCaloriesBurned();
                            //Assume 0.04 calories to 1 step.
                            double caloriesBurned = (steps * 0.04) + currentCalories;
                            userRef.child("mCaloriesBurned").setValue(caloriesBurned);
                            steps = 0;
                            counterView.setText("0"); //Reset counter.
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                updateButton();
            }
        });

        mSearchText = (AutoCompleteTextView) findViewById(R.id.input_search);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        init();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putBoolean("Tracker", tracking);
        outState.putInt("Steps", steps);
        // Toast.makeText(this, "OOO" + steps, Toast.LENGTH_SHORT).show();
        super.onSaveInstanceState(outState);


    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //super.onRestoreInstanceState(savedInstanceState);

        tracking = savedInstanceState.getBoolean("Tracker");
        steps = savedInstanceState.getInt("Steps");
        //   Toast.makeText(this, "REE" + steps, Toast.LENGTH_SHORT).show();
        updateButton();
        updateSteps();
    }

    public void updateButton() {

        if (tracking) {
            //  tracking = true;
            trackerBtn.setText("Stop Tracking");
            trackerBtn.setBackgroundResource(R.drawable.buttonstlye_dead);
        } else if (!tracking) {
            //  tracking = false;
            trackerBtn.setText("Start Tracking");
            trackerBtn.setBackgroundResource(R.drawable.buttonstyle);
        }

    }

    public void updateSteps() {
        counterView.setText(Integer.toString(steps));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (tracking) {

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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);


        setUpMap();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1200); // two minute interval
        mLocationRequest.setFastestInterval(1200);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mMap.setMyLocationEnabled(true);
        }
    }

    private Marker myMarker;
    private Marker myMarker1;
    private Marker myMarker2;
    private Marker myMarker3;


    private void setUpMap() {

        List<LatLng> location = new ArrayList<>();
        List<String> locationName = new ArrayList<>();

        location.add(new LatLng(51.5074, 0.1278));
        location.add(new LatLng(51.243271, -0.591590));
        location.add(new LatLng(51.242373, -0.581312));
        location.add(new LatLng(51.242007, -0.586198));
        location.add(new LatLng(51.243012, -0.595327));

        mMap.setOnMarkerClickListener(this);

        locationName.add("London");
        locationName.add("Pats field");
        locationName.add("Friary Centre");
        locationName.add("Student Union");
        //locationName.add();


        myMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(51.243271, -0.591590))
                .title("Pats field")
                .snippet("112m")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));


        myMarker1 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(51.242373, -0.581312))
                .title("Friary Centre")
                .snippet("743m")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        myMarker2 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(51.242007, -0.586198))
                .title("Student Union")
                .snippet("123m")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        myMarker3 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(51.243012, -0.595327))
                .title("School Of Arts")
                .snippet("846m")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));


    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        locationText.setText(marker.getTitle());
        Toast.makeText(StepCounterActivity.this, "" + marker.getPosition() + " ", Toast.LENGTH_SHORT).show();


        return false;
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //stop location updates when Activity is no longer active
        //Toast.makeText(MapsActivity.this, "reeeeeeeeeeeeeeee", Toast.LENGTH_SHORT).show();

        if (mFusedLocationClient != null) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void init() {
        Log.d(TAG, "init: initializing");

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        mPlaceAutocompleteAdapater = new PlaceAutocompleteAdapter(this, mGoogleApiClient, LAT_LNG_BOUNDS, null);

        mSearchText.setAdapter(mPlaceAutocompleteAdapater);

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {

                    //execute our method for searching
                    geoLocate();
                }

                return false;
            }
        });
    }


    public double toRadians(double deg) {
        return deg * Math.PI / 180;
    }

    public double calcDist(LatLng latLng1, LatLng latLng2) {

        double lat1 = latLng1.latitude;
        double lon1 = latLng1.longitude;
        double lat2 = latLng2.latitude;
        double lon2 = latLng2.longitude;
        double dist = 0;
        double lat1R = toRadians(lat1);
        double lon1R = toRadians(lon1);
        double lat2R = toRadians(lat2);
        double lon2R = toRadians(lon2);

        double a = Math.pow(Math.sin((lat2R - lat1R) / 2), 2) + Math.cos(lat1R) * Math.cos(lat2R) * Math.pow(Math.sin((lon2R - lon1R) / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double R = 6371.0;
        dist = R * c;
        return dist;
    }

    private void geoLocate() {
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(StepCounterActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);
            LatLng addressLatLng = new LatLng(address.getLatitude(), address.getLongitude());
            moveCamera(addressLatLng, DEFAULT_ZOOM, address.getAddressLine(0));
            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, "Dist to " + address.toString() + " is " + calcDist(addressLatLng, currentLatLng), Toast.LENGTH_SHORT).show();

        }

    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(title);
        mMap.addMarker(options);
    }

    /**
     * CalcDist between two pars of lat-lon
     */


    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {

            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                currentLocation = locationList.get(locationList.size() - 1);
                currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                Log.i("MapsActivity", "Current Location: " + currentLocation.getLatitude() + " " + currentLocation.getLongitude());
                // Toast.makeText(StepCounterActivity.this, ""+currentLocation.getLatitude()+" " + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                mLastLocation = currentLocation;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                //Place current location marker
                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mCurrLocationMarker = mMap.addMarker(markerOptions);

                //move map camera
                //  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
            }
        }
    };


    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(StepCounterActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    //Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}
