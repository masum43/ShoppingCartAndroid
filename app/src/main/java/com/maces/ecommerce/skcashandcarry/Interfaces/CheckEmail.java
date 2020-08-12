package com.maces.ecommerce.skcashandcarry.Interfaces;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CheckEmail {
    @Headers("Content-Type:application/json")
    @POST("UserPasswordReset")
    Call<JsonObject> postRawJSON(@Body JsonObject locationPost);
}
