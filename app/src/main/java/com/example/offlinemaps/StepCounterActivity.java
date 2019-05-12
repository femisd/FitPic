package com.example.offlinemaps;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class StepCounterActivity extends AppCompatActivity implements SensorEventListener, OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final String TAG = "MapActivity";
    private float DEFAULT_ZOOM = 922337203685807f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168), new LatLng(71, 136));
    private static final int CAMERA_REQUEST_CODE = 1;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;
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

    ArrayList<NameCoords> nearbyMarkers;

    // private LocationCallback;
    private Toolbar toolbar;

    //widgets
    private NavigationView mNavView;
    private ImageButton selfieBtn;
    private GoogleMap mMap;
    private Button locationstart;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapater;
    private GoogleApiClient mGoogleApiClient;

    //Current Location
    private Location currentLocation;
    private LatLng currentLatLng;

    /**
     * CalcDist between two pars of lat-lon
     */

    boolean isModified = false;


    //method which move camera to our current location when the map is initially loaded or from returning from another activity
    public void loc(LatLng ln){

        if(!isModified) {
            this.isModified = true;
            moveCamera(ln,15f);



        }




    }

    public void updateMarkers(ArrayList<NameCoords> locations){
        for(int i = 0; i < locations.size(); i++){
            LatLng coordinates = locations.get(i).getCoords();
            double dist = calcDist(currentLatLng, coordinates);
            mMap.addMarker(new MarkerOptions()
                    .position(coordinates)
                    .title(locations.get(i).getName())
                    .snippet(dist*1000+"m")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.money_pointer)));
        }
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {

            List<Location> locationList = locationResult.getLocations();

            currentLocation = locationList.get(locationList.size() - 1);
            currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

            loc(currentLatLng);





            if (locationList.size() > 0) {
                //The last location in the list is the newest
                currentLocation = locationList.get(locationList.size() - 1);
                currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                Log.i("MapsActivity", "Current Location: " + currentLocation.getLatitude() + " " + currentLocation.getLongitude());
                mLastLocation = currentLocation;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }






                //Place current location marker
                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.running_pointer));
                mCurrLocationMarker = mMap.addMarker(markerOptions);

                //move map camera
                // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                updateMarkers(nearbyMarkers);
            }
        }
    };
    private TextView locationText;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private Marker myMarker;
    private Marker myMarker1;
    private Marker myMarker2;
    private Marker myMarker3;

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

        selfieBtn = findViewById(R.id.selfieBtn);

        updateButton();
        counterView = findViewById(R.id.counterText);
        goalsBtn = findViewById(R.id.goalsBtn);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        counterView.setText(String.valueOf(steps));

        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST);

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

        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid());

        trackerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selfieBtn.setVisibility(View.VISIBLE);


                if (!tracking) {
                    tracking = true;
                    //    Toast.makeText(StepCounterActivity.this, "CLick", Toast.LENGTH_SHORT).show();



                }else if (tracking) {
                    tracking = false;
                    goalsBtn.setEnabled(true);
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

        selfieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent, CAMERA_REQUEST_CODE);

            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        init();

        mStorage = FirebaseStorage.getInstance().getReference();

        mProgress = new ProgressDialog(this);

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
            trackerBtn.setText("Stop Tracking");
            trackerBtn.setBackgroundResource(R.drawable.buttonstlye_dead);
        } else if (!tracking) {
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
        setUpMap();
    }

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

        /**
         * Temporary List
         */
        nearbyMarkers = new ArrayList<NameCoords>();

        nearbyMarkers.add(new NameCoords("Pats Field", new LatLng(51.243271, -0.591590)));
        nearbyMarkers.add(new NameCoords("Friary Centre", new LatLng(51.242373, -0.581312)));
        nearbyMarkers.add(new NameCoords("Student Union", new LatLng(51.242007, -0.586198)));
        nearbyMarkers.add(new NameCoords("School Of Arts", new LatLng(51.243012, -0.595327)));
    }


    @Override
    public boolean onMarkerClick(Marker marker) {


        if (marker.equals(myMarker)) {
            //handle click here

            //  Toast.makeText(StepCounterActivity.this, "YOIIIIIIIIIIIIIIIIIIIIIIIIIIINKS", Toast.LENGTH_SHORT).show();
            // Toast.makeText(StepCounterActivity.this, ""+myMarker.getPosition()+" " , Toast.LENGTH_SHORT).show();

            locationText.setText("Pats field");
        }
        if (marker.equals(myMarker1)) {
            //handle click here

            // Toast.makeText(StepCounterActivity.this, "afjaknfask", Toast.LENGTH_SHORT).show();
            // Toast.makeText(StepCounterActivity.this, ""+myMarker1.getPosition()+" " , Toast.LENGTH_SHORT).show();
            locationText.setText("Friary Centre");

        }

        if (marker.equals(myMarker2)) {
            //handle click here

            // Toast.makeText(StepCounterActivity.this, "afjaknfask", Toast.LENGTH_SHORT).show();
            // Toast.makeText(StepCounterActivity.this, ""+myMarker2.getPosition()+" " , Toast.LENGTH_SHORT).show();
            locationText.setText("Student Union");

        }
        if (marker.equals(myMarker3)) {
            //handle click here

            //  Toast.makeText(StepCounterActivity.this, "afjaknfask", Toast.LENGTH_SHORT).show();
            // Toast.makeText(StepCounterActivity.this, ""+myMarker3.getPosition()+" " , Toast.LENGTH_SHORT).show();
            locationText.setText("School Of Arts");

        }
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

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            //set the progress dialog
            mProgress.setMessage("Uploding image...");
            mProgress.show();

            //get the camera image
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] databaos = baos.toByteArray();

            //set the image into imageview
            //mImageView.setImageBitmap(bitmap);
            //String img = "fire"

            //Firebase storage folder where you want to put the images
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();

            //name of the image file (add time to have different files to avoid rewrite on the same file)
            StorageReference imagesRef = storageRef.child("Selfies").child(FirebaseAuth.getInstance().getUid()).child(String.valueOf(new Date().getTime()));
            //send this name to database
            //upload image
            UploadTask uploadTask = imagesRef.putBytes(databaos);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(StepCounterActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //productDownloadUrl = taskSnapshot.getDownloadUrl().toString();
                    mProgress.dismiss();
                    Toast.makeText(StepCounterActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                }

            });
        }
    }


}
