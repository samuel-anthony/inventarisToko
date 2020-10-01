package com.example.inventaristoko.Screens.Pengguna;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.inventaristoko.Adapter.Pengguna.PenggunaAdapter;
import com.example.inventaristoko.Model.Pengguna.Pengguna;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

public class PenggunaActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private PenggunaAdapter mPenggunaAdapter;
    private Button btnTambahPengguna;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengguna);

        getSupportActionBar().setTitle(R.string.menu_pengguna);

        FloatingActionButton fab = findViewById(R.id.fabDataPengguna);
        fab.setOnClickListener(view -> Snackbar.make(view, "Terjadi Kesalahan!", Snackbar.LENGTH_LONG).setAction("Action", null).show());

        btnTambahPengguna = findViewById(R.id.btnTambahPengguna);
        btnTambahPengguna.setOnClickListener(v -> {
            Intent intent = new Intent (v.getContext(), PenggunaEntryActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("screenState", MyConstants.TAMBAH_PENGGUNA);
            intent.putExtras(bundle);
            v.getContext().startActivity(intent);
        });

        mRecyclerView = findViewById(R.id.rvDataPengguna);
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

        volleyAPI.getRequest("getSemuaAdminUser", params, result -> {
            try {
                ArrayList<Pengguna> mPengguna = new ArrayList<>();
                JSONObject resultJSON = new JSONObject(result);
                JSONArray resultArray = resultJSON.getJSONArray("result");

                for(int i = 0 ; i < resultArray.length() ; i ++ ) {
                    JSONObject dataPengguna = (JSONObject) resultArray.get(i);
                    Pengguna pengguna = new Pengguna();
                    pengguna.setId(String.valueOf(i+1));
                    pengguna.setNamaPengguna(dataPengguna.getString("full_name"));
                    pengguna.setUsernamePengguna(dataPengguna.getString("user_name"));
                    pengguna.setEmailPengguna(dataPengguna.getString("email"));
                    pengguna.setNomorTeleponPengguna(dataPengguna.getString("phone_number"));
                    pengguna.setTanggalLahirPengguna(dataPengguna.getString("birth_date"));
                    pengguna.setTanggalTambahPengguna(dataPengguna.getString("created_at"));
                    pengguna.setTanggalUbahPengguna(dataPengguna.getString("updated_at"));

                    mPengguna.add(pengguna);
                }

                mPenggunaAdapter.addItems(mPengguna);
                mRecyclerView.setAdapter(mPenggunaAdapter);
                CommonUtils.hideLoading();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }
}
