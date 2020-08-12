package com.maces.ecommerce.skcashandcarry.Interfaces;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Submit_Your_Order {
    @POST("submit-order")
    Call<JSONObject> postRawJSON(@Body JSONObject locationPost);
}
