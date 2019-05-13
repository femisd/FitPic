package com.example.offlinemaps;

import java.io.Serializable;
import java.util.HashMap;

public class User implements Serializable {

    //Fields
    private String mUid;
    private String mUsername;
    private String mProfilePicture;
    private String mLocation;
    private int mFollowers;
    private int mPhotos;
    private int mFollowing;
    private int mSteps;
    private int mPoints;
    private double mCaloriesBurned;
    private boolean mVIP;
    private HashMap<String, User> mFollowedUsers;

    //Default constructor
    public User() {
    }

    public User(User anotherUser) {
        mProfilePicture = anotherUser.getmProfilePicture();
        mUsername = anotherUser.getmUsername();
        mLocation = anotherUser.getmLocation();
        mSteps = anotherUser.getmSteps();
        mCaloriesBurned = anotherUser.getmCaloriesBurned();
        mPhotos = anotherUser.getmPhotos();
        mFollowers = anotherUser.getmFollowers();
        mFollowing = anotherUser.getmFollowing();
        mPoints = anotherUser.getmPoints();
        mVIP = anotherUser.getmVIP();
        mUid = anotherUser.getmUid();
        mFollowedUsers = anotherUser.getmFollowedUsers();
    }

    //Constructor used for creating a User.
    public User(String profilePicture, String uid, String username, String location, int steps, double caloriesBurned,
                int photos, int followers, int following, int points, boolean vip, HashMap<String, User> followedUsers) {
        mProfilePicture = profilePicture;
        mUsername = username;
        mLocation = location;
        mSteps = steps;
        mCaloriesBurned = caloriesBurned;
        mPhotos = photos;
        mFollowers = followers;
        mFollowing = following;
        mPoints = points;
        mVIP = vip;
        mUid = uid;
        mFollowedUsers = followedUsers;
    }

    //Get the users username.
    public String getmUsername() {
        return mUsername;
    }

    //Set the username for the user.
    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    //Get the users profile picture.
    public String getmProfilePicture() {
        return mProfilePicture;
    }

    //Set the profile picture for the user.
    public void setmProfilePicture(String mProfilePicture) {
        this.mProfilePicture = mProfilePicture;
    }

    //Get the users location.
    public String getmLocation() {
        return mLocation;
    }

    //Set the location for the user.
    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    //Get the users step count.
    public int getmSteps() {
        return mSteps;
    }

    //Set the users steps.
    public void setmSteps(int mSteps) {
        this.mSteps = mSteps;
    }

    //Get the users followers.
    public int getmFollowers() {
        return mFollowers;
    }

    //Set the the number of followers for users.
    public void setmFollowers(int mFollowers) {
        this.mFollowers = mFollowers;
    }

    //Get the users photo count.
    public int getmPhotos() {
        return mPhotos;
    }

    //Set photo count of the user.
    public void setmPhotos(int mPhotos) {
        this.mPhotos = mPhotos;
    }

    //Get users following count.
    public int getmFollowing() {
        return mFollowing;
    }

    //Set the following count of the user.
    public void setmFollowing(int mFollowing) {
        this.mFollowing = mFollowing;
    }

    //Get users calories burned.
    public double getmCaloriesBurned() {
        return mCaloriesBurned;
    }

    //Set the calories burned for the user.
    public void setmCaloriesBurned(double mCaloriesBurned) {
        this.mCaloriesBurned = mCaloriesBurned;
    }

    //Get the users points.
    public int getmPoints() {
        return mPoints;
    }

    //Set the points for the user.
    public void setmPoints(int points) {
        this.mPoints = points;
    }

    //Get the users vip_ticket Status.
    public boolean getmVIP() {
        return mVIP;
    }

    //Set the vip_ticket status of the user.
    public void setmVIP(boolean vip) {
        this.mVIP = vip;
    }

    //Get the users UID.
    public String getmUid() {
        return mUid;
    }

    public HashMap<String, User> getmFollowedUsers() {
        return mFollowedUsers;
    }

    @Override
    public String toString() {
        return "User{" +
                "mUid='" + mUid + '\'' +
                ", mUsername='" + mUsername + '\'' +
                ", mProfilePicture='" + mProfilePicture + '\'' +
                ", mLocation='" + mLocation + '\'' +
                ", mFollowers=" + mFollowers +
                ", mPhotos=" + mPhotos +
                ", mFollowing=" + mFollowing +
                ", mSteps=" + mSteps +
                ", mPoints=" + mPoints +
                ", mCaloriesBurned=" + mCaloriesBurned +
                ", mVIP=" + mVIP +
                '}';
    }
}
