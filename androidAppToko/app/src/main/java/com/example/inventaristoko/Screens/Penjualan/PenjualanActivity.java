package com.example.inventaristoko.Screens.Penjualan;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
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
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.PDFDownload;
import com.example.inventaristoko.Utils.Preferences;
import com.example.inventaristoko.Utils.VolleyAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;


public class PenjualanActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private PenjualanAdapter mPenjualanAdapter;
    private TextView tvTanggalPenjualan;
    private long backPressedTime;
    JSONArray elementDownload = new JSONArray();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjualan);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Selamat Datang, " + Preferences.getLoggedInUser(getBaseContext()));

        FloatingActionButton fab = findViewById(R.id.fabDataPenjualan);
//        fab.setOnClickListener(view -> Snackbar.make(view, "Terjadi Kesalahan!", Snackbar.LENGTH_LONG).setAction("Action", null).show());

        tvTanggalPenjualan = findViewById(R.id.tvTanggalPenjualan);
        tvTanggalPenjualan.setText(CommonUtils.dateFormat());

        mRecyclerView = findViewById(R.id.rvDataPenjualan);
        ButterKnife.bind(this);
        setUp();
    }

    private void setUp() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mPenjualanAdapter = new PenjualanAdapter(new ArrayList<>());

        prepareDataPenjualan();
    }

    private void prepareDataPenjualan() {
        CommonUtils.showLoading(PenjualanActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(this);

        Map<String, String> params = new HashMap<>();

        volleyAPI.getRequest("getPesananBelumSelesai", params, result -> {
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
                    penjualan.setTanggalTambahPenjualan(dataPenjualan.getString("created_at"));
                    penjualan.setTanggalUbahPenjualan(dataPenjualan.getString("updated_at"));
                    mPenjualan.add(penjualan);
                }

                mPenjualanAdapter.addItems(mPenjualan);
                mRecyclerView.setAdapter(mPenjualanAdapter);
                CommonUtils.hideLoading();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.string.tombol_keluar) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Anda Yakin Ingin Keluar?");
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
        } else if (item.getItemId() == R.string.menu_kategori) {
            Intent myIntent = new Intent(getApplicationContext(), KategoriActivity.class);
            startActivityForResult(myIntent, 0);
        } else if (item.getItemId() == R.string.menu_makanan) {
            Intent myIntent = new Intent(getApplicationContext(), MakananActivity.class);
            startActivityForResult(myIntent, 0);
        } else if (item.getItemId() == R.string.menu_pengguna) {
            Intent myIntent = new Intent(getApplicationContext(), PenggunaActivity.class);
            startActivityForResult(myIntent, 0);
        } else if (item.getItemId() == R.string.menu_bahan_pokok) {
            Intent myIntent = new Intent(getApplicationContext(), BahanPokokActivity.class);
            startActivityForResult(myIntent, 0);
        } else if (item.getItemId() == R.string.menu_meja) {
            Intent myIntent = new Intent(getApplicationContext(), MejaActivity.class);
            startActivityForResult(myIntent, 0);
        } else if (item.getItemId() == R.string.menu_password) {
            Intent myIntent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
            startActivityForResult(myIntent, 0);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        return true;
    }

    public void onclickPDF(View view){
        PDFDownload pdf = new PDFDownload();
        List<String> columnName = new ArrayList<>();
        columnName.add("number");
        columnName.add("ref no");
        columnName.add("total harga");
        List<String> key = new ArrayList<>();
        key.add("number");
        key.add("ref_no");
        key.add("total_harga");
        try {
            pdf.download(columnName,key,elementDownload);
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), R.string.press_once_again, Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();
    }
}
