package com.example.inventaristoko.Screens.Penjualan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

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
import com.example.inventaristoko.Utils.Preferences;
import com.example.inventaristoko.Utils.VolleyCallback;
import com.example.inventaristoko.Utils.VolleyAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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
import java.util.Map;

import butterknife.ButterKnife;


public class PenjualanActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private PenjualanAdapter mPenjualanAdapter;
    private TextView tvTanggalPenjualan, tvTestDoang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjualan);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.menu_admin);

        FloatingActionButton fab = findViewById(R.id.fabDataPenjualan);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Terjadi Kesalahan!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

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
        volleyAPI.getRequest("getPesananBelumSelesai", params, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                try {
                    ArrayList<Penjualan> mPenjualan = new ArrayList<>();
                    JSONObject resultJSON = new JSONObject(result);
                    JSONArray resultArray = resultJSON.getJSONArray("result");
                    for(int i = 0 ; i < resultArray.length() ; i ++ ) {
                        JSONObject dataPenjualan = (JSONObject) resultArray.get(i);
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
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.string.tombol_keluar) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Anda Yakin Ingin Keluar?");
            builder.setCancelable(false);
            builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CommonUtils.showLoading(PenjualanActivity.this);
                    Preferences.clearLoggedInUser(getBaseContext());
                    startActivity(new Intent(getBaseContext(), LoginActivity.class));
                    finish();
                    CommonUtils.hideLoading();
                }
            });
            builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
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
}
