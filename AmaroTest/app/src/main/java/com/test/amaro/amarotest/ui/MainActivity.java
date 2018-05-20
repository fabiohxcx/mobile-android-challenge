package com.test.amaro.amarotest.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.test.amaro.amarotest.R;
import com.test.amaro.amarotest.adapters.ProductMainAdapter;
import com.test.amaro.amarotest.listener.HidingScrollListener;
import com.test.amaro.amarotest.model.Product;
import com.test.amaro.amarotest.network.ProductsRepository;
import com.test.amaro.amarotest.network.ProductsResponse;
import com.test.amaro.amarotest.util.ProductsUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mProgressBar;

    @BindView(R.id.bt_retry_main)
    Button mBtRetry;

    @BindString(R.string.str_all_time_best_sellers)
    String mAllTimeBestSellers;

    @BindString(R.string.on_sale)
    String mOnSale;

    @BindString(R.string.by_price)
    String mByPrice;

    @BindString(R.string.retry)
    String mRetry;

    @BindString(R.string.no_internet_connection)
    String mNoInternetConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initToolbar();

        loadProductsData();

        mFabButton.addOnMenuItemClickListener(this);

    }

    //Button clicks
    @OnClick(R.id.bt_retry_main)
    public void retryGetProducts(View view) {
        showRetryButton(false);
        showProgressBar(true);

        loadProductsData();

    }

    // Callback<ProductsResponse>
    @Override
    public void onResponse(Call<ProductsResponse> call, Response<ProductsResponse> response) {
        showProgressBar(false);

        if (response.isSuccessful()) {

            mResponse = response.body();
            mBestSellersList = mResponse.getProductsList();

            if (mBestSellersList != null && !mBestSellersList.isEmpty()) {
                setupRecyclerView(mBestSellersList);
            }

        } else {
            showRetryButton(true);
        }

    }

    @Override
    public void onFailure(Call<ProductsResponse> call, Throwable t) {
        showRetryButton(true);
    }


    private void setupRecyclerView(List<Product> productList) {

        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        mRecyclerView.setLayoutAnimation(animation);

        int columns = 2;

        if (getResources().getConfiguration().orientation == getResources().getConfiguration().ORIENTATION_LANDSCAPE) {
            columns = 4;
        }

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, columns));
        ProductMainAdapter adapter = new ProductMainAdapter(new ArrayList<Product>(mBestSellersList), this);
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

    //Show hide FAB
    private void hideViews() {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) mFabButton.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        mFabButton.animate().translationY(mFabButton.getHeight() + fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void showViews() {
        mFabButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    //Mini FAB actions
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
            if (mBestSellersList != null && !mBestSellersList.isEmpty()) {
                setupRecyclerView(mBestSellersList);
            }

        }

    }

    //Util methods
    private void loadProductsData() {
        mProductsRepository = new ProductsRepository(this);
        mProductsRepository.getBestSellers(this);
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle(mAllTimeBestSellers);
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }

    private void showRetryButton(boolean visible) {
        mBtRetry.setVisibility((visible) ? View.VISIBLE : View.INVISIBLE);
        showSnackbarRetry();
    }

    private void showProgressBar(boolean visible) {
        mProgressBar.setVisibility((visible) ? View.VISIBLE : View.INVISIBLE);
    }

    private void showSnackbarRetry() {
        Snackbar snackbar = Snackbar
                .make(mRecyclerView, mNoInternetConnection, Snackbar.LENGTH_LONG)
                .setAction(mRetry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showRetryButton(false);
                        showProgressBar(true);
                        loadProductsData();
                    }
                });

        snackbar.show();
    }
}
