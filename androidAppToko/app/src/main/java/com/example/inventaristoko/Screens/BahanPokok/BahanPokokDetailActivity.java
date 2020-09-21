package com.example.inventaristoko.Screens.BahanPokok;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventaristoko.Adapter.BahanPokok.BahanPokokFoodAdapter;
import com.example.inventaristoko.Adapter.BahanPokok.BahanPokokHistoryAdapter;
import com.example.inventaristoko.Model.BahanPokok.BahanPokokHistory;
import com.example.inventaristoko.Model.BahanPokok.BahanPokokFood;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.VolleyAPI;
import com.example.inventaristoko.Utils.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BahanPokokDetailActivity extends AppCompatActivity {
    private RecyclerView mRecyclerViewDetail, mRecyclerViewFood;
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

        btnTambahDetailBahanPokok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent (v.getContext(), BahanPokokEntryActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("screenState", MyConstants.TAMBAH_DETAIL_BAHAN_POKOK);
//                bundle.putString("namaBahanPokok", tvNamaBahanPokok.getText().toString());
//                bundle.putString("jumlahBahanPokok", tvJumlahBahanPokok.getText().toString());
//                bundle.putString("satuanBahanPokok", bundle.getString("satuanBahanPokok"));
//                intent.putExtras(bundle);
//                v.getContext().startActivity(intent);

                Toast.makeText(getApplicationContext(), "Tambah Detail Bahan Pokok", Toast.LENGTH_SHORT).show();
            }
        });

        mRecyclerViewDetail = findViewById(R.id.rvDataBahanPokokRiwayat);
        ButterKnife.bind(this);
        setUpRiwayatMakanan();

//        mRecyclerViewFood = findViewById(R.id.rvDataBahanPokokMakanan);
//        ButterKnife.bind(this);
//        setUpDetailMakanan();
    }

//    private void setUpDetailMakanan() {
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
//        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
//        mRecyclerViewFood.setLayoutManager(mLayoutManager);
//        mRecyclerViewFood.setItemAnimator(new DefaultItemAnimator());
//        mBahanPokokFoodAdapter = new BahanPokokFoodAdapter(new ArrayList<>());
//
//        prepareDataBahanPokokMakananDetail();
//    }

//    private void prepareDataBahanPokokMakananDetail() {
//        CommonUtils.showLoading(BahanPokokDetailActivity.this);
//        VolleyAPI volleyAPI = new VolleyAPI(this);
//        Bundle bundle = getIntent().getExtras();
//        Map<String, String> params = new HashMap<>();
//        params.put("bahan_pokok_id", bundle.getString("idBahanPokok"));
//
//        volleyAPI.getRequest("getBahanPokokDetailHistory", params, new VolleyCallback() {
//            @Override
//            public void onSuccessResponse(String result) {
//                try {
//                    ArrayList<BahanPokokFood> mBahanPokokFood = new ArrayList<>();
//                    JSONObject resultJSON = new JSONObject(result);
//                    JSONObject resultArray = resultJSON.getJSONObject("result");
//                    JSONArray historyArray = resultArray.getJSONArray("makanan");
//
//                    for(int i = 0 ; i < historyArray.length() ; i ++ ) {
//                        JSONObject dataBahanPokokMakanan = (JSONObject) historyArray.get(i);
//                        BahanPokokFood bahanPokokFood = new BahanPokokFood();
//                        bahanPokokFood.setId(String.valueOf(i+1));
//                        bahanPokokFood.setStapleFoodName(dataBahanPokokMakanan.getString("nama"));
//                        bahanPokokFood.setStapleFoodPrice(dataBahanPokokMakanan.getString("harga_jual"));
//                        bahanPokokFood.setStapleFoodCreatedAt(dataBahanPokokMakanan.getString("created_at"));
//                        bahanPokokFood.setStapleFoodUpdatedAt(dataBahanPokokMakanan.getString("updated_at"));
//                        mBahanPokokFood.add(bahanPokokFood);
//                    }
//
//                    mBahanPokokFoodAdapter.addItems(mBahanPokokFood);
//                    mRecyclerViewFood.setAdapter(mBahanPokokFoodAdapter);
//                    CommonUtils.hideLoading();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    private void setUpRiwayatMakanan() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerViewDetail.setLayoutManager(mLayoutManager);
        mRecyclerViewDetail.setItemAnimator(new DefaultItemAnimator());
        mBahanPokokHistoryAdapter = new BahanPokokHistoryAdapter(new ArrayList<>());

        prepareDataBahanPokokRiwayatDetail();
    }

    private void prepareDataBahanPokokRiwayatDetail() {
        CommonUtils.showLoading(BahanPokokDetailActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(this);
        Bundle bundle = getIntent().getExtras();
        Map<String, String> params = new HashMap<>();
        params.put("bahan_pokok_id", bundle.getString("idBahanPokok"));

        volleyAPI.getRequest("getBahanPokokDetailHistory", params, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                try {
                    ArrayList<BahanPokokHistory> mBahanPokokHistory = new ArrayList<>();
                    JSONObject resultJSON = new JSONObject(result);
                    JSONObject resultArray = resultJSON.getJSONObject("result");
                    JSONArray historyArray = resultArray.getJSONArray("riwayats");

                    for(int i = 0 ; i < historyArray.length() ; i ++ ) {
                        JSONObject dataBahanPokokDetail = (JSONObject) historyArray.get(i);
                        BahanPokokHistory bahanPokokHistory = new BahanPokokHistory();
                        bahanPokokHistory.setJumlahDetailBahanPokok(dataBahanPokokDetail.getString("jumlah"));
                        bahanPokokHistory.setAksiDetailBahanPokok(dataBahanPokokDetail.getString("aksi"));
                        bahanPokokHistory.setNamaTokoDetailBahanPokok(dataBahanPokokDetail.getString("nama_toko"));
                        bahanPokokHistory.setHargaDetailBahanPokok(dataBahanPokokDetail.getString("harga"));
                        bahanPokokHistory.setTanggalTambahDetailBahanPokok(dataBahanPokokDetail.getString("created_at"));
                        bahanPokokHistory.setTanggalUbahDetailBahanPokok(dataBahanPokokDetail.getString("updated_at"));
                        mBahanPokokHistory.add(bahanPokokHistory);
                    }
                    mBahanPokokHistoryAdapter.addItems(mBahanPokokHistory);
                    mRecyclerViewDetail.setAdapter(mBahanPokokHistoryAdapter);
                    CommonUtils.hideLoading();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}