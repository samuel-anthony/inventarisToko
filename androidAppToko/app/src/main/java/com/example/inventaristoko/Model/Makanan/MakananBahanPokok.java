package com.example.inventaristoko.Model.Makanan;

public class MakananBahanPokok {
    private int bahan_pokok_id;
    private String name;
    private String jumlah;
    private String satuan;

    public int getBahan_pokok_id() {
        return bahan_pokok_id;
    }

    public void setBahan_pokok_id(int bahan_pokok_id) {
        this.bahan_pokok_id = bahan_pokok_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }
}
