package com.example.inventaristoko.Screens;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.example.inventaristoko.Adapter.PenjualanAdapter;
import com.example.inventaristoko.Adapter.SportAdapter;
import com.example.inventaristoko.Model.Penjualan;
import com.example.inventaristoko.Model.Sport;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.DividerItemDecoration;
import com.example.inventaristoko.VolleyCallback;
import com.example.inventaristoko.volleyAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.content.ContextCompat;
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
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private RecyclerView mRecyclerView;
    private SportAdapter mSportAdapter;
    private PenjualanAdapter mPenjualanAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Tidak ada Pesan Disini", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.string.action_logout) {
            Toast.makeText(HomeActivity.this, "Logout is Successfully", Toast.LENGTH_LONG).show();
            Intent intent = new Intent (HomeActivity.this, FrontActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void setUp() {
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider_drawable);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(dividerDrawable));
        mPenjualanAdapter = new PenjualanAdapter(new ArrayList<>());

        prepareDataPenjualan();
    }

    private void prepareDataPenjualan() {
        CommonUtils.showLoading(HomeActivity.this);

        volleyAPI volleyAPI = new volleyAPI(this);
        Map<String, String> params = new HashMap<>();
        new Handler().postDelayed(() -> {
            CommonUtils.hideLoading();
            volleyAPI.getRequest("getPesananBelumSelesai", params, new VolleyCallback() {
                @Override
                public void onSuccessResponse(String result) {
                    try {
                        ArrayList<Penjualan> mPenjualan = new ArrayList<>();
                        JSONObject resultJSON = new JSONObject(result);
                        JSONArray resultArray = resultJSON.getJSONArray("result");
                        for(int i = 0 ; i < resultArray.length() ; i ++ ) {
                            JSONObject dataPenjualan = (JSONObject) resultArray.get(i);
                            String  urutanPenjualan = String.valueOf(i+1);
                            String noPenjualan = dataPenjualan.getString("ref_no");
                            String statusPenjualan = dataPenjualan.getString("status");
                            mPenjualan.add(new Penjualan(noPenjualan, statusPenjualan, urutanPenjualan));
                        }
                        mPenjualanAdapter.addItems(mPenjualan);
                        mRecyclerView.setAdapter(mPenjualanAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }, 2000);
    }
}
