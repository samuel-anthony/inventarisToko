package com.example.inventaristoko.Model.BahanPokok;

import java.io.Serializable;

public class Pemasok implements Serializable {
    private String idPemasok;
    private String namaPemasok;
    private String alamatPemasok;
    private String nomorTeleponPemasok;
    private String tanggalTambahPemasok;
    private String tanggalUbahPemasok;

    public Pemasok() {
    }

    public Pemasok(String idPemasok, String namaPemasok) {
        this.idPemasok = idPemasok;
        this.namaPemasok = namaPemasok;
    }

    public String getIdPemasok() {
        return idPemasok;
    }

    public void setIdPemasok(String idPemasok) {
        this.idPemasok = idPemasok;
    }

    public String getNamaPemasok() {
        return namaPemasok;
    }

    public void setNamaPemasok(String namaPemasok) {
        this.namaPemasok = namaPemasok;
    }

    public String getAlamatPemasok() {
        return alamatPemasok;
    }

    public void setAlamatPemasok(String alamatPemasok) {
        this.alamatPemasok = alamatPemasok;
    }

    public String getNomorTeleponPemasok() {
        return nomorTeleponPemasok;
    }

    public void setNomorTeleponPemasok(String nomorTeleponPemasok) {
        this.nomorTeleponPemasok = nomorTeleponPemasok;
    }

    public String getTanggalTambahPemasok() {
        return tanggalTambahPemasok;
    }

    public void setTanggalTambahPemasok(String tanggalTambahPemasok) {
        this.tanggalTambahPemasok = tanggalTambahPemasok;
    }

    public String getTanggalUbahPemasok() {
        return tanggalUbahPemasok;
    }

    public void setTanggalUbahPemasok(String tanggalUbahPemasok) {
        this.tanggalUbahPemasok = tanggalUbahPemasok;
    }

    @Override
    public String toString() {
        return namaPemasok;
    }
}
