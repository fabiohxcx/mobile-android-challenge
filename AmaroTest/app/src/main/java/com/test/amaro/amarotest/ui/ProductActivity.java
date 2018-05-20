package com.test.amaro.amarotest.ui;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.test.amaro.amarotest.R;
import com.test.amaro.amarotest.model.Product;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductActivity extends AppCompatActivity {

    @BindView(R.id.iv_product_detail)
    ImageView mImageViewProduct;

    @BindView(R.id.tv_name_detail)
    TextView mNameProduct;

    @BindView(R.id.tv_regular_price)
    TextView mRegularPrice;

    @BindView(R.id.tv_actual_price)
    TextView mActualPrice;

    @BindView(R.id.tv_discount_percentage)
    TextView mDiscountPercentage;

    @BindView(R.id.ll_sizes)
    LinearLayout mLinearLayoutSizes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Product product = Parcels.unwrap(getIntent().getParcelableExtra("product"));

        setTitle(product.getName());

        if (!TextUtils.isEmpty(product.getImage())) {
            Picasso.get().load(product.getImage()).placeholder(R.drawable.frame_white).into(mImageViewProduct);
        }

        mNameProduct.setText(product.getName());

        mActualPrice.setText(product.getActualPrice());

        if (product.isOnSale()) {
            mRegularPrice.setText(product.getRegularPrice());
            mRegularPrice.setPaintFlags(mRegularPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            mDiscountPercentage.setText(getString(R.string.discount, product.getDiscountPercentage()));
        }

        for (int i = 0; i < product.getSizes().size(); i++) {

            if (product.getSizes().get(i).isAvailable()) {
                View size = getLayoutInflater().inflate(R.layout.ball_size, (ViewGroup) mLinearLayoutSizes, false);
                ((TextView) size).setText(product.getSizes().get(i).getSize());
                mLinearLayoutSizes.addView(size);
            }

        }


    }


    @Override
    public boolean onSupportNavigateUp() {
        supportFinishAfterTransition();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
