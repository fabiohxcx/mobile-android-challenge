package com.test.amaro.amarotest.network;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface AmaroService {

    public static final String VERSION = "v2";
    public String API_BASE_URL = "http://www.mocky.io/" + VERSION + "/";


    @GET("59b6a65a0f0000e90471257d/")
    Call<ProductsResponse> allTimeBestSellers();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
