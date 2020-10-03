package com.example.inventaristoko.Screens.Makanan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.inventaristoko.Adapter.Makanan.MakananAdapter;
import com.example.inventaristoko.Model.BahanPokok.BahanPokok;
import com.example.inventaristoko.Model.Makanan.Makanan;
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

import butterknife.ButterKnife;

public class MakananActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MakananAdapter mMakananAdapter;
    private Button btnTambahMakanan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makanan);

        getSupportActionBar().setTitle(R.string.menu_makanan);

        FloatingActionButton fab = findViewById(R.id.fabMakanan);
        fab.setOnClickListener(view -> Snackbar.make(view, "Terjadi Kesalahan!", Snackbar.LENGTH_LONG).setAction("Action", null).show());

        btnTambahMakanan = findViewById(R.id.btnTambahMakanan);
        btnTambahMakanan.setOnClickListener(v -> {
            ArrayList<BahanPokok> mBahanPokok = new ArrayList<>();
            VolleyAPI volleyAPI = new VolleyAPI(v.getContext());
            Map<String, String> params = new HashMap<>();
            volleyAPI.getRequest("getSemuaBahanPokok", params, result -> {
                try {
                    JSONObject resultJSON = new JSONObject(result);
                    JSONArray resultArray = resultJSON.getJSONArray("result");

                    for(int i = 0 ; i < resultArray.length() ; i ++ ) {
                        JSONObject dataBahanPokok = (JSONObject) resultArray.get(i);
                        BahanPokok bahanPokok = new BahanPokok();
                        bahanPokok.setIdBahanPokok(dataBahanPokok.getString("bahan_pokok_id"));
                        bahanPokok.setNamaBahanPokok(dataBahanPokok.getString("nama"));
                        bahanPokok.setSatuanBahanPokok(dataBahanPokok.getString("satuan"));
                        mBahanPokok.add(bahanPokok);
                    }

                    Intent intent = new Intent(v.getContext(), MakananEntryActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("screenState", MyConstants.TAMBAH_MAKANAN);
                    bundle.putSerializable("daftarBahanPokok", mBahanPokok);
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        });

        mRecyclerView = findViewById(R.id.rvMakanan);
        ButterKnife.bind(this);
        setUp();
    }

    private void setUp() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mMakananAdapter = new MakananAdapter(new ArrayList<>());

        prepareDataMakanan();
    }

    private void prepareDataMakanan() {
        CommonUtils.showLoading(MakananActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(this);

        Map<String, String> params = new HashMap<>();

        volleyAPI.getRequest("getSemuaMakanan", params, result -> {
            try {
                ArrayList<Makanan> mMakanan = new ArrayList<>();
                JSONObject resultJSON = new JSONObject(result);
                JSONArray resultArray = resultJSON.getJSONArray("result");

                for(int i = 0 ; i < resultArray.length() ; i ++ ) {
                    JSONObject dataMakanan = (JSONObject) resultArray.get(i);
                    Makanan makanan = new Makanan();
                    makanan.setId(String.valueOf(i+1));
                    makanan.setIdMakanan(dataMakanan.getString("makanan_id"));
                    makanan.setNamaMakanan(dataMakanan.getString("nama"));
                    makanan.setHargaMakanan(dataMakanan.getString("harga_jual"));
                    makanan.setTanggalTambahMakanan(dataMakanan.getString("created_at"));
                    makanan.setTanggalUbahMakanan(dataMakanan.getString("updated_at"));

                    mMakanan.add(makanan);
                }

                mMakananAdapter.addItems(mMakanan);
                mRecyclerView.setAdapter(mMakananAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            CommonUtils.hideLoading();
        });
    }
}
