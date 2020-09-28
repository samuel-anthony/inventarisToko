package com.example.inventaristoko.Model.Makanan;

import java.io.Serializable;

public class Makanan implements Serializable {
    private String id;
    private String idMakanan;
    private String namaMakanan;
    private String hargaMakanan;
    private String gambarMakanan;
    private String bahanMakanan;
    private String tanggalTambahMakanan;
    private String tanggalUbahMakanan;

    public Makanan() {
    }

    public Makanan(String idMakanan, String namaMakanan) {
        this.idMakanan = idMakanan;
        this.namaMakanan = namaMakanan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdMakanan() {
        return idMakanan;
    }

    public void setIdMakanan(String idMakanan) {
        this.idMakanan = idMakanan;
    }

    public String getNamaMakanan() {
        return namaMakanan;
    }

    public void setNamaMakanan(String namaMakanan) {
        this.namaMakanan = namaMakanan;
    }

    public String getHargaMakanan() {
        return hargaMakanan;
    }

    public void setHargaMakanan(String hargaMakanan) {
        this.hargaMakanan = hargaMakanan;
    }

    public String getGambarMakanan() {
        return gambarMakanan;
    }

    public void setGambarMakanan(String gambarMakanan) {
        this.gambarMakanan = gambarMakanan;
    }

    public String getBahanMakanan() {
        return bahanMakanan;
    }

    public void setBahanMakanan(String bahanMakanan) {
        this.bahanMakanan = bahanMakanan;
    }

    public String getTanggalTambahMakanan() {
        return tanggalTambahMakanan;
    }

    public void setTanggalTambahMakanan(String tanggalTambahMakanan) {
        this.tanggalTambahMakanan = tanggalTambahMakanan;
    }

    public String getTanggalUbahMakanan() {
        return tanggalUbahMakanan;
    }

    public void setTanggalUbahMakanan(String tanggalUbahMakanan) {
        this.tanggalUbahMakanan = tanggalUbahMakanan;
    }

    @Override
    public String toString() {
        return namaMakanan;
    }
}
