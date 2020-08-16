package com.maces.ecommerce.skcashandcarry.Interfaces;

import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonArray;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;

public interface Search {
    //String URL="https://skcc.luqmansoftwares.com/api/auth/";
    String URL="https://skcc.luqmansoftwares.com/api/";
    @Headers("Content-Type: application/json")
    @GET("search-product")
    Call<String> ApiName(@HeaderMap Map<String, String> headers);



}
