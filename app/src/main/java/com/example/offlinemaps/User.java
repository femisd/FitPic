package com.example.offlinemaps;

public class User {

    //Fields
    private String mUsername;
    private String mProfilePicture;
    private String mLocation;
    private int mFollowers;
    private int mPhotos;
    private int mFollowing;
    private int mSteps;
    private double mCaloriesBurned;

    //Default constructor
    public User() {
    }

    //Constructor used for creating a User.
    public User(String profilePicture, String username, String location, int steps, double caloriesBurned,
                int photos, int followers, int following) {
        mProfilePicture = profilePicture;
        mUsername = username;
        mLocation = location;
        mSteps = steps;
        mCaloriesBurned = caloriesBurned;
        mPhotos = photos;
        mFollowers = followers;
        mFollowing = following;
    }

    //Get the users username.
    public String getmUsername() {
        return mUsername;
    }

    //Get the users profile picture.
    public String getmProfilePicture() {
        return mProfilePicture;
    }

    //Get the users location.
    public String getmLocation() {
        return mLocation;
    }

    //Get the users step count.
    public int getmSteps() {
        return mSteps;
    }

    //Get the users followers.
    public int getmFollowers() {
        return mFollowers;
    }

    //Get the users photo count.
    public int getmPhotos() {
        return mPhotos;
    }

    //Get users following count.
    public int getmFollowing() {
        return mFollowing;
    }

    //Get users calories burned.
    public double getmCaloriesBurned() {
        return mCaloriesBurned;
    }

    //Set the username for the user.
    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    //Set the profile picture for the user.
    public void setmProfilePicture(String mProfilePicture) {
        this.mProfilePicture = mProfilePicture;
    }

    //Set the location for the user.
    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    //Set the the number of followers for users.
    public void setmFollowers(int mFollowers) {
        this.mFollowers = mFollowers;
    }

    //Set photo count of the user.
    public void setmPhotos(int mPhotos) {
        this.mPhotos = mPhotos;
    }

    //Set the following count of the user.
    public void setmFollowing(int mFollowing) {
        this.mFollowing = mFollowing;
    }

    //Set the users steps.
    public void setmSteps(int mSteps) {
        this.mSteps = mSteps;
    }

    //Set the calories burned for the user.
    public void setmCaloriesBurned(double mCaloriesBurned) {
        this.mCaloriesBurned = mCaloriesBurned;
    }



}
