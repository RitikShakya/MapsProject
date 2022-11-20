package com.company.mapsproject.webservices;

import com.company.mapsproject.models.GooglePlaceModels.GoogleResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitApi {


    @GET
    Call<GoogleResponseModel> getNearByPlaces(@Url String url);
}
