package com.example.inventaristoko.Screens.Resi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.inventaristoko.Adapter.Penjualan.PenjualanAdapter;
import com.example.inventaristoko.Model.Penjualan.Penjualan;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.VolleyAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ResiDaftarActivity extends AppCompatActivity {
    private RecyclerView rvDataResi;
    private PenjualanAdapter resiAdapter;
    private String txtTanggalDariResi, txtTanggalSampaiResi;

    private void init() {
        rvDataResi = findViewById(R.id.rvDataResi);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resi_daftar);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_daftar_resi);

        init();

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        txtTanggalDariResi = bundle.getString("tanggalDari");
        txtTanggalSampaiResi = bundle.getString("tanggalSampai");

        setUp();
    }


    private void setUp() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvDataResi.setLayoutManager(mLayoutManager);
        rvDataResi.setItemAnimator(new DefaultItemAnimator());
        resiAdapter = new PenjualanAdapter(new ArrayList<>());

        callCariDataResiRequest();
    }

    private void callCariDataResiRequest() {
        CommonUtils.showLoading(ResiDaftarActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(ResiDaftarActivity.this);

        Map<String, String> params = new HashMap<>();
        params.put("tanggalDari", txtTanggalDariResi);
        params.put("tanggalSampai", txtTanggalSampaiResi);

        volleyAPI.getRequest(MyConstants.RESI_GET_ACTION, params, result -> {
            try {
                ArrayList<Penjualan> mResi = new ArrayList<>();
                JSONObject resultJSON = new JSONObject(result);
                JSONArray resultArray = resultJSON.getJSONArray("result");

                for(int i = 0 ; i < resultArray.length() ; i ++ ) {
                    JSONObject dataResi = (JSONObject) resultArray.get(i);
                    Penjualan resi = new Penjualan();
                    resi.setId(String.valueOf(i+1));
                    resi.setIdPenjualan(dataResi.getString("ref_no"));
                    resi.setKodeStatusPenjualan(dataResi.getString("status_code"));
                    resi.setTotalHargaPenjualan(dataResi.getString("total_harga"));
                    resi.setTanggalTambahPenjualan(CommonUtils.dateFormat(dataResi.getString("created_at")));
                    resi.setTanggalUbahPenjualan(dataResi.getString("updated_at"));
                    mResi.add(resi);
                }

                resiAdapter.addItems(mResi);
                rvDataResi.setAdapter(resiAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        CommonUtils.hideLoading();
    }
}
