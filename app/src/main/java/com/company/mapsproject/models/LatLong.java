package com.company.mapsproject.models;

public class LatLong {


    Double longitude;
    Double latitude;
    String saveTitle;
    public LatLong() {
    }


    public LatLong(Double latitude, Double longitude, String saveTitle) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.saveTitle = saveTitle;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getSaveTitle() {
        return saveTitle;
    }

    public void setSaveTitle(String saveTitle) {
        this.saveTitle = saveTitle;
    }
}
