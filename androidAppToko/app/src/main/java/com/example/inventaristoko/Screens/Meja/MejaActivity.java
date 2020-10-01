package com.example.inventaristoko.Screens.Meja;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.inventaristoko.Adapter.Meja.MejaAdapter;
import com.example.inventaristoko.Model.Meja.Meja;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.VolleyAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

public class MejaActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MejaAdapter mMejaAdapter;
    private Button btnTambahMeja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meja);

        getSupportActionBar().setTitle(R.string.menu_meja);

        FloatingActionButton fab = findViewById(R.id.fabDataMeja);
        fab.setOnClickListener(view -> Snackbar.make(view, "Terjadi Kesalahan!", Snackbar.LENGTH_LONG).setAction("Action", null).show());

        btnTambahMeja = findViewById(R.id.btnTambahMeja);
        btnTambahMeja.setOnClickListener(v -> {
            Intent intent = new Intent (v.getContext(), MejaEntryActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("screenState", MyConstants.TAMBAH_MEJA);
            intent.putExtras(bundle);
            v.getContext().startActivity(intent);
        });

        mRecyclerView = findViewById(R.id.rvDataMeja);
        ButterKnife.bind(this);
        setUp();
    }

    private void setUp() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mMejaAdapter = new MejaAdapter(new ArrayList<>());

        prepareDataMeja();
    }

    private void prepareDataMeja() {
        CommonUtils.showLoading(MejaActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(this);

        Map<String, String> params = new HashMap<>();

        volleyAPI.getRequest("getSemuaDataMeja", params, result -> {
            try {
                ArrayList<Meja> mMeja = new ArrayList<>();
                JSONObject resultJSON = new JSONObject(result);
                JSONArray resultArray = resultJSON.getJSONArray("result");

                for(int i = 0 ; i < resultArray.length() ; i ++ ) {
                    JSONObject dataMeja = (JSONObject) resultArray.get(i);
                    Meja meja = new Meja();
                    meja.setId(String.valueOf(i+1));
                    meja.setIdMeja(dataMeja.getString("user_id"));
                    meja.setNamaMeja(dataMeja.getString("full_name"));
                    meja.setTanggalTambahMeja(dataMeja.getString("created_at"));
                    meja.setTanggalUbahMeja(dataMeja.getString("updated_at"));

                    mMeja.add(meja);
                }

                mMejaAdapter.addItems(mMeja);
                mRecyclerView.setAdapter(mMejaAdapter);
                CommonUtils.hideLoading();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }
}
