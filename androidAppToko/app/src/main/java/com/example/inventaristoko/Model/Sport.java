package com.example.inventaristoko.Model;

import com.google.gson.annotations.SerializedName;

public class Sport {

    @SerializedName("imageUrl")
    private String mImageUrl;
    @SerializedName("info")
    private String mInfo;
    @SerializedName("subTitle")
    private String mSubTitle;
    @SerializedName("title")
    private String mTitle;

    public Sport(String mImageUrl, String mInfo, String mSubTitle, String mTitle) {
        this.mImageUrl = mImageUrl;
        this.mInfo = mInfo;
        this.mSubTitle = mSubTitle;
        this.mTitle = mTitle;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getInfo() {
        return mInfo;
    }

    public String getSubTitle() {
        return mSubTitle;
    }

    public String getTitle() {
        return mTitle;
    }
}