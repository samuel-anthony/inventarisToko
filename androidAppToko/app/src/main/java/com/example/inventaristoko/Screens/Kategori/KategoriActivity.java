package com.example.inventaristoko.Screens.Kategori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.inventaristoko.Adapter.Kategori.KategoriAdapter;
import com.example.inventaristoko.Model.Kategori.Kategori;
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

public class KategoriActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private KategoriAdapter mKategoriAdapter;
    private Button btnTambahKategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori);

        getSupportActionBar().setTitle(R.string.menu_kategori);

        FloatingActionButton fab = findViewById(R.id.fabKategori);
        fab.setOnClickListener(view -> Snackbar.make(view, "Terjadi Kesalahan!", Snackbar.LENGTH_LONG).setAction("Action", null).show());

        btnTambahKategori = findViewById(R.id.btnTambahKategori);
        btnTambahKategori.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), KategoriEntryActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("screenState", MyConstants.TAMBAH_KATEGORI);
            intent.putExtras(bundle);
            v.getContext().startActivity(intent);
        });

        mRecyclerView = findViewById(R.id.rvKategori);
        ButterKnife.bind(this);
        setUp();
    }

    private void setUp() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mKategoriAdapter = new KategoriAdapter(new ArrayList<>());

        prepareDataKategori();
    }

    private void prepareDataKategori() {
        CommonUtils.showLoading(KategoriActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(this);
        Map<String, String> params = new HashMap<>();
        volleyAPI.getRequest("getSemuaJenisMenu", params, result -> {
            try {
                ArrayList<Kategori> mKategori = new ArrayList<>();
                JSONObject resultJSON = new JSONObject(result);
                JSONArray resultArray = resultJSON.getJSONArray("result");

                for(int i = 0 ; i < resultArray.length() ; i ++ ) {
                    JSONObject dataKategori = (JSONObject) resultArray.get(i);
                    Kategori kategori = new Kategori();
                    kategori.setId(String.valueOf(i+1));
                    kategori.setIdKategori(dataKategori.getString("jenis_menu_id"));
                    kategori.setNamaKategori(dataKategori.getString("nama"));
                    kategori.setTanggalTambahKategori(dataKategori.getString("created_at"));
                    kategori.setTanggalUbahKategori(dataKategori.getString("updated_at"));
                    mKategori.add(kategori);
                }

                mKategoriAdapter.addItems(mKategori);
                mRecyclerView.setAdapter(mKategoriAdapter);
                CommonUtils.hideLoading();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }
}
