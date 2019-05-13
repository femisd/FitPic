package com.example.offlinemaps;

public class Selfie {
    private String id;
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
     * @param latitude
     * @param longitude
     */
    public Selfie(String id, double latitude, double longitude) {
        super();
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Selfie{" +
                "id='" + id + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}

