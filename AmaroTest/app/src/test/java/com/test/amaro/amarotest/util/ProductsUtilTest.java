package com.test.amaro.amarotest.util;

import com.test.amaro.amarotest.model.Product;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ProductsUtilTest {

    List<Product> products = new ArrayList<>();

    @Before
    public void setUp() {

        Product product1 = new Product();
        product1.setName("Prod1");
        product1.setOnSale(true);
        product1.setActualPrice("R$ 199,90");

        products.add(product1);

        Product product2 = new Product();
        product2.setName("Prod2");
        product2.setOnSale(false);
        product2.setActualPrice("R$ 129,90");

        products.add(product2);
    }

    @Test
    public void getOnSale() {

        List<Product> productsSale = ProductsUtil.getOnSale(products);

        for (int i = 0; i < productsSale.size(); i++) {
            Assert.assertTrue("Product: " + productsSale.get(i).getName() + " is on sale: " + productsSale.get(0).isOnSale(), productsSale.get(0).isOnSale());
        }

    }

    @Test
    public void getOrderByPrice() {

        List<Product> productsOrdered = ProductsUtil.getOrderByPrice(products);

        for (int i = 0; i < productsOrdered.size() - 1; i++) {

            double price = ProductsUtil.parseCurrencyPtBR(productsOrdered.get(i).getActualPrice());
            double price2 = ProductsUtil.parseCurrencyPtBR(productsOrdered.get(i + 1).getActualPrice());

            Assert.assertTrue("prod: " + productsOrdered.get(i).getName() + " - prod: " + productsOrdered.get(i + 1).getName(), price < price2);

        }

    }

}