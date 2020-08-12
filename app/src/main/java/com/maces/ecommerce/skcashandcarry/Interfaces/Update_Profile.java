package com.maces.ecommerce.skcashandcarry.Interfaces;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Update_Profile {
    @POST("update-profile")
    Call<JsonObject> postRawJSON(@Body JsonObject locationPost);
}
