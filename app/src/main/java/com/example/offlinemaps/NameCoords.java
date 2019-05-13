package com.example.offlinemaps;

import com.google.android.gms.maps.model.LatLng;

/**
 * Conveniently packs up the location data. Not using prebuilt cancer <33
 * <p>
 * Vytenis <3
 */
public class NameCoords {
    String name;
    LatLng coords;
    double dist = 0;
    boolean init = false;
    double poorManGeofenceDist = 100;

    public NameCoords(String name, LatLng coords) {
        this.name = name;
        this.coords = coords;
    }

    public String getName() {
        return this.name;
    }

    public LatLng getCoords() {
        return this.coords;
    }

    public double getDist() {
        return dist;
    }

    public boolean amIInIt() {
        return init;
    }

    public void updateDist(LatLng current) {
        dist = StepCounterActivity.calcDist(coords, current) * 1000;
        init = dist < poorManGeofenceDist;
    }
}
