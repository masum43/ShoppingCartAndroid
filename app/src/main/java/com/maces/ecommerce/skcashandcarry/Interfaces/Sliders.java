package com.maces.ecommerce.skcashandcarry.Interfaces;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Sliders {
    String URL="https://skcc.luqmansoftwares.com/api/auth/";
    @Headers("Content-Type: application/json")
    @GET("fetch-offers")
    Call<JsonArray> ApiName(@HeaderMap Map<String, String> headers);



}
