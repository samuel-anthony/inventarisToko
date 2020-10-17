package com.example.inventaristoko.Screens.Pengunjung;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.inventaristoko.Adapter.Pengunjung.PengunjungMakananAdapter;
import com.example.inventaristoko.Model.Makanan.Makanan;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Screens.Front.HomeActivity;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.VolleyAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PengunjungActivity extends AppCompatActivity {
    private Context appContext;
    private Toolbar toolbarPengunjung;
    private TextView tvNamaMejaPengunjung;
    private RecyclerView rvDataPengunjung;
    private PengunjungMakananAdapter pengunjungMakananAdapter;
    private long backPressedTime;

    private void init() {
        appContext = getApplicationContext();
        toolbarPengunjung = findViewById(R.id.toolbarPengunjung);
        tvNamaMejaPengunjung = findViewById(R.id.tvNamaMejaPengunjung);
        rvDataPengunjung = findViewById(R.id.rvDataPengunjung);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengunjung);

        init();

        setSupportActionBar(toolbarPengunjung);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        tvNamaMejaPengunjung.setText(bundle.getString("namaMeja"));

        setUp();
    }

    private void setUp() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvDataPengunjung.setLayoutManager(mLayoutManager);
        rvDataPengunjung.setItemAnimator(new DefaultItemAnimator());
        pengunjungMakananAdapter = new PengunjungMakananAdapter(new ArrayList<>());

        callDataMakananRequest();
    }

    private void callDataMakananRequest() {
        CommonUtils.showLoading(PengunjungActivity.this);
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

                pengunjungMakananAdapter.addItems(mMakanan);
                rvDataPengunjung.setAdapter(pengunjungMakananAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        CommonUtils.hideLoading();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.string.tombol_history :
//                Intent intent = new Intent(getApplicationContext(), PengunjungHistoryActivity.class);
//                startActivityForResult(intent, 0);

                CommonUtils.showToast(appContext, "Menu Riwayat");
                break;
            case R.string.tombol_notification :
//                intent = new Intent(getApplicationContext(), PengunjungDaftarActivity.class);
//                startActivityForResult(intent, 0);

                CommonUtils.showToast(appContext, "Menu Daftar");
                break;
            case R.string.tombol_keluar :
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.confirmation_dialog_logout);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.label_yes, (dialog, which) -> {
                    CommonUtils.showLoading(PengunjungActivity.this);
                    startActivity(new Intent(getBaseContext(), HomeActivity.class));
                    finish();
                    CommonUtils.hideLoading();
                });
                builder.setNegativeButton(R.string.label_no, (dialog, which) -> {
                });
                builder.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_menu_pengunjung, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            CommonUtils.showToast(appContext, appContext.getString(R.string.press_once_again));
        }

        backPressedTime = System.currentTimeMillis();
    }
}
