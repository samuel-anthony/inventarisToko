package com.example.inventaristoko.Screens.BahanPokok;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.inventaristoko.Adapter.BahanPokok.BahanPokokAdapter;
import com.example.inventaristoko.Model.BahanPokok.BahanPokok;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

public class BahanPokokActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private BahanPokokAdapter mBahanPokokAdapter;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bahan_pokok);

        getSupportActionBar().setTitle(R.string.label_data_bahan_pokok);

        FloatingActionButton fab = findViewById(R.id.fabBahanPokok);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Terjadi Kesalahan!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        btnAdd = findViewById(R.id.buttonAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), BahanPokokEntryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("screenState", MyConstants.ADD_BAHAN_POKOK);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });

        mRecyclerView = findViewById(R.id.rvBahanPokok);
        ButterKnife.bind(this);
        setUp();
    }

    private void setUp() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mBahanPokokAdapter = new BahanPokokAdapter(new ArrayList<>());

        prepareDataBahanPokok();
    }

    private void prepareDataBahanPokok() {
        CommonUtils.showLoading(BahanPokokActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(this);
        Map<String, String> params = new HashMap<>();
        volleyAPI.getRequest("getSemuaBahanPokok", params, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                try {
                    ArrayList<BahanPokok> mBahanPokok = new ArrayList<>();
                    JSONObject resultJSON = new JSONObject(result);
                    JSONArray resultArray = resultJSON.getJSONArray("result");
                    for(int i = 0 ; i < resultArray.length() ; i ++ ) {
                        JSONObject dataBahanPokok = (JSONObject) resultArray.get(i);
                        BahanPokok bahanPokok = new BahanPokok();
                        bahanPokok.setId(String.valueOf(i+1));
                        bahanPokok.setStapleId(dataBahanPokok.getString("bahan_pokok_id"));
                        bahanPokok.setStapleName(dataBahanPokok.getString("nama"));
                        bahanPokok.setStapleAmount(dataBahanPokok.getString("jumlah"));
                        bahanPokok.setStapleUnit(dataBahanPokok.getString("satuan"));
                        bahanPokok.setStapleCreatedAt(dataBahanPokok.getString("created_at"));
                        bahanPokok.setStapleUpdatedAt(dataBahanPokok.getString("updated_at"));

                        mBahanPokok.add(bahanPokok);
                    }
                    mBahanPokokAdapter.addItems(mBahanPokok);
                    mRecyclerView.setAdapter(mBahanPokokAdapter);
                    CommonUtils.hideLoading();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}