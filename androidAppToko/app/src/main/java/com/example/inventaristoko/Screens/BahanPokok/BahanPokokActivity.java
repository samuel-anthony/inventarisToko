package com.example.inventaristoko.Screens.BahanPokok;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.inventaristoko.Adapter.BahanPokok.BahanPokokAdapter;
import com.example.inventaristoko.Model.BahanPokok.BahanPokok;
import com.example.inventaristoko.Model.BahanPokok.Pemasok;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Screens.Makanan.MakananEntryActivity;
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
            ArrayList<Pemasok> mPemasok = new ArrayList<>();
            VolleyAPI volleyAPI = new VolleyAPI(v.getContext());

            Map<String, String> params = new HashMap<>();

            volleyAPI.getRequest("getSemuaSupplier", params, result -> {
                try {
                    JSONObject resultJSON = new JSONObject(result);
                    JSONArray resultArray = resultJSON.getJSONArray("result");

                    for(int i = 0 ; i < resultArray.length() ; i ++ ) {
                        JSONObject dataPemasok = (JSONObject) resultArray.get(i);
                        Pemasok pemasok = new Pemasok();
                        pemasok.setIdPemasok(dataPemasok.getString("supplier_id"));
                        pemasok.setNamaPemasok(dataPemasok.getString("nama"));
                        pemasok.setAlamatPemasok(dataPemasok.getString("alamat"));
                        pemasok.setNomorTeleponPemasok(dataPemasok.getString("nomor_telepon"));
                        pemasok.setTanggalTambahPemasok(dataPemasok.getString("created_at"));
                        pemasok.setTanggalUbahPemasok(dataPemasok.getString("updated_at"));
                        mPemasok.add(pemasok);
                    }

                    Intent intent = new Intent(v.getContext(), BahanPokokEntryActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("screenState", MyConstants.TAMBAH_BAHAN_POKOK);
                    bundle.putSerializable("daftarPemasok", mPemasok);
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
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