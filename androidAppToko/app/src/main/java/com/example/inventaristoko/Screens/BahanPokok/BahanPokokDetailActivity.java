package com.example.inventaristoko.Screens.BahanPokok;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventaristoko.Adapter.BahanPokok.BahanPokokHistoryAdapter;
import com.example.inventaristoko.Model.BahanPokok.BahanPokokHistory;
import com.example.inventaristoko.Model.BahanPokok.BahanPokokMakanan;
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
    private RecyclerView mRecyclerView;
    private ListView mListView;
    ArrayAdapter<CharSequence> adapter;
    private BahanPokokHistoryAdapter mBahanPokokHistoryAdapter;
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

        mRecyclerView = findViewById(R.id.rvBahanPokokDetail);
        ButterKnife.bind(this);

        setUpRiwayatMakanan();

        mListView = findViewById(R.id.lvMakananBahanPokok);
        ButterKnife.bind(this);
        setUpDetailMakanan();
    }

    private void setUpDetailMakanan() {
        adapter = ArrayAdapter.createFromResource(this,R.array.countries_arry,android.R.layout.simple_list_item_1);
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
                    ArrayList<BahanPokokMakanan> mBahanPokokMakanan = new ArrayList<>();
                    JSONObject resultJSON = new JSONObject(result);
                    JSONObject resultArray = resultJSON.getJSONObject("result");
                    JSONArray historyArray = resultArray.getJSONArray("makanan_details");

                    for(int i = 0 ; i < historyArray.length() ; i ++ ) {
                        JSONObject dataBahanPokokDetail = (JSONObject) historyArray.get(i);
                        BahanPokokMakanan bahanPokokMakanan = new BahanPokokMakanan();
                        bahanPokokMakanan.setNamaMakanan(dataBahanPokokDetail.getString("nama"));
                        mBahanPokokMakanan.add(bahanPokokMakanan);
                    }
                    adapter.addAll((CharSequence) mBahanPokokMakanan);
                    mListView.setAdapter(adapter);
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
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
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
                    mRecyclerView.setAdapter(mBahanPokokHistoryAdapter);
                    CommonUtils.hideLoading();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}