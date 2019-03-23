package com.example.offlinemaps;

public class User {

    private String mUsername;
    private long mProfilePicture;
    private String mLocation;

    public User() {}

    public User(int profilePicture, String username, String location) {
        mProfilePicture = profilePicture;
        mUsername = username;
        mLocation = location;
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

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public void setmProfilePicture(long mProfilePicture) {
        this.mProfilePicture = mProfilePicture;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    @Override
    public String toString() {
        return "User{" +
                "mUsername='" + mUsername + '\'' +
                ", mProfilePicture=" + mProfilePicture +
                ", mLocation='" + mLocation + '\'' +
                '}';
    }
}
