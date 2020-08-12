package com.maces.ecommerce.skcashandcarry.Interfaces;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Fetchorders {
    @GET("my-orders")
    Call<JsonObject> postRawJSON();
}
