package com.example.offlinemaps;

public class User {

    private String mUsername;
    private int mProfilePicture;
    private String mLocation;

    public User(int profilePicture, String username, String location) {
        mProfilePicture = profilePicture;
        mUsername = username;
        mLocation = location;
    }

    public String getmUsername() {
        return mUsername;
    }

    public int getmProfilePicture() {
        return mProfilePicture;
    }

    public String getmLocation() {
        return mLocation;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public void setmProfilePicture(int mProfilePicture) {
        this.mProfilePicture = mProfilePicture;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }
}
