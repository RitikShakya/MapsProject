package com.company.mapsproject.models;

import com.company.mapsproject.R;

import java.util.ArrayList;
import java.util.Arrays;

public interface ContantData {

    ArrayList<PlaceModel> nearByPlaces = new ArrayList<>(

            Arrays.asList(
                    new PlaceModel(1, R.drawable.ic_baseline_restaurant_24,"Restaurant","restaurant"),
                    new PlaceModel(1, R.drawable.ic_baseline_restaurant_24,"AtM","atm"),
                    new PlaceModel(1, R.drawable.ic_baseline_restaurant_24,"GAS","gas_station"),
                    new PlaceModel(1, R.drawable.ic_baseline_restaurant_24,"Groceries","supermarket"),
                    new PlaceModel(1, R.drawable.ic_baseline_restaurant_24,"Hotels","hotel"),
                    new PlaceModel(1, R.drawable.ic_baseline_restaurant_24,"Pharmacies","pharmacy"),
                    new PlaceModel(1, R.drawable.ic_baseline_restaurant_24,"Car Wash","car_wash"),
                    new PlaceModel(1, R.drawable.ic_baseline_restaurant_24,"Hospitals","hospital"),
                    new PlaceModel(1, R.drawable.ic_baseline_restaurant_24,"BeautySalons","beauty_salon")

            )
    );
}
