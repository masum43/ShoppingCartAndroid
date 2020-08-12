package com.maces.ecommerce.skcashandcarry.Interfaces;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Get_UserInfor {
    @GET("user")
    Call<JsonObject> postRawJSON();
}
