package com.example.inventaristoko.Screens.Penjualan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventaristoko.Adapter.Penjualan.PenjualanAdapter;
import com.example.inventaristoko.Model.Penjualan.Penjualan;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Screens.FrontActivity;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.VolleyCallback;
import com.example.inventaristoko.Utils.VolleyAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
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
    private AppBarConfiguration mAppBarConfiguration;
    private RecyclerView mRecyclerView;
    private PenjualanAdapter mPenjualanAdapter;
    private TextView tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjualan);

        tvDate = findViewById(R.id.tanggal);
        tvDate.setText(CommonUtils.dateFormat());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.admin);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Terjadi Kesalahan!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        mRecyclerView = findViewById(R.id.mRecyclerView);
        ButterKnife.bind(this);
        setUp();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.string.action_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Anda Yakin Ingin Keluar?");
            builder.setCancelable(false);
            builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CommonUtils.showLoading(PenjualanActivity.this);
                    VolleyAPI volleyAPI = new VolleyAPI(PenjualanActivity.this);
                    Map<String, String> params = new HashMap<>();
                    new Handler().postDelayed(() -> {
                        volleyAPI.getRequest("logout", params, new VolleyCallback() {
                            @Override
                            public void onSuccessResponse(String result) {
                                try {
                                    JSONObject resultJSON = new JSONObject(result);
                                    Toast.makeText(getApplicationContext(), resultJSON.getString("message"), Toast.LENGTH_SHORT).show();
                                    if (resultJSON.getString("is_error").equalsIgnoreCase("0")) {
                                        Intent myIntent = new Intent(getApplicationContext(), FrontActivity.class);
                                        startActivityForResult(myIntent, 0);
                                        Toast.makeText(getApplicationContext(), "Berhasil Keluar", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        CommonUtils.hideLoading();
                    }, 2000);
                }
            });
            builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_title, menu);
        return true;
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
                        penjualan.setOrderNo(String.valueOf(i+1));
                        penjualan.setOrderMasterId(dataPenjualan.getString("pesanan_master_id"));
                        penjualan.setRefNo(dataPenjualan.getString("ref_no"));
                        penjualan.setStatus(dataPenjualan.getString("status"));
                        penjualan.setStatusCode(dataPenjualan.getString("status_code"));
                        penjualan.setTotalPrice(dataPenjualan.getString("total_harga"));
                        penjualan.setTotalCharge(dataPenjualan.getString("total_charges"));
                        penjualan.setAmount(dataPenjualan.getString("total_pesanan"));
                        penjualan.setUserId(dataPenjualan.getString("user_id"));
                        penjualan.setCreatedAt(dataPenjualan.getString("created_at"));
                        penjualan.setUpdatedAt(dataPenjualan.getString("updated_at"));

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
}
