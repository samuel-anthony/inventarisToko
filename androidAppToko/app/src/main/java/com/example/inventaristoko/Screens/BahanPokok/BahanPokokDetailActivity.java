package com.example.inventaristoko.Screens.BahanPokok;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.inventaristoko.Adapter.BahanPokok.BahanPokokFoodAdapter;
import com.example.inventaristoko.Adapter.BahanPokok.BahanPokokHistoryAdapter;
import com.example.inventaristoko.Model.BahanPokok.BahanPokokHistory;
import com.example.inventaristoko.Model.BahanPokok.BahanPokokFood;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.VolleyAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BahanPokokDetailActivity extends AppCompatActivity {
    private RecyclerView mRecyclerViewDetailHistory, mRecyclerViewDetailFood;
    private BahanPokokHistoryAdapter mBahanPokokHistoryAdapter;
    private BahanPokokFoodAdapter mBahanPokokFoodAdapter;
    private Button btnTambahDetailBahanPokok;
    private TextView tvNamaBahanPokok, tvJumlahBahanPokok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bahan_pokok_detail);

        getSupportActionBar().setTitle(R.string.menu_detail_bahan_pokok);

        tvNamaBahanPokok = findViewById(R.id.tvValueNamaBahanPokok);
        tvJumlahBahanPokok = findViewById(R.id.tvValueJumlahBahanPokok);

        btnTambahDetailBahanPokok = findViewById(R.id.btnTambahDetailBahanPokok);

        Bundle bundle = getIntent().getExtras();
        tvNamaBahanPokok.setText(bundle.getString("namaBahanPokok"));
        tvJumlahBahanPokok.setText(bundle.getString("jumlahBahanPokok") + " " + bundle.getString("satuanBahanPokok"));

        btnTambahDetailBahanPokok.setOnClickListener(v -> {
            Intent intent = new Intent (v.getContext(), BahanPokokEntryActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putString("screenState", MyConstants.TAMBAH_DETAIL_BAHAN_POKOK);
            mBundle.putString("namaBahanPokok", tvNamaBahanPokok.getText().toString());
            mBundle.putString("jumlahBahanPokok", bundle.getString("jumlahBahanPokok"));
            mBundle.putString("satuanBahanPokok", bundle.getString("satuanBahanPokok"));
            intent.putExtras(mBundle);
            v.getContext().startActivity(intent);
        });

        mRecyclerViewDetailHistory = findViewById(R.id.rvDataBahanPokokRiwayat);
        ButterKnife.bind(this);
        setUpRiwayatMakanan();

        mRecyclerViewDetailFood = findViewById(R.id.rvDataBahanPokokMakanan);
        ButterKnife.bind(this);
        setUpDetailMakanan();
    }

    private void setUpDetailMakanan() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerViewDetailFood.setLayoutManager(mLayoutManager);
        mRecyclerViewDetailFood.setItemAnimator(new DefaultItemAnimator());
        mBahanPokokFoodAdapter = new BahanPokokFoodAdapter(new ArrayList<>());

        prepareDataBahanPokokMakananDetail();
        CommonUtils.hideLoading();
    }

    private void setUpRiwayatMakanan() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerViewDetailHistory.setLayoutManager(mLayoutManager);
        mRecyclerViewDetailHistory.setItemAnimator(new DefaultItemAnimator());
        mBahanPokokHistoryAdapter = new BahanPokokHistoryAdapter(new ArrayList<>());

        prepareDataBahanPokokRiwayatDetail();
        CommonUtils.hideLoading();
    }

    private void prepareDataBahanPokokMakananDetail() {
        CommonUtils.showLoading(BahanPokokDetailActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(this);
        Bundle bundle = getIntent().getExtras();

        Map<String, String> params = new HashMap<>();
        params.put("bahan_pokok_id", bundle.getString("idBahanPokok"));

        volleyAPI.getRequest("getBahanPokokDetailHistory", params, result -> {
            try {
                ArrayList<BahanPokokFood> mBahanPokokFood = new ArrayList<>();
                JSONObject resultJSON = new JSONObject(result);
                JSONObject resultArray = resultJSON.getJSONObject("result");
                JSONArray historyArray = resultArray.getJSONArray("makanan");

                for(int i = 0 ; i < historyArray.length() ; i ++ ) {
                    JSONObject dataBahanPokokMakanan = (JSONObject) historyArray.get(i);
                    BahanPokokFood bahanPokokFood = new BahanPokokFood();
                    bahanPokokFood.setIdDetailMakanan(String.valueOf(i+1));
                    bahanPokokFood.setNamaDetailMakananBahanPokok(dataBahanPokokMakanan.getString("nama"));
                    bahanPokokFood.setHargaDetailMakananBahanPokok(dataBahanPokokMakanan.getString("harga_jual"));
                    bahanPokokFood.setTanggalTambahDetailMakananBahanPokok(dataBahanPokokMakanan.getString("created_at"));
                    bahanPokokFood.setTanggalUbahDetailMakananBahanPokok(dataBahanPokokMakanan.getString("updated_at"));
                    mBahanPokokFood.add(bahanPokokFood);
                }

                mBahanPokokFoodAdapter.addItems(mBahanPokokFood);
                mRecyclerViewDetailFood.setAdapter(mBahanPokokFoodAdapter);
                CommonUtils.hideLoading();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void prepareDataBahanPokokRiwayatDetail() {
        CommonUtils.showLoading(BahanPokokDetailActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(this);
        Bundle bundle = getIntent().getExtras();

        Map<String, String> params = new HashMap<>();
        params.put("bahan_pokok_id", bundle.getString("idBahanPokok"));

        volleyAPI.getRequest("getBahanPokokDetailHistory", params, result -> {
            try {
                ArrayList<BahanPokokHistory> mBahanPokokHistory = new ArrayList<>();
                JSONObject resultJSON = new JSONObject(result);
                JSONObject resultArray = resultJSON.getJSONObject("result");
                JSONArray historyArray = resultArray.getJSONArray("riwayats");

                for(int i = 0 ; i < historyArray.length() ; i ++ ) {
                    JSONObject dataBahanPokokRiwayatDetail = (JSONObject) historyArray.get(i);
                    BahanPokokHistory bahanPokokHistory = new BahanPokokHistory();
                    bahanPokokHistory.setJumlahDetailRiwayatBahanPokok(dataBahanPokokRiwayatDetail.getString("jumlah"));
                    bahanPokokHistory.setAksiDetailRiwayatBahanPokok(dataBahanPokokRiwayatDetail.getString("aksi"));
                    bahanPokokHistory.setNamaTokoDetailRiwayatBahanPokok(dataBahanPokokRiwayatDetail.getString("nama_toko"));
                    bahanPokokHistory.setHargaDetailRiwayatBahanPokok(dataBahanPokokRiwayatDetail.getString("harga"));
                    bahanPokokHistory.setTanggalTambahDetailRiwayatBahanPokok(dataBahanPokokRiwayatDetail.getString("created_at"));
                    bahanPokokHistory.setTanggalUbahDetailRiwayatBahanPokok(dataBahanPokokRiwayatDetail.getString("updated_at"));
                    mBahanPokokHistory.add(bahanPokokHistory);
                }

                mBahanPokokHistoryAdapter.addItems(mBahanPokokHistory);
                mRecyclerViewDetailHistory.setAdapter(mBahanPokokHistoryAdapter);
                CommonUtils.hideLoading();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }
}