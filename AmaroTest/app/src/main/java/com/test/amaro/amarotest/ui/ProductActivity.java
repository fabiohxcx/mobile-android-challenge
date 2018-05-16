package com.test.amaro.amarotest.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.test.amaro.amarotest.R;
import com.test.amaro.amarotest.model.Product;

import org.parceler.Parcels;

public class ProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Product product = Parcels.unwrap(getIntent().getParcelableExtra("product"));
        setTitle(product.getName());

    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
