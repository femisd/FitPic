package com.example.offlinemaps;

public class User {

    private String mUsername;
    private long mProfilePicture;
    private String mLocation;
    private int mSteps;
    private double mCaloriesBurned;

    public User() {
    }

    public User(int profilePicture, String username, String location) {
        mProfilePicture = profilePicture;
        mUsername = username;
        mLocation = location;
    }

    public User(int profilePicture, String username, String location, int steps, double caloriesBurned) {
        mProfilePicture = profilePicture;
        mUsername = username;
        mLocation = location;
        mSteps = steps;
        mCaloriesBurned = caloriesBurned;
    }

    public String getmUsername() {
        return mUsername;
    }

    public long getmProfilePicture() {
        return mProfilePicture;
    }

    public String getmLocation() {
        return mLocation;
    }

    public int getmSteps() {
        return mSteps;
    }


    public double getmCaloriesBurned() {
        return mCaloriesBurned;
    }

    public void setmSteps(int mSteps) {
        this.mSteps = mSteps;
    }

    public void setmCaloriesBurned(double mCaloriesBurned) {
        this.mCaloriesBurned = mCaloriesBurned;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public void setmProfilePicture(long mProfilePicture) {
        this.mProfilePicture = mProfilePicture;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }
}
