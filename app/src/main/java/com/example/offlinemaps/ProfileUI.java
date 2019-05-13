package com.example.offlinemaps;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileUI extends AppCompatActivity {

    //Final fields
    private static final int RC_SIGN_IN = 1;
    private static boolean calledAlready;
    //Test button for step counter!
    private Button startWalkingBtn;
    private boolean mDoubleBackToExitPressedOnce;
    private CircleImageView mProfilePicture;
    private String mCurrentUser;
    //Firebase fields
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private StorageReference userProfilePicturesRef;
    private DatabaseReference userRef;


    //List of login methods.
    private List<AuthUI.IdpConfig> mProviders = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build()
    );

    //fields for nav view.
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView mNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_ui);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        //Enable offline capabilities.
        if (!calledAlready) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }

        //Prompt user for login.
        signIn();

        //Initialisation of fields.
        mDrawer = findViewById(R.id.drawer_layout);
        mNavView = findViewById(R.id.nv_profile);
        setupDrawerContent(mNavView);
        mProfilePicture = findViewById(R.id.cv_profile_picture);

        //Firebase initialisation fields.
        userProfilePicturesRef = FirebaseStorage.getInstance().getReference().child("Profile Pictures");

        //Test button for step counter
        startWalkingBtn = findViewById(R.id.btn_startWalking);
        startWalkingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentStepCounter = new Intent(getApplicationContext(), StepCounterActivity.class);
                startActivity(intentStepCounter);
            }
        });

        /*
            Allows user to select profile picture from an image in their gallery.
         */
        mProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(ProfileUI.this);
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
                Intent leaderboard = new Intent(ProfileUI.this, Leaderboard.class);
                startActivity(leaderboard);
                finish();
                break;
            case R.id.nav_logout:
                //Go to main activity.
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                finish();
                break;
            case R.id.nav_friends:
                Intent friends = new Intent(ProfileUI.this, FriendsUI.class);
                startActivity(friends);
                finish();
                break;
            case R.id.nav_feed:
                Intent feed = new Intent(ProfileUI.this, FeedActivity.class);
                startActivity(feed);
                finish();
                break;
            case R.id.nav_shop:
                Intent shop = new Intent(ProfileUI.this, ShopActivity.class);
                startActivity(shop);
                finish();
                break;

        }
        menuItem.setChecked(true);
        mDrawer.closeDrawers();
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
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //user signed in.
                    mCurrentUser = FirebaseAuth.getInstance().getUid();
                    userRef = FirebaseDatabase.getInstance().getReference().child("users").child(mCurrentUser);
                    Log.d("TAG", userRef.toString());

                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                final User user = dataSnapshot.getValue(User.class);
                                String image = user.getmProfilePicture();
                                if (!image.isEmpty()) {
                                    Picasso.get().load(image).placeholder(R.drawable.ic_user_placeholder).into(mProfilePicture);
                                }
                                //Username
                                TextView username = findViewById(R.id.tv_profile_user);
                                if (user.getmUsername().isEmpty()) {
                                    Intent intent = new Intent(ProfileUI.this, UpdateUsername.class);
                                    startActivity(intent);
                                } else {
                                    username.setText(user.getmUsername());
                                }

                                LinearLayout usernameBox = findViewById(R.id.ll_user_box);
                                //Launch update username activity.
                                username.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent update = new Intent(ProfileUI.this, UpdateUsername.class);
                                        startActivity(update);
                                    }
                                });

                                //Steps
                                TextView steps = findViewById(R.id.tv_profile_steps);
                                steps.setText(user.getmSteps() + "");

                                //Calories
                                TextView calories = findViewById(R.id.tv_profile_calories);
                                calories.setText(user.getmCaloriesBurned() + "");

                                //Photos
                                TextView photos = findViewById(R.id.tv_profile_photos);
                                photos.setText(user.getmPhotos() + "");

                                //Photos box/button thingy
                                LinearLayout photoBox = findViewById(R.id.photosBox);
                                photoBox.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(ProfileUI.this, GalleryActivity.class);
                                        intent.putExtra("user", user);
                                        startActivity(intent);
                                    }
                                });

                                //Followers
                                TextView followers = findViewById(R.id.tv_profile_followers);
                                followers.setText(user.getmFollowers() + "");

                                //Following
                                TextView following = findViewById(R.id.tv_profile_following);
                                following.setText(user.getmFollowing() + "");

                                LinearLayout followingBox = findViewById(R.id.ll_following_box);
                                followingBox.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent following = new Intent(ProfileUI.this, FriendsUI.class);
                                        startActivity(following);
                                    }
                                });

                                //Points
                                TextView points = findViewById(R.id.tv_profile_points);
                                points.setText(user.getmPoints() + "");

                            } else {
                                User signedIn = new User("", mCurrentUser, "", "Guildford, UK", 0, 0, 0, 0, 0, 0, false, null);
                                userRef.setValue(signedIn);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    //Toast.makeText(FriendsUI.this, "Signed in!", Toast.LENGTH_SHORT).show();
                } else {
                    //user is signed out.
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(mProviders)
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
                Intent search = new Intent(ProfileUI.this, FollowersSearch.class);
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

    /**
     * Give user confirmation before closing app.
     */
    @Override
    public void onBackPressed() {
        if (mDoubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.mDoubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mDoubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    /**
     * Intent results.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Log.d("SIGN_IN", "Success");
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show(); //Successful sign in.
                return;

            } else {
                //permission granted
            }
        } else if (resultCode == RESULT_CANCELED) {
            //Put back to profile activity.
            Intent profile = new Intent(ProfileUI.this, ProfileUI.class);
            startActivity(profile);
            finish();
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri cropped = result.getUri();

                final StorageReference filePath = userProfilePicturesRef.child(mCurrentUser + ".jpg");

                filePath.putFile(cropped).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            userRef.child("mProfilePicture").setValue(downloadUri.toString());
                        } else {
                            // Handle failures
                        }
                    }
                });
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
