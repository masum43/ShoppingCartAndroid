package com.maces.ecommerce.skcashandcarry.Model;

import com.maces.ecommerce.skcashandcarry.Interfaces.Fetch_Products;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ClientApi {

    private static Retrofit retrofit = null;
    //        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Fetch_Products.URL)
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .build();
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit =new Retrofit.Builder()
                .baseUrl(Fetch_Products.URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        }
        return retrofit;
    }

}
