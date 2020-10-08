package com.example.inventaristoko.Screens.Penjualan;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventaristoko.Adapter.Penjualan.PenjualanAdapter;
import com.example.inventaristoko.Model.Penjualan.Penjualan;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Screens.BahanPokok.BahanPokokActivity;
import com.example.inventaristoko.Screens.Front.ChangePasswordActivity;
import com.example.inventaristoko.Screens.Front.LoginActivity;
import com.example.inventaristoko.Screens.Kategori.KategoriActivity;
import com.example.inventaristoko.Screens.Makanan.MakananActivity;
import com.example.inventaristoko.Screens.Meja.MejaActivity;
import com.example.inventaristoko.Screens.Pengguna.PenggunaActivity;
import com.example.inventaristoko.Screens.Resi.ResiActivity;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.PDFDownload;
import com.example.inventaristoko.Utils.Preferences;
import com.example.inventaristoko.Utils.VolleyAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class PenjualanActivity extends AppCompatActivity implements View.OnClickListener {
    private FloatingActionButton fabDataPenjualan;
    private Context appContext;
    private Toolbar toolbar;
    private RecyclerView rvDataPenjualan;
    private PenjualanAdapter penjualanAdapter;
    private TextView tvTanggalPenjualan;
    private long backPressedTime;
    private JSONArray elementDownload = new JSONArray();

    private void init() {
        appContext = getApplicationContext();
        toolbar = findViewById(R.id.toolbar);
        fabDataPenjualan = findViewById(R.id.fabDataPenjualan);
        tvTanggalPenjualan = findViewById(R.id.tvTanggalPenjualan);
        rvDataPenjualan = findViewById(R.id.rvDataPenjualan);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjualan);

        init();

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Selamat Datang, " + Preferences.getLoggedInUser(getBaseContext()));

        tvTanggalPenjualan.setText(CommonUtils.dateFormat());

        fabDataPenjualan.setOnClickListener(this);

        setUp();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setUp() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvDataPenjualan.setLayoutManager(mLayoutManager);
        rvDataPenjualan.setItemAnimator(new DefaultItemAnimator());
        penjualanAdapter = new PenjualanAdapter(new ArrayList<>());

        callDataPenjualanRequest();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void callDataPenjualanRequest() {
        CommonUtils.showLoading(PenjualanActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(this);

        Map<String, String> params = new HashMap<>();

        volleyAPI.getRequest(MyConstants.PENJUALAN_GET_ACTION, params, result -> {
            try {
                ArrayList<Penjualan> mPenjualan = new ArrayList<>();
                JSONObject resultJSON = new JSONObject(result);
                JSONArray resultArray = resultJSON.getJSONArray("result");

                for(int i = 0 ; i < resultArray.length() ; i ++ ) {
                    JSONObject dataPenjualan = (JSONObject) resultArray.get(i);
                    JSONObject elementToDownload = new JSONObject();
                    elementToDownload.put("number",i+1);
                    elementToDownload.put("ref_no",dataPenjualan.getString("ref_no"));
                    elementToDownload.put("total_harga",dataPenjualan.getString("total_harga"));
                    elementDownload.put(elementToDownload);
                    Penjualan penjualan = new Penjualan();
                    penjualan.setId(String.valueOf(i+1));
                    penjualan.setIdPenjualan(dataPenjualan.getString("ref_no"));
                    penjualan.setKodeStatusPenjualan(dataPenjualan.getString("status_code"));
                    penjualan.setTotalHargaPenjualan(dataPenjualan.getString("total_harga"));
                    penjualan.setTanggalTambahPenjualan(CommonUtils.dateFormat(dataPenjualan.getString("created_at")));
                    penjualan.setTanggalUbahPenjualan(dataPenjualan.getString("updated_at"));
                    mPenjualan.add(penjualan);
                }

                penjualanAdapter.addItems(mPenjualan);
                rvDataPenjualan.setAdapter(penjualanAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        CommonUtils.hideLoading();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fabDataPenjualan) {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage(R.string.confirmation_dialog_download);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.label_yes, (dialog, which) -> {
                PDFDownload pdf = new PDFDownload("Penjualan On Going");

                List<String> columnName = new ArrayList<>();
                columnName.add("number");
                columnName.add("ref no");
                columnName.add("total harga");

                List<String> key = new ArrayList<>();
                key.add("number");
                key.add("ref_no");
                key.add("total_harga");

                try {
                    pdf.download(columnName, key, elementDownload, appContext);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
            builder.setNegativeButton(R.string.label_no, (dialog, which) -> {
            });
            builder.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.string.menu_kategori :
                Intent intent = new Intent(getApplicationContext(), KategoriActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.string.menu_makanan :
                intent = new Intent(getApplicationContext(), MakananActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.string.menu_pengguna :
                intent = new Intent(getApplicationContext(), PenggunaActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.string.menu_bahan_pokok :
                intent = new Intent(getApplicationContext(), BahanPokokActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.string.menu_meja :
                intent = new Intent(getApplicationContext(), MejaActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.string.menu_password :
                intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.string.menu_receipt :
                intent = new Intent(getApplicationContext(), ResiActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.string.tombol_keluar :
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.confirmation_dialog_logout);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.label_yes, (dialog, which) -> {
                    CommonUtils.showLoading(PenjualanActivity.this);
                    Preferences.clearLoggedInUser(getBaseContext());
                    startActivity(new Intent(getBaseContext(), LoginActivity.class));
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
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        return true;
    }


    @Override
    public void onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), R.string.press_once_again, Toast.LENGTH_SHORT).show();
            CommonUtils.showToast(appContext, appContext.getString(R.string.press_once_again));
        }

        backPressedTime = System.currentTimeMillis();
    }
}
