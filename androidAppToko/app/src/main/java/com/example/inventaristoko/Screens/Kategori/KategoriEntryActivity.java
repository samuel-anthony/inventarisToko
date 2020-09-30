package com.example.inventaristoko.Screens.Kategori;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
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
import com.example.inventaristoko.Model.Kategori.MakananKategori;
import com.example.inventaristoko.Model.Makanan.Makanan;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.VolleyAPI;
import com.example.inventaristoko.Utils.VolleyCallback;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KategoriEntryActivity extends AppCompatActivity implements View.OnClickListener  {
    private ArrayList<MakananKategori> makananKategories = new ArrayList<>();
    private ArrayList<Makanan> mMakanan = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private KategoriMakananAdapter kategoriMakananAdapter;
    private Button btnTambahMakananKategori, btnKirimKategori;
    private EditText etNamaKategori;
    private int position;
    private String screenState;
    private Spinner spnDaftarMakanan;
    private String idMakananSelected, makananSelected;

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
        mMakanan = (ArrayList<Makanan>) bundle.getSerializable("daftarMakanan");

        spnDaftarMakanan = findViewById(R.id.spnDaftarMakanan);


        ArrayList<Makanan> makanans = new ArrayList<>();
        for (int i = 0 ; i < mMakanan.size() ; i++) {
            makanans.add(new Makanan(mMakanan.get(i).getIdMakanan(), mMakanan.get(i).getNamaMakanan()));
        }

        ArrayAdapter<Makanan> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, makanans);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spnDaftarMakanan.setAdapter(adapter);
        spnDaftarMakanan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                makananSelected = makanans.get(position).getNamaMakanan();
                idMakananSelected = mMakanan.get(position).getIdMakanan();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if(screenState.equals(MyConstants.UBAH_KATEGORI)) {
            getSupportActionBar().setTitle(R.string.menu_ubah_kategori);
        } else {
            spnDaftarMakanan.setSelection(0);
            getSupportActionBar().setTitle(R.string.menu_tambah_kategori);
        }

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        kategoriMakananAdapter = new KategoriMakananAdapter(getApplicationContext(), makananKategories,
                new KategoriMakananAdapter.Onclick() {
                    @Override
                    public void onEvent(MakananKategori makananKategori, int pos) {
                        position = pos;
                    }
                });
        mRecyclerView.setAdapter(kategoriMakananAdapter);
        btnTambahMakananKategori.setOnClickListener(this);
        btnKirimKategori.setOnClickListener(this);

        etNamaKategori.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnTambahMakananKategori: {
                if(makananKategories.size() > mMakanan.size()-1) {
                    Toast.makeText(getApplicationContext(),"MELAMPAUI BATAS JANCUK", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    insertMethod(idMakananSelected, makananSelected);
                }
            }
            break;
            case R.id.btnKirimKategori: {
                if(makananKategories.size() < 1) {
                    Toast.makeText(getApplicationContext(), R.string.label_data_kosong, Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(KategoriEntryActivity.this);
                    builder.setMessage("Anda Yakin Ingin Kirim Data ini?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CommonUtils.showLoading(KategoriEntryActivity.this);
                            VolleyAPI volleyAPI = new VolleyAPI(KategoriEntryActivity.this);
                            JSONArray jsonArray = new JSONArray();
                            for(int i = 0; i < makananKategories.size() ; i++){
                                try {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("makanan_id",makananKategories.get(i).makanan_id);
                                    jsonArray.put(jsonObject);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            Map<String, String> params = new HashMap<>();
                            params.put("nama", etNamaKategori.getText().toString());
                            params.put("makanans", jsonArray.toString());

                            if (screenState.equals(MyConstants.UBAH_KATEGORI)) {
                                params.put("jenis_menu_id", "");
                            }

                            if (screenState.equals(MyConstants.UBAH_KATEGORI)) {
                                volleyAPI.putRequest("updateJenisMenu", params, new VolleyCallback() {
                                    @Override
                                    public void onSuccessResponse(String result) {
                                        try {
                                            JSONObject resultJSON = new JSONObject(result);
                                            Intent myIntent = new Intent(getApplicationContext(), KategoriActivity.class);
                                            startActivityForResult(myIntent, 0);
                                            Toast.makeText(getApplicationContext(), resultJSON.getString("message"), Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } else if (screenState.equals(MyConstants.TAMBAH_KATEGORI)) {
                                volleyAPI.postRequest("addJenisMenu", params, new VolleyCallback() {
                                    @Override
                                    public void onSuccessResponse(String result) {
                                        try {
                                            JSONObject resultJSON = new JSONObject(result);
                                            Intent myIntent = new Intent(getApplicationContext(), KategoriActivity.class);
                                            startActivityForResult(myIntent, 0);
                                            Toast.makeText(getApplicationContext(), resultJSON.getString("message"), Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }
                    });
                    builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
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
            MakananKategori makananKategori = gson.fromJson(String.valueOf(jsonObject), MakananKategori.class);
            makananKategories.add(makananKategori);
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
