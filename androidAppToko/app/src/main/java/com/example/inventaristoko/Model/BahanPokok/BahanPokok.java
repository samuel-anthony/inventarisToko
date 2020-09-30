package com.example.inventaristoko.Model.BahanPokok;

import java.io.Serializable;

public class BahanPokok implements Serializable  {
    private String id;
    private String idBahanPokok;
    private String namaBahanPokok;
    private String jumlahBahanPokok;
    private String satuanBahanPokok;
    private String tanggalTambahBahanPokok;
    private String tanggalUbahBahanPokok;

    public BahanPokok() {
    }

    public BahanPokok(String idBahanPokok, String namaBahanPokok) {
        this.idBahanPokok = idBahanPokok;
        this.namaBahanPokok = namaBahanPokok;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdBahanPokok() {
        return idBahanPokok;
    }

    public void setIdBahanPokok(String idBahanPokok) {
        this.idBahanPokok = idBahanPokok;
    }

    public String getNamaBahanPokok() {
        return namaBahanPokok;
    }

    public void setNamaBahanPokok(String namaBahanPokok) {
        this.namaBahanPokok = namaBahanPokok;
    }

    public String getJumlahBahanPokok() {
        return jumlahBahanPokok;
    }

    public void setJumlahBahanPokok(String jumlahBahanPokok) {
        this.jumlahBahanPokok = jumlahBahanPokok;
    }

    public String getSatuanBahanPokok() {
        return satuanBahanPokok;
    }

    public void setSatuanBahanPokok(String satuanBahanPokok) {
        this.satuanBahanPokok = satuanBahanPokok;
    }

    public String getTanggalTambahBahanPokok() {
        return tanggalTambahBahanPokok;
    }

    public void setTanggalTambahBahanPokok(String tanggalTambahBahanPokok) {
        this.tanggalTambahBahanPokok = tanggalTambahBahanPokok;
    }

    public String getTanggalUbahBahanPokok() {
        return tanggalUbahBahanPokok;
    }

    public void setTanggalUbahBahanPokok(String tanggalUbahBahanPokok) {
        this.tanggalUbahBahanPokok = tanggalUbahBahanPokok;
    }

    @Override
    public String toString() {
        return namaBahanPokok;
    }
}
