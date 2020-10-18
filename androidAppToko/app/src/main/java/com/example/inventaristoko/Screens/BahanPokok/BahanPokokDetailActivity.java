package com.example.inventaristoko.Screens.BahanPokok;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.inventaristoko.Adapter.BahanPokok.BahanPokokFoodAdapter;
import com.example.inventaristoko.Adapter.BahanPokok.BahanPokokHistoryAdapter;
import com.example.inventaristoko.Model.BahanPokok.BahanPokokHistory;
import com.example.inventaristoko.Model.BahanPokok.BahanPokokFood;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Screens.Pengunjung.PengunjungCartActivity;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.VolleyAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class BahanPokokDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView rvBahanPokokHistoryDetail, rvBahanPokokFoodDetail;
    private BahanPokokHistoryAdapter bahanPokokHistoryAdapter;
    private BahanPokokFoodAdapter bahanPokokFoodAdapter;
    private Button btnTambahDetailBahanPokok, btnTambahDetailPemasok;
    private TextView tvNamaBahanPokok, tvJumlahBahanPokok;
    private String idBahanPokok, jumlahBahanPokok, satuanBahanPokok;

    private void init() {
        rvBahanPokokHistoryDetail = findViewById(R.id.rvBahanPokokHistoryDetail);
        rvBahanPokokFoodDetail = findViewById(R.id.rvBahanPokokFoodDetail);
        tvNamaBahanPokok = findViewById(R.id.tvValueNamaBahanPokok);
        tvJumlahBahanPokok = findViewById(R.id.tvValueJumlahBahanPokok);
        btnTambahDetailBahanPokok = findViewById(R.id.btnTambahDetailBahanPokok);
        btnTambahDetailPemasok = findViewById(R.id.btnTambahDetailPemasok);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bahan_pokok_detail);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_detail_bahan_pokok);

        init();

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        idBahanPokok = bundle.getString("idBahanPokok");
        jumlahBahanPokok = bundle.getString("jumlahBahanPokok");
        satuanBahanPokok = bundle.getString("satuanBahanPokok");

        tvNamaBahanPokok.setText(bundle.getString("namaBahanPokok"));
        tvJumlahBahanPokok.setText(String.format("%s %s", bundle.getString("jumlahBahanPokok"), bundle.getString("satuanBahanPokok")));

        btnTambahDetailBahanPokok.setOnClickListener(this);
        btnTambahDetailPemasok.setOnClickListener(this);

        setUpRiwayatMakanan();
        setUpDetailMakanan();
    }

    private void setUpDetailMakanan() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvBahanPokokFoodDetail.setLayoutManager(mLayoutManager);
        rvBahanPokokFoodDetail.setItemAnimator(new DefaultItemAnimator());
        bahanPokokFoodAdapter = new BahanPokokFoodAdapter(new ArrayList<>());

        callDataBahanPokokFoodDetail();
    }

    private void setUpRiwayatMakanan() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvBahanPokokHistoryDetail.setLayoutManager(mLayoutManager);
        rvBahanPokokHistoryDetail.setItemAnimator(new DefaultItemAnimator());
        bahanPokokHistoryAdapter = new BahanPokokHistoryAdapter(new ArrayList<>());

        callDataBahanPokokHistoryDetail();
    }

    private void callDataBahanPokokFoodDetail() {
        CommonUtils.showLoading(BahanPokokDetailActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(this);

        Map<String, String> params = new HashMap<>();
        params.put("bahan_pokok_id", idBahanPokok);

        volleyAPI.getRequest(MyConstants.BAHAN_POKOK_GET_DETAIL_ACTION, params, result -> {
            try {
                ArrayList<BahanPokokFood> mBahanPokokFood = new ArrayList<>();
                JSONObject resultJSON = new JSONObject(result);
                JSONObject resultArray = resultJSON.getJSONObject("result");
                JSONArray foodArray = resultArray.getJSONArray("makanan");

                for(int i = 0 ; i < foodArray.length() ; i ++ ) {
                    JSONObject dataBahanPokokMakanan = (JSONObject) foodArray.get(i);
                    BahanPokokFood bahanPokokFood = new BahanPokokFood();
                    bahanPokokFood.setIdDetailMakanan(String.valueOf(i+1));
                    bahanPokokFood.setNamaDetailMakananBahanPokok(dataBahanPokokMakanan.getString("nama"));
                    bahanPokokFood.setHargaDetailMakananBahanPokok(dataBahanPokokMakanan.getString("harga_jual"));
                    bahanPokokFood.setTanggalTambahDetailMakananBahanPokok(dataBahanPokokMakanan.getString("created_at"));
                    bahanPokokFood.setTanggalUbahDetailMakananBahanPokok(dataBahanPokokMakanan.getString("updated_at"));
                    mBahanPokokFood.add(bahanPokokFood);
                }

                bahanPokokFoodAdapter.addItems(mBahanPokokFood);
                rvBahanPokokFoodDetail.setAdapter(bahanPokokFoodAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        CommonUtils.hideLoading();
    }

    private void callDataBahanPokokHistoryDetail() {
        CommonUtils.showLoading(BahanPokokDetailActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(this);

        Map<String, String> params = new HashMap<>();
        params.put("bahan_pokok_id", idBahanPokok);

        volleyAPI.getRequest(MyConstants.BAHAN_POKOK_GET_DETAIL_ACTION, params, result -> {
            try {
                ArrayList<BahanPokokHistory> mBahanPokokHistory = new ArrayList<>();
                JSONObject resultJSON = new JSONObject(result);
                JSONObject resultArray = resultJSON.getJSONObject("result");
                JSONArray historyArray = resultArray.getJSONArray("riwayats");

                for(int i = 0 ; i < historyArray.length() ; i ++ ) {
                    JSONObject dataBahanPokokRiwayatDetail = (JSONObject) historyArray.get(i);
                    BahanPokokHistory bahanPokokHistory = new BahanPokokHistory();
                    bahanPokokHistory.setJumlahDetailRiwayatBahanPokok(dataBahanPokokRiwayatDetail.getString("jumlah"));
                    bahanPokokHistory.setSatuanDetailRiwayatBahanPokok(dataBahanPokokRiwayatDetail.getString("satuan"));
                    bahanPokokHistory.setAksiDetailRiwayatBahanPokok(dataBahanPokokRiwayatDetail.getString("aksi"));
                    bahanPokokHistory.setNamaTokoDetailRiwayatBahanPokok(dataBahanPokokRiwayatDetail.getString("nama_toko"));
                    bahanPokokHistory.setHargaDetailRiwayatBahanPokok(dataBahanPokokRiwayatDetail.getString("harga"));
                    bahanPokokHistory.setTanggalTambahDetailRiwayatBahanPokok(dataBahanPokokRiwayatDetail.getString("created_at"));
                    bahanPokokHistory.setTanggalUbahDetailRiwayatBahanPokok(dataBahanPokokRiwayatDetail.getString("updated_at"));
                    mBahanPokokHistory.add(bahanPokokHistory);
                }

                bahanPokokHistoryAdapter.addItems(mBahanPokokHistory);
                rvBahanPokokHistoryDetail.setAdapter(bahanPokokHistoryAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        CommonUtils.hideLoading();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTambahDetailBahanPokok:
                Intent intent = new Intent(v.getContext(), BahanPokokEntryActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("screenState", MyConstants.TAMBAH_DETAIL_BAHAN_POKOK);
                mBundle.putString("idBahanPokok", idBahanPokok);
                mBundle.putString("namaBahanPokok", tvNamaBahanPokok.getText().toString());
                mBundle.putString("jumlahBahanPokok", jumlahBahanPokok);
                mBundle.putString("satuanBahanPokok", satuanBahanPokok);
                intent.putExtras(mBundle);
                v.getContext().startActivity(intent);
                break;
            case R.id.btnTambahDetailPemasok:
                Intent intent2 = new Intent(getApplicationContext(), BahanPokokSupplierActivity.class);
                startActivityForResult(intent2, 0);
                break;
        }
    }
}