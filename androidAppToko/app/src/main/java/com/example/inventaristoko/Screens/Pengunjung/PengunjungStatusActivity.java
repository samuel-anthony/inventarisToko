package com.example.inventaristoko.Screens.Pengunjung;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.inventaristoko.Adapter.Pengunjung.PengunjungStatusAdapter;
import com.example.inventaristoko.Model.Penjualan.Penjualan;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.Preferences;
import com.example.inventaristoko.Utils.VolleyAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PengunjungStatusActivity extends AppCompatActivity {
    private RecyclerView rvDataPengunjungStatusPesanan;
    private PengunjungStatusAdapter pengunjungStatusAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengunjung_status);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_status);

        rvDataPengunjungStatusPesanan = findViewById(R.id.rvDataPengunjungStatusPesanan);

        setUp();
    }

    private void setUp() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvDataPengunjungStatusPesanan.setLayoutManager(mLayoutManager);
        rvDataPengunjungStatusPesanan.setItemAnimator(new DefaultItemAnimator());
        pengunjungStatusAdapter = new PengunjungStatusAdapter(new ArrayList<>());

        callDataPengunjungStatusRequest();
    }

    private void callDataPengunjungStatusRequest() {
        CommonUtils.showLoading(PengunjungStatusActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(PengunjungStatusActivity.this);

        Map<String, String> params = new HashMap<>();
        params.put("user_id", Preferences.getLoggedInUserCustomer(getBaseContext()));

        volleyAPI.getRequest(MyConstants.PENGUNJUNG_GET_STATUS_ACTION, params, result -> {
            try {
                ArrayList<Penjualan> mPengunjungStatus = new ArrayList<>();
                JSONObject resultJSON = new JSONObject(result);
                JSONArray resultArray = resultJSON.getJSONArray("result");

                for(int i = 0 ; i < resultArray.length() ; i ++ ) {
                    JSONObject dataPengunjung = (JSONObject) resultArray.get(i);
                    Penjualan pengunjung = new Penjualan();
                    pengunjung.setId(String.valueOf(i+1));
                    pengunjung.setIdPenjualan(dataPengunjung.getString("ref_no"));
                    pengunjung.setKodeStatusPenjualan(dataPengunjung.getString("status_code"));
                    pengunjung.setTotalHargaPenjualan(dataPengunjung.getString("total_harga"));
                    pengunjung.setTanggalTambahPenjualan(CommonUtils.dateFormat(dataPengunjung.getString("created_at")));
                    mPengunjungStatus.add(pengunjung);
                }

                pengunjungStatusAdapter.addItems(mPengunjungStatus);
                rvDataPengunjungStatusPesanan.setAdapter(pengunjungStatusAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        CommonUtils.hideLoading();
    }
}