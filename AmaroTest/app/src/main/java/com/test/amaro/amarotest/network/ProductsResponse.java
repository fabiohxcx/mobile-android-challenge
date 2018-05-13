package com.test.amaro.amarotest.network;

import com.google.gson.annotations.SerializedName;
import com.test.amaro.amarotest.model.Product;

import java.util.List;

public class ProductsResponse {

    @SerializedName("products")
    private List<Product> productsList;

    public List<Product> getProductsList() {
        return productsList;
    }

    public void setProductsList(List<Product> productsList) {
        this.productsList = productsList;
    }
}
