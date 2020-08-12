package com.maces.ecommerce.skcashandcarry.Interfaces;

import com.maces.ecommerce.skcashandcarry.Model.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {
    @GET("fetch-products")
    Call<List<Movie>> getProducts();


//    String URL="https://skcc.luqmansoftwares.com/api/";
//    @GET("fetch-products")
//    Call<String> getProducts(@Query("page") int page);
}
