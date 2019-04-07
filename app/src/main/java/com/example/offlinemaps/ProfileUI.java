package com.example.offlinemaps;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
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

    private DrawerLayout mDrawerLayout;
    private boolean mDoubleBackToExitPressedOnce;
    private CircleImageView mProfilePicture;
    private String mCurrentUser;

    //Final fields
    private static final int RC_SIGN_IN = 1;

    //Firebase fields
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private StorageReference userProfilePicturesRef;
    private DatabaseReference userRef;

    private List<AuthUI.IdpConfig> mProviders = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build()
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_ui);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        //Prompt user for login.
        signIn();

        //Initialisation of fields.
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nv_profile);
        mProfilePicture = (CircleImageView) findViewById(R.id.cv_profile_picture);

        //Firebase initialisation fields.
        userProfilePicturesRef = FirebaseStorage.getInstance().getReference().child("Profile Pictures");


        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        //Update the UI based on the item selected
                        switch (menuItem.getItemId()) {
                            case R.id.nav_map:
                                //Go to map activity.
                                Intent map = new Intent(ProfileUI.this, MapsActivity.class);
                                startActivity(map);
                                break;
                            case R.id.nav_leaderboard:
                                //Go to leader board activity.
                                Intent leaderboard = new Intent(ProfileUI.this, Leaderboard.class);
                                startActivity(leaderboard);
                                finish();
                                break;
                            case R.id.nav_home:
                                //Go to main activity.
                                firebaseAuth = FirebaseAuth.getInstance();
                                firebaseAuth.signOut();
                                break;
                            case R.id.nav_friends:
                                Intent profile = new Intent(ProfileUI.this, FriendsUI.class);
                                startActivity(profile);
                                finish();
                        }
                        return true;
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
                    mCurrentUser = FirebaseAuth.getInstance().getUid();
                    userRef = FirebaseDatabase.getInstance().getReference().child("users").child(mCurrentUser);
                    Log.d("TAG", userRef.toString());

                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String image = dataSnapshot.child("mProfilePicture").getValue().toString();
                                Log.d("IMAGE", image);
                                if (!image.isEmpty()) {
                                    Picasso.get().load(image).placeholder(R.drawable.ic_person_white_24dp).into(mProfilePicture);
                                }

                                //User
                                TextView user = (TextView) findViewById(R.id.tv_profile_id);
                                user.setText(dataSnapshot.child("mUsername").getValue().toString());

                                //Username
                                TextView id = (TextView) findViewById(R.id.tv_profile_user);
                                id.setText(dataSnapshot.child("mUsername").getValue().toString());

                                //Steps
                                TextView steps = (TextView) findViewById(R.id.tv_profile_steps);
                                steps.setText(dataSnapshot.child("mSteps").getValue().toString());

                                //Calories
                                TextView calories = (TextView) findViewById(R.id.tv_profile_calories);
                                calories.setText(dataSnapshot.child("mCaloriesBurned").getValue().toString());

                                //Photos
                                TextView photos = (TextView) findViewById(R.id.tv_profile_photos);
                                photos.setText(dataSnapshot.child("mPhotos").getValue().toString());

                                //Followers
                                TextView followers = (TextView) findViewById(R.id.tv_profile_followers);
                                followers.setText(dataSnapshot.child("mFollowers").getValue().toString());

                                //Following
                                TextView following = (TextView) findViewById(R.id.tv_profile_following);
                                following.setText(dataSnapshot.child("mFollowing").getValue().toString());


                            } else {
                                User signedIn = new User("", "", "", 0, 0, 0, 0, 0);
                                userRef.setValue(signedIn);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError dataaseError) {

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

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
                            Log.d("IMAGE", downloadUri + "");
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
