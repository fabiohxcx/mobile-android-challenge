package com.test.amaro.amarotest.util;

import com.test.amaro.amarotest.model.Product;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class ProductsUtil {

    public static List<Product> getOnSale(List<Product> productList) {

        List<Product> onSaleList = new ArrayList<>();

        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).isOnSale()) {
                onSaleList.add(productList.get(i));
            }
        }

        return onSaleList;
    }

    public static List<Product> getOrderByPrice(List<Product> productList) {

        List<Product> byPriceList = new ArrayList<>(productList);

        Collections.sort(byPriceList, new Comparator<Product>() {
            @Override
            public int compare(Product p1, Product p2) {
                return Double.compare(parseCurrencyPtBR(p1.getActualPrice()), parseCurrencyPtBR(p2.getActualPrice()));
            }
        });

        return byPriceList;
    }

    public static double parseCurrencyPtBR(String price) {

        try {
            Locale ptBR = new Locale("pt", "BR");
            NumberFormat cf = NumberFormat.getCurrencyInstance(ptBR);
            Number number = cf.parse(price);

            return number.doubleValue();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }


}
