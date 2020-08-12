package com.maces.ecommerce.skcashandcarry;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Login_User {
    @Headers("Content-Type:application/json")
    @POST("login")
    Call<JsonObject> postRawJSON(@Body JsonObject locationPost);
}
