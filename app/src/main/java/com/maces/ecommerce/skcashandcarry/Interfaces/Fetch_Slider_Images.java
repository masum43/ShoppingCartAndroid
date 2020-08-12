package com.maces.ecommerce.skcashandcarry.Interfaces;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Fetch_Slider_Images {
    String URL="https://skcc.luqmansoftwares.com/api/";
    @GET("fetch-slider-pics")
    Call<String> getSlider_Images();
}
