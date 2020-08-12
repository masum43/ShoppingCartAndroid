package com.maces.ecommerce.skcashandcarry.Interfaces;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Fetch_Categories {
    String URL="https://skcc.luqmansoftwares.com/api/";
    @GET("fetch-categories")
    Call<String> getCategories();
}
