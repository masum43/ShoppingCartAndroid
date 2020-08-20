package com.maces.ecommerce.skcashandcarry.Interfaces;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Get_UserInCity {
    @GET("user-city")
    Call<JsonObject> postRawJSON();
}
