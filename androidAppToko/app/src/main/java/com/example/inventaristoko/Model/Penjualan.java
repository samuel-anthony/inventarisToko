package com.example.inventaristoko.Model;

import com.google.gson.annotations.SerializedName;

public class Penjualan {
    @SerializedName("refNo")
    private String refNo;
    @SerializedName("status")
    private String status;
    @SerializedName("urutan")
    private String urutan;

    public Penjualan(String refNo, String status, String urutan) {
        this.refNo = refNo;
        this.status = status;
        this.urutan = urutan;
    }

    public String getRefNo() {
        return refNo;
    }

    public String getStatus() {
        return status;
    }

    public String getUrutan() {
        return urutan;
    }
}
