package com.test.amaro.amarotest.ui;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.test.amaro.amarotest.R;
import com.test.amaro.amarotest.adapters.ProductMainAdapter;
import com.test.amaro.amarotest.listener.HidingScrollListener;
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

    @BindView(R.id.appbar)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.rv_mainlayout)
    RecyclerView mRecyclerView;

    @BindString(R.string.str_all_time_best_sellers)
    String mAllTimeBestSellers;


    @BindView(R.id.fab)
    FloatingActionButton mFabButton;

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

            mResponse = response.body();
            mBestSellersList = mResponse.getProductsList();

            int resId = R.anim.layout_animation_fall_down;
            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
            mRecyclerView.setLayoutAnimation(animation);

            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            ProductMainAdapter adapter = new ProductMainAdapter(mBestSellersList, this);
            mRecyclerView.setAdapter(adapter);

            //setting up our OnScrollListener
            mRecyclerView.setOnScrollListener(new HidingScrollListener() {
                @Override
                public void onHide() {
                    hideViews();
                }

                @Override
                public void onShow() {
                    showViews();
                }
            });

        } else {
            Toast.makeText(this, "response fail: " + response.raw().cacheResponse(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onFailure(Call<ProductsResponse> call, Throwable t) {
        Toast.makeText(this, "onFailure ", Toast.LENGTH_SHORT).show();
    }


    private void hideViews() {
       // mAppBarLayout.animate().translationY(-mAppBarLayout.getHeight()).setInterpolator(new AccelerateInterpolator(2));

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) mFabButton.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        mFabButton.animate().translationY(mFabButton.getHeight() + fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void showViews() {
       // mAppBarLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        mFabButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }
}
