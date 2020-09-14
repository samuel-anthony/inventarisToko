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
    private Button btnAddDetail;
    private TextView tvStapleName, tvStapleAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bahan_pokok_detail);

        getSupportActionBar().setTitle(R.string.label_detil_bahan_pokok);

        tvStapleName = findViewById(R.id.labelValueBahanPokokNama);
        tvStapleAmount = findViewById(R.id.labelValueBahanPokokSatuan);

        btnAddDetail = findViewById(R.id.buttonAddDetail);

        Bundle bundle = getIntent().getExtras();
        tvStapleName.setText(bundle.getString("stapleName"));
        tvStapleAmount.setText(bundle.getString("stapleAmount") + " " + bundle.getString("stapleUnit"));

        btnAddDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent (v.getContext(), BahanPokokDetailEntryActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("screenState", MyConstants.EDIT_BAHAN_POKOK);
//                bundle.putString("stapleName", tvStapleName.getText().toString());
//                bundle.putString("stapleAmount", tvStapleAmount.getText().toString());
//                intent.putExtras(bundle);
//                v.getContext().startActivity(intent);

                Toast.makeText(getApplicationContext(), "Add Detail Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        mRecyclerViewDetail = findViewById(R.id.rvBahanPokokDetail);
        ButterKnife.bind(this);
        setUpRiwayatMakanan();

        mRecyclerViewFood = findViewById(R.id.rvBahanPokokMakanan);
        ButterKnife.bind(this);
        setUpDetailMakanan();
    }

    private void setUpDetailMakanan() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerViewFood.setLayoutManager(mLayoutManager);
        mRecyclerViewFood.setItemAnimator(new DefaultItemAnimator());
        mBahanPokokFoodAdapter = new BahanPokokFoodAdapter(new ArrayList<>());

        prepareDataBahanPokokMakananDetail();
    }

    private void prepareDataBahanPokokMakananDetail() {
        CommonUtils.showLoading(BahanPokokDetailActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(this);
        Bundle bundle = getIntent().getExtras();
        Map<String, String> params = new HashMap<>();
        params.put("bahan_pokok_id", bundle.getString("stapleId"));

        volleyAPI.getRequest("getBahanPokokDetailHistory", params, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                try {
                    ArrayList<BahanPokokFood> mBahanPokokFood = new ArrayList<>();
                    JSONObject resultJSON = new JSONObject(result);
                    JSONObject resultArray = resultJSON.getJSONObject("result");
                    JSONArray historyArray = resultArray.getJSONArray("makanan_details");

                    for(int i = 0 ; i < historyArray.length() ; i ++ ) {
                        JSONObject dataBahanPokokDetail = (JSONObject) historyArray.get(i);
                        JSONObject dataBahanPokokMakanan = dataBahanPokokDetail.getJSONObject("makanan_master");

                        BahanPokokFood bahanPokokFood = new BahanPokokFood();
                        bahanPokokFood.setId(String.valueOf(i+1));
                        bahanPokokFood.setStapleFoodName(dataBahanPokokMakanan.getString("nama"));
                        bahanPokokFood.setStapleFoodPrice(dataBahanPokokMakanan.getString("harga_jual"));
                        bahanPokokFood.setStapleFoodCreatedAt(dataBahanPokokMakanan.getString("created_at"));
                        mBahanPokokFood.add(bahanPokokFood);
                    }
                    mBahanPokokFoodAdapter.addItems(mBahanPokokFood);
                    mRecyclerViewFood.setAdapter(mBahanPokokFoodAdapter);
                    CommonUtils.hideLoading();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

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
        params.put("bahan_pokok_id", bundle.getString("stapleId"));

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
                        bahanPokokHistory.setStapleDetailAmount(dataBahanPokokDetail.getString("jumlah"));
                        bahanPokokHistory.setStapleDetailAction(dataBahanPokokDetail.getString("aksi"));
                        bahanPokokHistory.setStapleDetailStoreName(dataBahanPokokDetail.getString("nama_toko"));
                        bahanPokokHistory.setStapleDetailPrice(dataBahanPokokDetail.getString("harga"));
                        bahanPokokHistory.setStapleDetailCreatedAt(dataBahanPokokDetail.getString("created_at"));
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