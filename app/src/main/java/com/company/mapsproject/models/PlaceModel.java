package com.company.mapsproject.models;

public class PlaceModel {
    int id , drawerId;

    String name;
    String placetype;

    public PlaceModel() {
    }

    public PlaceModel(int id, int drawerId, String name, String placetype) {
        this.id = id;
        this.drawerId = drawerId;
        this.name = name;
        this.placetype = placetype;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDrawerId() {
        return drawerId;
    }

    public void setDrawerId(int drawerId) {
        this.drawerId = drawerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlacetype() {
        return placetype;
    }

    public void setPlacetype(String placetype) {
        this.placetype = placetype;
    }
}
