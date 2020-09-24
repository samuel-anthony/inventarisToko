package com.example.inventaristoko.Screens.Kategori;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.inventaristoko.Adapter.Kategori.KategoriMakananAdapter;
import com.example.inventaristoko.Model.Kategori.MakananKategori;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.VolleyAPI;
import com.example.inventaristoko.Utils.VolleyCallback;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KategoriEntryActivity extends AppCompatActivity implements View.OnClickListener  {
    private ArrayList<MakananKategori> makananKategories = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private KategoriMakananAdapter kategoriMakananAdapter;
    private Button btnTambahMakananKategori, btnKirimKategori;
    private EditText etNamaMakananKategori;
    private int position;
    private String screenState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori_entry);

        Bundle bundle = getIntent().getExtras();
        screenState = bundle.getString("screenState");

        if(screenState.equals(MyConstants.UBAH_KATEGORI)) {
            getSupportActionBar().setTitle(R.string.menu_ubah_kategori);
        } else {
            getSupportActionBar().setTitle(R.string.menu_tambah_kategori);
        }

        mRecyclerView = findViewById(R.id.rvDataMakananKategori);
        btnTambahMakananKategori = findViewById(R.id.btnTambahMakananKategori);
        etNamaMakananKategori = findViewById(R.id.etNamaMakananKategori);
        btnKirimKategori = findViewById(R.id.btnKirimKategori);

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        kategoriMakananAdapter = new KategoriMakananAdapter(getApplicationContext(), makananKategories,
                new KategoriMakananAdapter.Onclick() {
                    @Override
                    public void onEvent(MakananKategori makananKategori, int pos) {
                        position = pos;
                        etNamaMakananKategori.setText(makananKategori.getName());
                    }
                });
        mRecyclerView.setAdapter(kategoriMakananAdapter);
        btnTambahMakananKategori.setOnClickListener(this);
        btnKirimKategori.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnTambahMakananKategori: {
                if(String.valueOf(etNamaMakananKategori.getText()).equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.label_makanan_kategori_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                insertMethod(String.valueOf(etNamaMakananKategori.getText()));
            }
            break;
            case R.id.btnKirimKategori: {
//                AlertDialog.Builder builder = new AlertDialog.Builder(KategoriEntryActivity.this);
//                builder.setMessage("Anda Yakin Ingin Kirim Data ini?");
//                builder.setCancelable(false);
//                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        CommonUtils.showLoading(KategoriEntryActivity.this);
//                        VolleyAPI volleyAPI = new VolleyAPI(KategoriEntryActivity.this);
//                        Map<String, String> params = new HashMap<>();
//                        params.put("nama", etNamaMakananKategori.getText().toString());
//
//                        if (screenState.equals(MyConstants.UBAH_KATEGORI)) {
//                            params.put("jenis_menu_id", "");
//                        }
//
//                        params.put("makanans", "");
//
//                        if (screenState.equals(MyConstants.UBAH_KATEGORI)) {
//                            volleyAPI.putRequest("updateJenisMenu", params, new VolleyCallback() {
//                                @Override
//                                public void onSuccessResponse(String result) {
//                                    try {
//                                        JSONObject resultJSON = new JSONObject(result);
//                                        Intent myIntent = new Intent(getApplicationContext(), KategoriActivity.class);
//                                        startActivityForResult(myIntent, 0);
//                                        Toast.makeText(getApplicationContext(), resultJSON.getString("message"), Toast.LENGTH_SHORT).show();
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            });
//                        } else if (screenState.equals(MyConstants.TAMBAH_KATEGORI)) {
//                            volleyAPI.postRequest("addJenisMenu", params, new VolleyCallback() {
//                                @Override
//                                public void onSuccessResponse(String result) {
//                                    try {
//                                        JSONObject resultJSON = new JSONObject(result);
//                                        Intent myIntent = new Intent(getApplicationContext(), KategoriActivity.class);
//                                        startActivityForResult(myIntent, 0);
//                                        Toast.makeText(getApplicationContext(), resultJSON.getString("message"), Toast.LENGTH_SHORT).show();
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            });
//                        }
//                    }
//                });
//                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                });
//                builder.show();

                Toast.makeText(getApplicationContext(), "Kirim Kategori", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    private void insertMethod(String name) {
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", name);
            MakananKategori makananKategori = gson.fromJson(String.valueOf(jsonObject), MakananKategori.class);
            makananKategories.add(makananKategori);
            kategoriMakananAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
