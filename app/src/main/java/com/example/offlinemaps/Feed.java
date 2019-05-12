package com.example.offlinemaps;

import android.media.Image;

import java.util.Date;

public class Feed {

    private String username;
    private Date date;
    private String location;
    private Image image;


    public Feed(String username, Date date, String location, Image image) {
        this.username = username;
        this.date = date;
        this.location = location;
        this.image = image;
    }
}
