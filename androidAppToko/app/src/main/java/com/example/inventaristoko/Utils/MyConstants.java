package com.example.inventaristoko.Utils;

public interface MyConstants {
    String ORDER_CODE = "001";
    String GOING_CODE = "002";
    String FINISH_CODE = "003";

    String MILI_GRAM = "Mg";
    String DESI_GRAM = "Dg";
    String CENTI_GRAM = "Cg";
    String GRAM = "G";
    String HEKTO_GRAM = "Hg";
    String DEKA_GRAM = "Dg";
    String KILO_GRAM = "Kg";

    String INCREASE_CODE = "1";
    String DECREASE_CODE = "0";

    String ORDER_NAME = "Dipesan";
    String GOING_NAME = "Sedang Dibuat";
    String FINISH_NAME = "Sudah Selesai";

    int ORDER_COLOR = 0xFF000000;
    int GOING_COLOR = 0xFF409AE1;
    int FINISH_COLOR = 0xFF40C5AF;

    String TAMBAH_KATEGORI = "Tambah Kategori";
    String UBAH_KATEGORI = "Ubah Kategori";

    String TAMBAH_PENGGUNA = "Tambah Pengguna";
    String UBAH_PENGGUNA = "Ubah Pengguna";

    String TAMBAH_BAHAN_POKOK = "Tambah Bahan Pokok";
    String TAMBAH_DETAIL_BAHAN_POKOK = "Tambah Detail Bahan Pokok";

    String TAMBAH_MEJA = "Tambah Meja";
    String UBAH_MEJA = "Ubah Meja";

    String TAMBAH_MAKANAN = "Tambah Makanan";
    String UBAH_MAKANAN = "Ubah Makanan";


    // Service
    String LOGIN_ACTION = "login";
    String CHANGE_PASSWORD_ACTION = "updateUserPassword";

    String MEJA_GET_ACTION = "getSemuaDataMeja";
    String MEJA_DELETE_ACTION = "deleteUser";
    String MEJA_EDIT_ACTION = "updateUser";
    String MEJA_ADD_ACTION = "register";

    String PENJUALAN_GET_ACTION = "getPesananBelumSelesai";
    String PENJUALAN_GET_DETAIL_ACTION = "getPesananDetailRefNo";
    String PENJUALAN_EDIT_STATUS_ACTION = "updateStatusPesanan";
    String PENJUALAN_SUBMIT_PENJUALAN_ACTION = "updateStatusPesananSelesai";

    String PENGGUNA_GET_ACTION = "getSemuaAdminUser";
    String PENGGUNA_DELETE_ACTION = "deleteAdmin";
    String PENGGUNA_EDIT_ACTION = "updateAdminUser";
    String PENGGUNA_ADD_ACTION = "registerAdmin";

    String KATEGORI_GET_ACTION = "getSemuaJenisMenu";
    String KATEGORI_GET_DETAIL_ACTION = "getSemuaJenisMenuDetail";
    String KATEGORI_DELETE_ACTION = "deleteJenisMenu";
    String KATEGORI_EDIT_ACTION = "updateJenisMenu";
    String KATEGORI_ADD_ACTION = "addJenisMenu";

    String MAKANAN_GET_ACTION = "getSemuaMakanan";
    String MAKANAN_GET_DETAIL_ACTION = "getMakananDetail";
    String MAKANAN_GET_IMAGE_DETAIL_ACTION = "getGambarMakananDetail";
    String MAKANAN_DELETE_ACTION = "deleteMakanan";
    String MAKANAN_EDIT_ACTION = "updateMakanan";
    String MAKANAN_ADD_ACTION = "addMakanan";

    String BAHAN_POKOK_GET_ACTION = "getSemuaBahanPokok";
    String BAHAN_POKOK_GET_DETAIL_ACTION = "getBahanPokokDetailHistory";
    String BAHAN_POKOK_GET_SUPPLIER_ACTION = "getSemuaSupplier";
    String BAHAN_POKOK_ADD_ACTION = "addBahanPokokBaru";
    String BAHAN_POKOK_ADD_HISTORY_ACTION = "addRiwayatBahanPokok";
}
