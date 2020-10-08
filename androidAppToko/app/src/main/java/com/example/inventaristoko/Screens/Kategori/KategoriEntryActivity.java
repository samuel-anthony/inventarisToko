package com.example.inventaristoko.Screens.Kategori;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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
import java.util.Objects;


public class KategoriEntryActivity extends AppCompatActivity implements View.OnClickListener  {
    private Context appContext;
    private RecyclerView rvKategoriMakanan;
    private KategoriMakananAdapter kategoriMakananAdapter;
    private ArrayList<KategoriMakanan> kategoryMakanan = new ArrayList<>();
    private ArrayList<Makanan> mMakanan = new ArrayList<>();
    private ArrayList<Makanan> mMakananResponse = new ArrayList<>();
    private ArrayList<Makanan> mMakananSelected = new ArrayList<>();
    private ArrayList<Makanan> mSpnMakanan = new ArrayList<>();
    private Button btnTambahMakananKategori, btnKirimKategori;
    private EditText etNamaKategori;
    private Spinner spnDaftarMakanan;
    private String screenState, txtNamaKategori, idMakananSelected, makananSelected, oldIdKategori;

    private void init() {
        appContext = getApplicationContext();
        rvKategoriMakanan = findViewById(R.id.rvKategoriMakanan);
        etNamaKategori = findViewById((R.id.etNamaKategori));
        spnDaftarMakanan = findViewById(R.id.spnDaftarMakanan);
        btnTambahMakananKategori = findViewById(R.id.btnTambahMakananKategori);
        btnKirimKategori = findViewById(R.id.btnKirimKategori);

        getSemuaMakanan();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori_entry);

        init();

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        screenState = bundle.getString("screenState");
        oldIdKategori = bundle.getString("idKategori");

        if(screenState.equals(MyConstants.UBAH_KATEGORI)) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_ubah_kategori);
            etNamaKategori.setText(bundle.getString("namaKategori"));
        } else {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_tambah_kategori);
        }

        btnTambahMakananKategori.setOnClickListener(this);
        btnKirimKategori.setOnClickListener(this);
        
        etNamaKategori.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                CommonUtils.hideKeyboard(appContext, v);
            }
        });
    }

    private void getSemuaMakanan() {
        CommonUtils.showLoading(KategoriEntryActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(KategoriEntryActivity.this);

        Map<String, String> params = new HashMap<>();

        volleyAPI.getRequest(MyConstants.MAKANAN_GET_ACTION, params, result -> {
            try {
                JSONObject resultJSON = new JSONObject(result);
                JSONArray resultArray = resultJSON.getJSONArray("result");

                for(int i = 0 ; i < resultArray.length() ; i ++ ) {
                    JSONObject dataMakanan = (JSONObject) resultArray.get(i);
                    Makanan makanan = new Makanan();
                    makanan.setIdMakanan(dataMakanan.getString("makanan_id"));
                    makanan.setNamaMakanan(dataMakanan.getString("nama"));
                    mMakananResponse.add(makanan);
                }
                mMakanan = mMakananResponse;

                for (int i = 0; i < mMakanan.size(); i++) {
                    mSpnMakanan.add(new Makanan(mMakanan.get(i).getIdMakanan(), mMakanan.get(i).getNamaMakanan()));
                }

                ArrayAdapter<Makanan> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, mSpnMakanan);
                adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                spnDaftarMakanan.setAdapter(adapter);
                spnDaftarMakanan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        makananSelected = mSpnMakanan.get(position).getNamaMakanan();
                        idMakananSelected = mSpnMakanan.get(position).getIdMakanan();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                rvKategoriMakanan.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                rvKategoriMakanan.setLayoutManager(layoutManager);
                kategoriMakananAdapter = new KategoriMakananAdapter(getApplicationContext(), kategoryMakanan, (kategoriMakanan, pos) -> {
                });
                rvKategoriMakanan.setAdapter(kategoriMakananAdapter);

                if(screenState.equals(MyConstants.UBAH_KATEGORI)) {
                    Bundle bundle = getIntent().getExtras();
                    mMakananSelected = (ArrayList<Makanan>) bundle.getSerializable("daftarMakananSelected");

                    for (int i = 0; i < mMakananSelected.size(); i++) {
                        insertMethod(mMakananSelected.get(i).getIdMakanan(), mMakananSelected.get(i).getNamaMakanan());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        CommonUtils.hideLoading();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTambahMakananKategori: {
                if (kategoryMakanan.size() > mMakanan.size() - 1) {
                    CommonUtils.showToast(appContext, appContext.getString(R.string.label_data_melampaui));
                    return;
                }

                for(int i = 0 ; i < kategoryMakanan.size() ; i++) {
                    if(String.valueOf(kategoryMakanan.get(i).getMakanan_id()).equals(idMakananSelected)) {
                        CommonUtils.showToast(appContext, appContext.getString(R.string.label_data_sama));
                        return;
                    }
                }
                
                insertMethod(idMakananSelected, makananSelected);
                CommonUtils.hideKeyboard(appContext, v);
            }
            break;
            case R.id.btnKirimKategori: {
                txtNamaKategori = etNamaKategori.getText().toString();

                if(txtNamaKategori.isEmpty() || kategoryMakanan.size() < 1) {
                    CommonUtils.showToast(appContext, appContext.getString(R.string.label_data_kosong));
                    return;
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage(R.string.confirmation_dialog_submit);
                    builder.setCancelable(false);
                    builder.setPositiveButton(R.string.label_yes, (dialog, which) -> callSubmitDataKategoriRequest(v.getContext()));
                    builder.setNegativeButton(R.string.label_no, (dialog, which) -> {
                    });
                    builder.show();
                }
            }
            break;
        }
    }

    private void callSubmitDataKategoriRequest(Context context) {
        CommonUtils.showLoading(context);
        VolleyAPI volleyAPI = new VolleyAPI(context);
        JSONArray jsonArray = new JSONArray();

        for(int i = 0; i < kategoryMakanan.size() ; i++){
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("makanan_id", kategoryMakanan.get(i).makanan_id);
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Map<String, String> params = new HashMap<>();
        params.put("nama", txtNamaKategori);
        params.put("makanans", jsonArray.toString());

        if (screenState.equals(MyConstants.UBAH_KATEGORI)) {
            params.put("jenis_menu_id", oldIdKategori);
        }

        if (screenState.equals(MyConstants.UBAH_KATEGORI)) {
            volleyAPI.putRequest(MyConstants.KATEGORI_EDIT_ACTION, params, result -> {
                try {
                    JSONObject resultJSON = new JSONObject(result);
                    Intent myIntent = new Intent(context, KategoriActivity.class);
                    startActivityForResult(myIntent, 0);

                    CommonUtils.showToast(context, resultJSON.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        } else if (screenState.equals(MyConstants.TAMBAH_KATEGORI)) {
            volleyAPI.postRequest(MyConstants.KATEGORI_ADD_ACTION, params, result -> {
                try {
                    JSONObject resultJSON = new JSONObject(result);
                    Intent myIntent = new Intent(context, KategoriActivity.class);
                    startActivityForResult(myIntent, 0);

                    CommonUtils.showToast(context, resultJSON.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }

        CommonUtils.hideLoading();
    }

    private void insertMethod(String id, String name) {
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("makanan_id", id);
            jsonObject.put("name", name);

            KategoriMakanan kategoriMakanan = gson.fromJson(String.valueOf(jsonObject), KategoriMakanan.class);
            kategoryMakanan.add(kategoriMakanan);
            kategoriMakananAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
