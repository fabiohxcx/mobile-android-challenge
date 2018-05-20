package com.test.amaro.amarotest.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.test.amaro.amarotest.R;
import com.test.amaro.amarotest.model.Product;
import com.test.amaro.amarotest.ui.ProductActivity;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductMainAdapter extends RecyclerView.Adapter<ProductMainAdapter.ProductHolder> {

    private List<Product> productsList;
    private Context context;

    public ProductMainAdapter(List<Product> productsList, Context context) {
        this.productsList = productsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductMainAdapter.ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_product_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {

        if (productsList != null && productsList.size() > 0) {

            Product product = productsList.get(position);

            holder.mName.setText(product.getName());
            holder.mPrice.setText(product.getActualPrice());

            if (!TextUtils.isEmpty(product.getImage())) {
                Picasso.get().load(product.getImage()).placeholder(R.drawable.file_image).into(holder.mImageProduct);
            } else {
                holder.mImageProduct.setImageDrawable(context.getResources().getDrawable(R.drawable.file_image));
            }

            if (product.isOnSale()) {
                holder.mRegularPrice.setText(product.getRegularPrice());
                holder.mRegularPrice.setPaintFlags(holder.mRegularPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.mRegularPrice.setText(""); //fix scrolling bug
            }
        }

    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.frame_product)
        FrameLayout constraintLayoutItem;

        @BindView(R.id.tv_name)
        TextView mName;

        @BindView(R.id.tv_regular_price)
        TextView mRegularPrice;

        @BindView(R.id.tv_actual_price)
        TextView mPrice;

        @BindView(R.id.iv_product)
        ImageView mImageProduct;

        public ProductHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            constraintLayoutItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(context, ProductActivity.class);
            intent.putExtra("product", Parcels.wrap(productsList.get(getAdapterPosition())));
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, mImageProduct, "product_image");
            context.startActivity(intent, options.toBundle());


        }
    }
}
