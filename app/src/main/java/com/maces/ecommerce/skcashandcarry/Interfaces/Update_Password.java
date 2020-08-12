package com.maces.ecommerce.skcashandcarry.Interfaces;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Update_Password {
    @POST("change-password")
    Call<JsonObject> postRawJSON(@Body JsonObject locationPost);
}
