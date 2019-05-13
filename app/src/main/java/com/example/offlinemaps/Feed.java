package com.example.offlinemaps;

public class Feed {

    private String username;
    private String date;
    private String location;
    private String image;


    public Feed(String username, String date, String location, String image) {
        this.username = username;
        this.date = date;
        this.location = location;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String toString(){
        return this.username + " " + this.location + " " + this.date + " " + this.image;
    }
}
