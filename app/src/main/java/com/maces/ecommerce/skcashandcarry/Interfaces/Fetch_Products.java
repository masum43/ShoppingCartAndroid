package com.maces.ecommerce.skcashandcarry.Interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Fetch_Products {
    String URL="https://skcc.luqmansoftwares.com/api/";
    @GET("fetch-products")
    Call<String> getProducts(@Query("page") int page);
}
