package com.example.inventaristoko.Screens.BahanPokok;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventaristoko.Adapter.BahanPokok.BahanPokokPemasokAdapter;
import com.example.inventaristoko.Model.BahanPokok.BahanPokokPemasok;
import com.example.inventaristoko.Model.BahanPokok.Pemasok;
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


public class BahanPokokEntryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private Context appContext;
    private RecyclerView rvBahanPokokPemasok;
    private BahanPokokPemasokAdapter bahanPokokPemasokAdapter;
    private ArrayList<BahanPokokPemasok> mBahanPokokPemasok = new ArrayList<>();
    private ArrayList<Pemasok> mPemasok = new ArrayList<>();
    private ArrayList<Pemasok> mPemasokResponse = new ArrayList<>();
    private ArrayList<Pemasok> mPemasokSelected = new ArrayList<>();
    private ArrayList<Pemasok> mSpnPemasok = new ArrayList<>();
    private Button btnKirimBahanPokok, btnTambahPemasokBahanPokok;
    private EditText etNamaBahanPokok, etJumlahBahanPokok, etHargaBahanPokok;
    private Spinner spnSatuanBahanPokok, spnDaftarPemasok;
    private TextView tvDaftarPemasokBahanPokok, tvHargaBahanPokok, tvSatuanBahanPokok, tvSatuanBahanPokokLabel;
    private String[] spnSatuan = {"Mg", "Dg", "Cg", "G", "Hg", "Dg", "Kg"};
    private String satuan, satuanSelected;
    private String idPemasokSelected, idBahanPokok, namaPemasokSelected, alamatPemasokSelected, nomorTeleponPemasokSelected, screenState, txtNamaBahanPokok, txtJumlahBahanPokok, txtHargaBahanPokok;

    private void init() {
        appContext = getApplicationContext();
        rvBahanPokokPemasok = findViewById(R.id.rvBahanPokokPemasok);
        etNamaBahanPokok = findViewById(R.id.etNamaBahanPokok);
        etHargaBahanPokok = findViewById(R.id.etHargaBahanPokok);
        etJumlahBahanPokok = findViewById(R.id.etJumlahBahanPokok);
        tvSatuanBahanPokok = findViewById(R.id.tvSatuanBahanPokok);
        tvSatuanBahanPokokLabel = findViewById(R.id.tvSatuanBahanPokokLabel);
        tvHargaBahanPokok = findViewById(R.id.tvHargaBahanPokok);
        tvDaftarPemasokBahanPokok = findViewById(R.id.tvDaftarPemasokBahanPokok);
        spnDaftarPemasok = findViewById(R.id.spnDaftarPemasok);
        spnSatuanBahanPokok = findViewById(R.id.spnSatuanBahanPokok);
        btnTambahPemasokBahanPokok = findViewById(R.id.btnTambahPemasokBahanPokok);
        btnKirimBahanPokok = findViewById(R.id.btnKirimBahanPokok);

        getSemuaPemasok();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bahan_pokok_entry);

        init();

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        screenState = bundle.getString("screenState");
        idBahanPokok = bundle.getString("idBahanPokok");
        satuan = bundle.getString("satuanBahanPokok");

        spnSatuanBahanPokok.setOnItemSelectedListener(this);

        ArrayAdapter<String> satuanAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spnSatuan);
        satuanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSatuanBahanPokok.setAdapter(satuanAdapter);

        if(screenState.equals(MyConstants.TAMBAH_DETAIL_BAHAN_POKOK)) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_tambah_detail_bahan_pokok);
            etNamaBahanPokok.setText(bundle.getString("namaBahanPokok"));
            tvSatuanBahanPokokLabel.setText(satuan);
            etNamaBahanPokok.setEnabled(false);
            tvSatuanBahanPokok.setVisibility(View.GONE);
            spnSatuanBahanPokok.setVisibility(View.GONE);
        } else {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_tambah_bahan_pokok);
            spnSatuanBahanPokok.setSelection(6);
            tvSatuanBahanPokokLabel.setVisibility(View.GONE);
            spnDaftarPemasok.setVisibility(View.GONE);
            btnTambahPemasokBahanPokok.setVisibility(View.GONE);
            tvDaftarPemasokBahanPokok.setVisibility(View.GONE);
            tvHargaBahanPokok.setVisibility(View.GONE);
            etHargaBahanPokok.setVisibility(View.GONE);
        }

        btnTambahPemasokBahanPokok.setOnClickListener(this);
        btnKirimBahanPokok.setOnClickListener(this);

        etNamaBahanPokok.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                CommonUtils.hideKeyboard(appContext, v);
            }
        });

        etHargaBahanPokok.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                CommonUtils.hideKeyboard(appContext, v);
            }
        });

        etJumlahBahanPokok.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                CommonUtils.hideKeyboard(appContext, v);
            }
        });
    }

    private void getSemuaPemasok() {
        CommonUtils.showLoading(BahanPokokEntryActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(BahanPokokEntryActivity.this);

        Map<String, String> params = new HashMap<>();

        volleyAPI.getRequest(MyConstants.BAHAN_POKOK_GET_SUPPLIER_ACTION, params, result -> {
            try {
                JSONObject resultJSON = new JSONObject(result);
                JSONArray resultArray = resultJSON.getJSONArray("result");

                for(int i = 0 ; i < resultArray.length() ; i ++ ) {
                    JSONObject dataPemasok = (JSONObject) resultArray.get(i);
                    Pemasok pemasok = new Pemasok();
                    pemasok.setIdPemasok(dataPemasok.getString("supplier_id"));
                    pemasok.setNamaPemasok(dataPemasok.getString("nama"));
                    pemasok.setAlamatPemasok(dataPemasok.getString("alamat"));
                    pemasok.setNomorTeleponPemasok(dataPemasok.getString("nomor_telepon"));
                    pemasok.setTanggalTambahPemasok(dataPemasok.getString("created_at"));
                    pemasok.setTanggalUbahPemasok(dataPemasok.getString("updated_at"));
                    mPemasokResponse.add(pemasok);
                }

                mPemasok = mPemasokResponse;

                for (int i = 0; i < mPemasok.size(); i++) {
                    mSpnPemasok.add(new Pemasok(mPemasok.get(i).getIdPemasok(), mPemasok.get(i).getNamaPemasok(), mPemasok.get(i).getAlamatPemasok(), mPemasok.get(i).getNomorTeleponPemasok()));
                }

                ArrayAdapter<Pemasok> adapter = new ArrayAdapter<>(appContext, android.R.layout.simple_spinner_dropdown_item, mSpnPemasok);
                adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                spnDaftarPemasok.setAdapter(adapter);
                spnDaftarPemasok.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        idPemasokSelected = mSpnPemasok.get(position).getIdPemasok();
                        namaPemasokSelected = mSpnPemasok.get(position).getNamaPemasok();
                        alamatPemasokSelected = mSpnPemasok.get(position).getAlamatPemasok();
                        nomorTeleponPemasokSelected = mSpnPemasok.get(position).getNomorTeleponPemasok();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                rvBahanPokokPemasok.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(appContext, LinearLayoutManager.VERTICAL, false);
                rvBahanPokokPemasok.setLayoutManager(layoutManager);
                bahanPokokPemasokAdapter = new BahanPokokPemasokAdapter(appContext, mBahanPokokPemasok, (bahanPokokPemasok, pos) -> {
                });
                rvBahanPokokPemasok.setAdapter(bahanPokokPemasokAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        CommonUtils.hideLoading();
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View v, int position, long id) {
        satuanSelected = spnSatuan[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTambahPemasokBahanPokok: {
                if (mBahanPokokPemasok.size() == 1) {
                    CommonUtils.showToast(appContext, appContext.getString(R.string.label_data_hanya_satu));
                    return;
                }

                insertMethod(idPemasokSelected, namaPemasokSelected);
                CommonUtils.hideKeyboard(appContext, v);
            }
            break;
            case R.id.btnKirimBahanPokok: {
                txtNamaBahanPokok = etNamaBahanPokok.getText().toString();
                txtJumlahBahanPokok = etJumlahBahanPokok.getText().toString();
                txtHargaBahanPokok = etHargaBahanPokok.getText().toString();

                if(txtNamaBahanPokok.isEmpty() || txtJumlahBahanPokok.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.label_data_kosong, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (screenState.equals(MyConstants.TAMBAH_DETAIL_BAHAN_POKOK)) {
                    if (txtHargaBahanPokok.isEmpty() || mBahanPokokPemasok.size() < 1) {
                        Toast.makeText(getApplicationContext(), R.string.label_data_kosong, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage(R.string.confirmation_dialog_submit);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.label_yes, (dialog, which) -> callSubmitDataBahanPokokRequest(v.getContext()));
                builder.setNegativeButton(R.string.label_no, (dialog, which) -> {
                });
                builder.show();
            }
            break;
        }
    }

    private void callSubmitDataBahanPokokRequest(Context context) {
        CommonUtils.showLoading(context);
        VolleyAPI volleyAPI = new VolleyAPI(context);

        Map<String, String> params = new HashMap<>();
        params.put("nama", txtNamaBahanPokok);
        params.put("jumlah", txtJumlahBahanPokok);

        if (screenState.equals(MyConstants.TAMBAH_DETAIL_BAHAN_POKOK)) {
            params.put("bahan_pokok_id", idBahanPokok);
            params.put("harga", txtHargaBahanPokok);
            params.put("supplier_id", idPemasokSelected);
            params.put("nama_supplier", namaPemasokSelected);
            params.put("alamat_supplier", alamatPemasokSelected);
            params.put("nomor_telepon_supplier", nomorTeleponPemasokSelected);
            params.put("aksi", "1");
        } else {
            params.put("satuan", satuanSelected);
        }

        if (screenState.equals(MyConstants.TAMBAH_BAHAN_POKOK)) {
            volleyAPI.postRequest(MyConstants.BAHAN_POKOK_ADD_ACTION, params, result -> {
                try {
                    JSONObject resultJSON = new JSONObject(result);
                    Intent myIntent = new Intent(getApplicationContext(), BahanPokokActivity.class);
                    startActivityForResult(myIntent, 0);

                    CommonUtils.showToast(context, resultJSON.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        } else if (screenState.equals(MyConstants.TAMBAH_DETAIL_BAHAN_POKOK)) {
            volleyAPI.postRequest(MyConstants.BAHAN_POKOK_ADD_HISTORY_ACTION, params, result -> {
                try {
                    JSONObject resultJSON = new JSONObject(result);
                    Intent myIntent = new Intent(getApplicationContext(), BahanPokokActivity.class);
                    startActivityForResult(myIntent, 0);
                    Toast.makeText(getApplicationContext(), resultJSON.getString("message"), Toast.LENGTH_SHORT).show();
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
            jsonObject.put("supplier_id", id);
            jsonObject.put("name", name);

            BahanPokokPemasok bahanPokokPemasok = gson.fromJson(String.valueOf(jsonObject), BahanPokokPemasok.class);
            mBahanPokokPemasok.add(bahanPokokPemasok);
            bahanPokokPemasokAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}