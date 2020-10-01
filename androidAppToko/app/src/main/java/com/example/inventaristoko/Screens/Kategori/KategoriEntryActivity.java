package com.example.inventaristoko.Screens.Kategori;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.inventaristoko.Adapter.Kategori.KategoriMakananAdapter;
import com.example.inventaristoko.Model.Kategori.KategoriMakanan;
import com.example.inventaristoko.Model.Makanan.Makanan;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.VolleyAPI;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KategoriEntryActivity extends AppCompatActivity implements View.OnClickListener  {
    private ArrayList<KategoriMakanan> kategoryMakanans = new ArrayList<>();
    private ArrayList<Makanan> mMakanan = new ArrayList<>();
    private ArrayList<Makanan> makanans = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private KategoriMakananAdapter kategoriMakananAdapter;
    private Button btnTambahMakananKategori, btnKirimKategori;
    private EditText etNamaKategori;
    private int position;
    private String screenState;
    private Spinner spnDaftarMakanan;
    private String idMakananSelected, makananSelected, idKategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori_entry);

        mRecyclerView = findViewById(R.id.rvDataMakananKategori);
        etNamaKategori = findViewById((R.id.etNamaKategori));
        btnTambahMakananKategori = findViewById(R.id.btnTambahMakananKategori);
        btnKirimKategori = findViewById(R.id.btnKirimKategori);

        Bundle bundle = getIntent().getExtras();
        screenState = bundle.getString("screenState");
        idKategori = bundle.getString("idKategori");

        spnDaftarMakanan = findViewById(R.id.spnDaftarMakanan);

        if(screenState.equals(MyConstants.TAMBAH_KATEGORI)) {
            mMakanan = (ArrayList<Makanan>) bundle.getSerializable("daftarMakanan");
            for (int i = 0; i < mMakanan.size(); i++) {
                makanans.add(new Makanan(mMakanan.get(i).getIdMakanan(), mMakanan.get(i).getNamaMakanan()));
            }
        }

        ArrayAdapter<Makanan> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, makanans);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spnDaftarMakanan.setAdapter(adapter);
        spnDaftarMakanan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(screenState.equals(MyConstants.TAMBAH_KATEGORI)) {
                    makananSelected = makanans.get(position).getNamaMakanan();
                    idMakananSelected = mMakanan.get(position).getIdMakanan();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if(screenState.equals(MyConstants.UBAH_KATEGORI)) {
            getSupportActionBar().setTitle(R.string.menu_ubah_kategori);
            etNamaKategori.setText(bundle.getString("namaKategori"));
        } else {
            getSupportActionBar().setTitle(R.string.menu_tambah_kategori);
            spnDaftarMakanan.setSelection(0);
        }

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        kategoriMakananAdapter = new KategoriMakananAdapter(getApplicationContext(), kategoryMakanans, (kategoriMakanan, pos) -> position = pos);
        mRecyclerView.setAdapter(kategoriMakananAdapter);
        btnTambahMakananKategori.setOnClickListener(this);
        btnKirimKategori.setOnClickListener(this);

        etNamaKategori.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTambahMakananKategori: {
                if(kategoryMakanans.size() > mMakanan.size()-1) {
                    Toast.makeText(getApplicationContext(),R.string.label_data_melampaui, Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    insertMethod(idMakananSelected, makananSelected);
                }
            }
            break;

            case R.id.btnKirimKategori: {
                if(String.valueOf(etNamaKategori.getText()).equals("") || kategoryMakanans.size() < 1) {
                    Toast.makeText(getApplicationContext(), R.string.label_data_kosong, Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage(R.string.confirmation_dialog_submit);
                    builder.setCancelable(false);
                    builder.setPositiveButton(R.string.label_yes, (dialog, which) -> {
                        CommonUtils.showLoading(v.getContext());
                        VolleyAPI volleyAPI = new VolleyAPI(v.getContext());
                        JSONArray jsonArray = new JSONArray();

                        for(int i = 0; i < kategoryMakanans.size() ; i++){
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("makanan_id", kategoryMakanans.get(i).makanan_id);
                                jsonArray.put(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        Map<String, String> params = new HashMap<>();
                        params.put("nama", etNamaKategori.getText().toString());
                        params.put("makanans", jsonArray.toString());

                        if (screenState.equals(MyConstants.UBAH_KATEGORI)) {
                            params.put("jenis_menu_id", idKategori);
                        }

                        if (screenState.equals(MyConstants.UBAH_KATEGORI)) {
                            volleyAPI.putRequest("updateJenisMenu", params, result -> {
                                try {
                                    JSONObject resultJSON = new JSONObject(result);
                                    Intent myIntent = new Intent(getApplicationContext(), KategoriActivity.class);
                                    startActivityForResult(myIntent, 0);
                                    Toast.makeText(getApplicationContext(), resultJSON.getString("message"), Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            });
                        } else if (screenState.equals(MyConstants.TAMBAH_KATEGORI)) {
                            volleyAPI.postRequest("addJenisMenu", params, result -> {
                                try {
                                    JSONObject resultJSON = new JSONObject(result);
                                    Intent myIntent = new Intent(getApplicationContext(), KategoriActivity.class);
                                    startActivityForResult(myIntent, 0);
                                    Toast.makeText(getApplicationContext(), resultJSON.getString("message"), Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                        CommonUtils.hideLoading();
                    });
                    builder.setNegativeButton(R.string.label_no, (dialog, which) -> {
                    });
                    builder.show();
                }
            }
            break;
        }
    }

    private void insertMethod(String id, String name) {
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("makanan_id", id);
            jsonObject.put("name", name);
            KategoriMakanan kategoriMakanan = gson.fromJson(String.valueOf(jsonObject), KategoriMakanan.class);
            kategoryMakanans.add(kategoriMakanan);
            kategoriMakananAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
