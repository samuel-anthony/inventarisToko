package com.example.inventaristoko.Screens.Kategori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.inventaristoko.Adapter.Kategori.KategoriAdapter;
import com.example.inventaristoko.Model.Kategori.Kategori;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.VolleyAPI;
import com.example.inventaristoko.Utils.VolleyCallback;
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Terjadi Kesalahan!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        btnTambahKategori = findViewById(R.id.btnTambahKategori);
        btnTambahKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent (v.getContext(), KategoriEntryActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("screenState", MyConstants.ADD_MEJA);
//                intent.putExtras(bundle);
//                v.getContext().startActivity(intent);

                Toast.makeText(getApplicationContext(), "Tambah Kategori", Toast.LENGTH_SHORT).show();
            }
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
        volleyAPI.getRequest("getSemuaJenisMenu", params, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                try {
                    ArrayList<Kategori> mKategori = new ArrayList<>();
                    JSONObject resultJSON = new JSONObject(result);
                    JSONArray resultArray = resultJSON.getJSONArray("result");
                    for(int i = 0 ; i < resultArray.length() ; i ++ ) {
                        JSONObject dataKategori = (JSONObject) resultArray.get(i);
                        Kategori kategori = new Kategori();
                        kategori.setId(String.valueOf(i+1));
                        kategori.setIdKategori(dataKategori.getString("id_kategori"));
                        kategori.setNamaKategori(dataKategori.getString("nama_kategori"));
                        kategori.setTanggalTambahKategori(dataKategori.getString("tanggal_tambah"));
                        kategori.setTanggalUbahKategori(dataKategori.getString("tanggal_ubah"));
                        mKategori.add(kategori);
                    }
                    mKategoriAdapter.addItems(mKategori);
                    mRecyclerView.setAdapter(mKategoriAdapter);
                    CommonUtils.hideLoading();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}