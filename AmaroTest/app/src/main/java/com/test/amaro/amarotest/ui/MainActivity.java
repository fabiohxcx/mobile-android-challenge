package com.test.amaro.amarotest.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.test.amaro.amarotest.R;
import com.test.amaro.amarotest.model.Product;
import com.test.amaro.amarotest.network.ProductsRepository;
import com.test.amaro.amarotest.network.ProductsResponse;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Callback<ProductsResponse> {

    ProductsRepository mProductsRepository;
    List<Product> mBestSellersList;
    ProductsResponse mResponse;

    private Toolbar mToolbar;

    @BindView(R.id.test)
    TextView tvTest;

    @BindString(R.string.str_all_time_best_sellers)
    String mAllTimeBestSellers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initToolbar();

        mProductsRepository = new ProductsRepository(this);
        mProductsRepository.getBestSellers(this);

    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle(mAllTimeBestSellers);
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }

    @Override
    public void onResponse(Call<ProductsResponse> call, Response<ProductsResponse> response) {

        if (response.isSuccessful()) {

            tvTest.setText(new Gson().toJson(response.body()));

        } else {
            Toast.makeText(this, "response fail: " + response.raw().cacheResponse(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onFailure(Call<ProductsResponse> call, Throwable t) {
        Toast.makeText(this, "onFailure ", Toast.LENGTH_SHORT).show();

        tvTest.setText(t.getMessage());
    }
}
