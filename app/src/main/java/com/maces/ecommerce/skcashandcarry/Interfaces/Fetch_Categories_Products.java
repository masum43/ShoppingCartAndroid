package com.maces.ecommerce.skcashandcarry.Interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface Fetch_Categories_Products {
@GET
    Call<String> getProducts(@Url String url);
}
