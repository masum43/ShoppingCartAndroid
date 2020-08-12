package com.maces.ecommerce.skcashandcarry.Interfaces;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Search_Products {
    @Headers("Content-Type:application/json")
    @POST("search-product")
    Call<JsonObject> postRawJSON(@Body JsonObject locationPost);
}
