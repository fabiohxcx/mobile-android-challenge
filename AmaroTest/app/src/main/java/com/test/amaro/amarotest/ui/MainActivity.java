package com.test.amaro.amarotest.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.test.amaro.amarotest.R;
import com.test.amaro.amarotest.adapters.ProductMainAdapter;
import com.test.amaro.amarotest.listener.HidingScrollListener;
import com.test.amaro.amarotest.model.Product;
import com.test.amaro.amarotest.network.ProductsRepository;
import com.test.amaro.amarotest.network.ProductsResponse;
import com.test.amaro.amarotest.util.ProductsUtil;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.kobakei.materialfabspeeddial.FabSpeedDial;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Callback<ProductsResponse>, FabSpeedDial.OnMenuItemClickListener {

    private ProductsRepository mProductsRepository;
    private List<Product> mBestSellersList;
    private ProductsResponse mResponse;

    private Toolbar mToolbar;

    @BindView(R.id.appbar)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.rv_mainlayout)
    RecyclerView mRecyclerView;

    @BindView(R.id.fab)
    FabSpeedDial mFabButton;

    @BindString(R.string.str_all_time_best_sellers)
    String mAllTimeBestSellers;

    @BindString(R.string.on_sale)
    String mOnSale;

    @BindString(R.string.by_price)
    String mByPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initToolbar();

        mProductsRepository = new ProductsRepository(this);
        mProductsRepository.getBestSellers(this);

        mFabButton.addOnMenuItemClickListener(this);

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

            if (mBestSellersList != null && !mBestSellersList.isEmpty()) {
                setupRecyclerView(mBestSellersList);
            }

        } else {
            Toast.makeText(this, "response fail: " + response.raw().cacheResponse(), Toast.LENGTH_SHORT).show();
        }

    }

    private void setupRecyclerView(List<Product> productList) {

        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        mRecyclerView.setLayoutAnimation(animation);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        ProductMainAdapter adapter = new ProductMainAdapter(productList, this);
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
    }

    @Override
    public void onFailure(Call<ProductsResponse> call, Throwable t) {
        Toast.makeText(this, "onFailure ", Toast.LENGTH_SHORT).show();
    }


    private void hideViews() {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) mFabButton.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        mFabButton.animate().translationY(mFabButton.getHeight() + fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void showViews() {
        mFabButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }


    @Override
    public void onMenuItemClick(FloatingActionButton miniFab, @Nullable TextView label, int itemId) {

        if (itemId == R.id.fab_sale) {

            setTitle(mOnSale);
            if (mBestSellersList != null && !mBestSellersList.isEmpty()) {
                List<Product> onSaleProducts = ProductsUtil.getOnSale(mBestSellersList);
                setupRecyclerView(onSaleProducts);
            }

        } else if (itemId == R.id.fab_by_price) {

            setTitle(mByPrice);
            if (mBestSellersList != null && !mBestSellersList.isEmpty()) {
                List<Product> byPriceProducts = ProductsUtil.getOrderByPrice(mBestSellersList);
                setupRecyclerView(byPriceProducts);
            }


        } else if (itemId == R.id.fab_all) {

            setTitle(mAllTimeBestSellers);
            setupRecyclerView(mBestSellersList);

        }

    }
}
