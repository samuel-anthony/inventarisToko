package com.example.inventaristoko.Screens.BahanPokok;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.inventaristoko.Adapter.BahanPokok.BahanPokokAdapter;
import com.example.inventaristoko.Model.BahanPokok.BahanPokok;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.VolleyAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

public class BahanPokokActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private BahanPokokAdapter mBahanPokokAdapter;
    private Button btnTambahBahanPokok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bahan_pokok);

        getSupportActionBar().setTitle(R.string.menu_detail_bahan_pokok);

        FloatingActionButton fab = findViewById(R.id.fabDataBahanPokok);
        fab.setOnClickListener(view -> Snackbar.make(view, "Terjadi Kesalahan!", Snackbar.LENGTH_LONG).setAction("Action", null).show());

        btnTambahBahanPokok = findViewById(R.id.btnTambahBahanPokok);
        btnTambahBahanPokok.setOnClickListener(v -> {
            Intent intent = new Intent (v.getContext(), BahanPokokEntryActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("screenState", MyConstants.TAMBAH_BAHAN_POKOK);
            intent.putExtras(bundle);
            v.getContext().startActivity(intent);
        });

        mRecyclerView = findViewById(R.id.rvDataBahanPokok);
        ButterKnife.bind(this);
        setUp();
    }

    private void setUp() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mBahanPokokAdapter = new BahanPokokAdapter(new ArrayList<>());

        prepareDataBahanPokok();
    }

    private void prepareDataBahanPokok() {
        CommonUtils.showLoading(BahanPokokActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(this);

        Map<String, String> params = new HashMap<>();

        volleyAPI.getRequest("getSemuaBahanPokok", params, result -> {
            try {
                ArrayList<BahanPokok> mBahanPokok = new ArrayList<>();
                JSONObject resultJSON = new JSONObject(result);
                JSONArray resultArray = resultJSON.getJSONArray("result");

                for(int i = 0 ; i < resultArray.length() ; i ++ ) {
                    JSONObject dataBahanPokok = (JSONObject) resultArray.get(i);
                    BahanPokok bahanPokok = new BahanPokok();
                    bahanPokok.setId(String.valueOf(i+1));
                    bahanPokok.setIdBahanPokok(dataBahanPokok.getString("bahan_pokok_id"));
                    bahanPokok.setNamaBahanPokok(dataBahanPokok.getString("nama"));
                    bahanPokok.setJumlahBahanPokok(dataBahanPokok.getString("jumlah"));
                    bahanPokok.setSatuanBahanPokok(dataBahanPokok.getString("satuan"));
                    bahanPokok.setTanggalTambahBahanPokok(dataBahanPokok.getString("created_at"));
                    bahanPokok.setTanggalUbahBahanPokok(dataBahanPokok.getString("updated_at"));

                    mBahanPokok.add(bahanPokok);
                }

                mBahanPokokAdapter.addItems(mBahanPokok);
                mRecyclerView.setAdapter(mBahanPokokAdapter);
                CommonUtils.hideLoading();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }
}