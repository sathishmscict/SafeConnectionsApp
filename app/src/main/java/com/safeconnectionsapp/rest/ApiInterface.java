package com.safeconnectionsapp.rest;

import com.safeconnectionsapp.Model.UNotificationResponse;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {

    @GET("InsertFCMToken")
    Call<UNotificationResponse> getTopRatedMovies(@Query("type") String type,@Query("fcmtoken") String fcmtoken,@Query("userid") String userid);






}
