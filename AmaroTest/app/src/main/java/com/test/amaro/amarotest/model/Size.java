package com.test.amaro.amarotest.model;

import org.parceler.Parcel;

@Parcel
public class Size {

    boolean available;
    String size;
    String sku;

    public Size() {
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}
