package com.example.inventaristoko.Model;

import com.google.gson.annotations.SerializedName;

public class Penjualan {
    @SerializedName("urutan")
    private String urutan;
    @SerializedName("refNo")
    private String refNo;
    @SerializedName("status")
    private String status;
    @SerializedName("statusCode")
    private String statusCode;
    @SerializedName("total_harga")
    private String totalHarga;
    @SerializedName("createdAt")
    private String createdAt;
//    @SerializedName("details")
//    private String details;

    public Penjualan(String urutan, String refNo, String status, String statusCode, String totalHarga, String createdAt) {
        this.urutan = urutan;
        this.refNo = refNo;
        this.status = status;
        this.statusCode = statusCode;
        this.totalHarga = totalHarga;
        this.createdAt = createdAt;
//        this.details = details;
    }

    public String getUrutan() {
        return urutan;
    }

    public String getRefNo() {
        return refNo;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getTotalHarga() {
        return totalHarga;
    }

    public String getCreatedAt() {
        return createdAt;
    }

//    public String getDetails() {
//        return details;
//    }
}
