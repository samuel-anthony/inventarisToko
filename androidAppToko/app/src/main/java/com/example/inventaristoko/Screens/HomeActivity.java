package com.example.inventaristoko.Screens;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventaristoko.Adapter.PenjualanAdapter;
import com.example.inventaristoko.Adapter.SportAdapter;
import com.example.inventaristoko.Model.Penjualan;
import com.example.inventaristoko.Model.Sport;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.VolleyCallback;
import com.example.inventaristoko.volleyAPI;
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
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private RecyclerView mRecyclerView;
    private SportAdapter mSportAdapter;
    private PenjualanAdapter mPenjualanAdapter;
    private TextView tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
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
                    CommonUtils.showLoading(HomeActivity.this);
                    volleyAPI volleyAPI = new volleyAPI(HomeActivity.this);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    private void setUp() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mPenjualanAdapter = new PenjualanAdapter(new ArrayList<>());
//        mSportAdapter = new SportAdapter(new ArrayList<>());

//        prepareDummyData();
        prepareDataPenjualan();
    }

    private void prepareDataPenjualan() {
        CommonUtils.showLoading(HomeActivity.this);
        volleyAPI volleyAPI = new volleyAPI(this);
        Map<String, String> params = new HashMap<>();
        new Handler().postDelayed(() -> {
            volleyAPI.getRequest("getPesananBelumSelesai", params, new VolleyCallback() {
                @Override
                public void onSuccessResponse(String result) {
                    try {
                        ArrayList<Penjualan> mPenjualan = new ArrayList<>();
                        JSONObject resultJSON = new JSONObject(result);
                        JSONArray resultArray = resultJSON.getJSONArray("result");
                        for(int i = 0 ; i < resultArray.length() ; i ++ ) {
                            JSONObject dataPenjualan = (JSONObject) resultArray.get(i);
                            String urutanPenjualan = String.valueOf(i+1);
                            String noPenjualan = dataPenjualan.getString("ref_no");
                            String statusPenjualan = dataPenjualan.getString("status");
                            String statusCodePenjualan = dataPenjualan.getString("status_code");
                            String totalPenjualan = dataPenjualan.getString("total_harga");
                            String tanggalPenjualan = dataPenjualan.getString("created_at");
//                            String detailPenjualan = dataDetail.getString(0);

                            mPenjualan.add(new Penjualan(urutanPenjualan, noPenjualan, statusPenjualan, statusCodePenjualan, totalPenjualan, tanggalPenjualan));
                        }
                        mPenjualanAdapter.addItems(mPenjualan);
                        mRecyclerView.setAdapter(mPenjualanAdapter);
                        CommonUtils.hideLoading();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }, 2000);
    }

    private void prepareDummyData() {
        CommonUtils.showLoading(HomeActivity.this);
        new Handler().postDelayed(() -> {
            //prepare data and show loading
            ArrayList<Sport> mSports = new ArrayList<>();
            String[] sportsList = getResources().getStringArray(R.array.sports_titles);
            String[] sportsInfo = getResources().getStringArray(R.array.sports_info);
            String[] sportsImage = getResources().getStringArray(R.array.sports_images);
            for (int i = 0; i < sportsList.length; i++) {
                mSports.add(new Sport(sportsImage[i], sportsInfo[i], "1", sportsList[i]));
            }
            mSportAdapter.addItems(mSports);
            mRecyclerView.setAdapter(mSportAdapter);
            CommonUtils.hideLoading();
        }, 2000);
    }
}
