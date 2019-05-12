package com.example.offlinemaps;

import com.google.android.gms.maps.model.LatLng;

/**
 *
 * Conveniently packs up the location data. Not using prebuilt cancer <33
 *
 * Vytenis <3
 */
public class NameCoords {
    String name;
    LatLng coords;

    public NameCoords(String name, LatLng coords){
        this.name = name;
        this.coords = coords;
    }

    public String getName(){
        return this.name;
    }
    public LatLng getCoords(){
        return this.coords;
    }
}
