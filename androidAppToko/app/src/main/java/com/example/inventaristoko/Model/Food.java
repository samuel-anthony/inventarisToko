package com.example.inventaristoko.Model;

import java.math.BigDecimal;

public class Food {
    private int mId;
    private String mName;
    private String mCategory;
    private BigDecimal mPrice;
    private String mImageUrl;

    public Food(int mId, String mName, String mCategory, BigDecimal mPrice, String mImageUrl) {
        this.mId = mId;
        this.mName = mName;
        this.mCategory = mCategory;
        this.mPrice = mPrice;
        this.mImageUrl = mImageUrl;
    }

    public int getmId() {
        return mId;
    }

    public String getmName() {
        return mName;
    }

    public String getmCategory() {
        return mCategory;
    }

    public BigDecimal getmPrice() {
        return mPrice;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }
}
