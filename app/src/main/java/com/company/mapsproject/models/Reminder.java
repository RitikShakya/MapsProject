package com.company.mapsproject.models;

public class Reminder {
    String id;
    String title;
    String desc;
    String location;
    LatLong latLong;
//    public Reminder(String title, String desc, String location) {
//        this.title = title;
//        this.desc = desc;
//        this.location = location;
//    }


    public Reminder(){

    }


    public Reminder(String id, String title, String desc, String location, LatLong latLong) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.location = location;
        this.latLong = latLong;
    }

//    public Reminder(String id, String title, String desc, String location) {
//        this.id = id;
//        this.title = title;
//        this.desc = desc;
//        this.location = location;
//    }

    public LatLong getLatLong() {
        return latLong;
    }

    public void setLatLng(LatLong latLong) {
        this.latLong = latLong;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}


