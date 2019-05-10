package com.example.offlinemaps;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewFriend extends AppCompatActivity {

    private CircleImageView mProfilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProfilePicture = (CircleImageView) findViewById(R.id.cv_view_friends_picture);

        User user = (User) getIntent().getSerializableExtra("user");
        Log.d("VIEW_FRIEND_REQUEST", user.toString());
        String image = user.getmProfilePicture();
        Log.d("IMAGE", image);
        if (!image.isEmpty()) {
            Picasso.get().load(image).placeholder(R.drawable.ic_person_white_24dp).into(mProfilePicture);
        }
        //Username
        TextView username = (TextView) findViewById(R.id.tv_view_friends_user);
        username.setText(user.getmUsername());

        //Steps
        TextView steps = (TextView) findViewById(R.id.tv_view_friends_steps);
        steps.setText(user.getmSteps() + "");

        //Calories
        TextView calories = (TextView) findViewById(R.id.tv_view_friends_calories);
        calories.setText(user.getmCaloriesBurned() + "");

        //Photos
        TextView photos = (TextView) findViewById(R.id.tv_view_friends_photos);
        photos.setText(user.getmPhotos() + "");

        //Followers
        TextView followers = (TextView) findViewById(R.id.tv_view_friends_followers);
        followers.setText(user.getmFollowers() + "");

        //Following
        TextView following = (TextView) findViewById(R.id.tv_view_friends_following);
        following.setText(user.getmFollowing() + "");

        //Points
        TextView points = (TextView) findViewById(R.id.tv_view_friends_points);
        points.setText(user.getmPoints() + "");
    }

}
