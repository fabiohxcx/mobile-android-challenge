package com.test.amaro.amarotest.network;

import android.content.Context;

import com.test.amaro.amarotest.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ProductsRepository {

    private Context context;

    private List<Product> productList = null;

    public ProductsRepository(Context context) {
        this.context = context;
    }

    public void getBestSellers(Callback<ProductsResponse> callback) {

        final AmaroService service = AmaroService.retrofit.create(AmaroService.class);

        Call<ProductsResponse> requestCatalog = service.allTimeBestSellers();

        requestCatalog.enqueue(callback);

    }

}
