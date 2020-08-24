package com.example.inventaristoko.Screens.Pengguna;

import android.os.Bundle;
import android.view.View;

import com.example.inventaristoko.Adapter.Pengguna.PenggunaAdapter;
import com.example.inventaristoko.Model.Pengguna.Pengguna;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.VolleyAPI;
import com.example.inventaristoko.Utils.VolleyCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

public class PenggunaActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private PenggunaAdapter mPenggunaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengguna);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle(R.string.menu_user);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Terjadi Kesalahan!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//            }
//        });

        mRecyclerView = findViewById(R.id.rvPengguna);
        ButterKnife.bind(this);
        setUp();
    }

    private void setUp() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mPenggunaAdapter = new PenggunaAdapter(new ArrayList<>());

        prepareDataPengguna();
    }

    private void prepareDataPengguna() {
        CommonUtils.showLoading(PenggunaActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(this);
        Map<String, String> params = new HashMap<>();
        volleyAPI.getRequest("getPengguna", params, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                try {
                    ArrayList<Pengguna> mPengguna = new ArrayList<>();
                    JSONObject resultJSON = new JSONObject(result);
                    JSONArray resultArray = resultJSON.getJSONArray("result");
                    for(int i = 0 ; i < resultArray.length() ; i ++ ) {
                        JSONObject dataPengguna = (JSONObject) resultArray.get(i);
                        Pengguna pengguna = new Pengguna();
                        pengguna.setUserNo(String.valueOf(i+1));
                        pengguna.setUserName(dataPengguna.getString("user_name"));
                        pengguna.setUserId(dataPengguna.getString("user_id"));
                        pengguna.setUserLevel(dataPengguna.getString("user_level"));
                        pengguna.setCreatedAt(dataPengguna.getString("created_at"));
                        pengguna.setUpdatedAt(dataPengguna.getString("updated_at"));

                        mPengguna.add(pengguna);
                    }
                    mPenggunaAdapter.addItems(mPengguna);
                    mRecyclerView.setAdapter(mPenggunaAdapter);
                    CommonUtils.hideLoading();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
