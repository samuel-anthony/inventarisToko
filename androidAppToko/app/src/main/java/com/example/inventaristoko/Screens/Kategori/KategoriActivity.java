package com.example.inventaristoko.Screens.Kategori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import java.util.Objects;


public class KategoriActivity extends AppCompatActivity implements View.OnClickListener {
    private FloatingActionButton fabDataKategori;
    private RecyclerView rvDataKategori;
    private KategoriAdapter kategoriAdapter;
    private Button btnTambahKategori;

    private void init() {
        rvDataKategori = findViewById(R.id.rvDataKategori);
        fabDataKategori = findViewById(R.id.fabDataKategori);
        btnTambahKategori = findViewById(R.id.btnTambahKategori);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_kategori);

        init();

        fabDataKategori.setOnClickListener(this);
        btnTambahKategori.setOnClickListener(this);

        setUp();
    }

    private void setUp() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvDataKategori.setLayoutManager(mLayoutManager);
        rvDataKategori.setItemAnimator(new DefaultItemAnimator());
        kategoriAdapter = new KategoriAdapter(new ArrayList<>());

        callDataKategoriRequest();
    }

    private void callDataKategoriRequest() {
        CommonUtils.showLoading(KategoriActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(this);

        Map<String, String> params = new HashMap<>();

        volleyAPI.getRequest(MyConstants.KATEGORI_GET_ACTION, params, result -> {
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

                kategoriAdapter.addItems(mKategori);
                rvDataKategori.setAdapter(kategoriAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        CommonUtils.hideLoading();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabDataKategori :
                Snackbar.make(v, "Terjadi Kesalahan!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                break;
            case R.id.btnTambahKategori :
                Intent intent = new Intent(v.getContext(), KategoriEntryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("screenState", MyConstants.TAMBAH_KATEGORI);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
                break;
        }
    }
}
