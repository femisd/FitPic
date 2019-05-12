package com.example.offlinemaps;

public class Selfie {
    private String id;
    private String uid;
    private String timestamp;
    private double latitude;
    private double longitude;


    /**
     * Default constructor
     */
    public Selfie() {
        super();
    }

    /**
     * Constructor
     *
     * @param id        selfie id
     * @param uid       user id
     * @param timestamp time taken
     * @param latitude
     * @param longitude
     */
    public Selfie(String id, String uid, String timestamp, double latitude, double longitude) {
        super();
        this.id = id;
        this.uid = uid;
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
