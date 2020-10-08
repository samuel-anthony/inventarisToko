package com.example.inventaristoko.Screens.Makanan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.inventaristoko.Adapter.Makanan.MakananAdapter;
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
import java.util.Objects;


public class MakananActivity extends AppCompatActivity implements View.OnClickListener {
    private FloatingActionButton fabDataMakanan;
    private RecyclerView rvDataMakanan;
    private MakananAdapter makananAdapter;
    private Button btnTambahMakanan;

    private void init() {
        fabDataMakanan = findViewById(R.id.fabDataMakanan);
        rvDataMakanan = findViewById(R.id.rvDataMakanan);
        btnTambahMakanan = findViewById(R.id.btnTambahMakanan);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makanan);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_makanan);

        init();

        fabDataMakanan.setOnClickListener(this);
        btnTambahMakanan.setOnClickListener(this);

        setUp();
    }

    private void setUp() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvDataMakanan.setLayoutManager(mLayoutManager);
        rvDataMakanan.setItemAnimator(new DefaultItemAnimator());
        makananAdapter = new MakananAdapter(new ArrayList<>());

        callDataMakananRequest();
    }

    private void callDataMakananRequest() {
        CommonUtils.showLoading(MakananActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(this);

        Map<String, String> params = new HashMap<>();

        volleyAPI.getRequest(MyConstants.MAKANAN_GET_ACTION, params, result -> {
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

                makananAdapter.addItems(mMakanan);
                rvDataMakanan.setAdapter(makananAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            CommonUtils.hideLoading();
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabDataMakanan :
                Snackbar.make(v, "Terjadi Kesalahan!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                break;
            case R.id.btnTambahMakanan :
                Intent intent = new Intent(v.getContext(), MakananEntryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("screenState", MyConstants.TAMBAH_MAKANAN);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
                break;
        }
    }
}
