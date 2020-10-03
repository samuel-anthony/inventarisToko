package com.example.inventaristoko.Screens.BahanPokok;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.VolleyAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BahanPokokEntryActivity extends AppCompatActivity{
    private EditText etNamaBahanPokok, etJumlahBahanPokok, etHargaBahanPokok;
    private Button btnKirimBahanPokok;
    private Spinner spnSatuanBahanPokok;
    private String[] spnSatuan = {"Mg", "Dg", "Cg", "G", "Hg", "Dg", "Kg"};
    private String satuanSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bahan_pokok_entry);

        Bundle bundle = getIntent().getExtras();
        String screenState = bundle.getString("screenState");
        String satuan = bundle.getString("satuanBahanPokok");

        etNamaBahanPokok = findViewById(R.id.etNamaBahanPokok);
        etHargaBahanPokok = findViewById(R.id.etHargaBahanPokok);
        etJumlahBahanPokok = findViewById(R.id.etJumlahBahanPokok);

        spnSatuanBahanPokok = findViewById(R.id.spnSatuanBahanPokok);
        spnSatuanBahanPokok.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                satuanSelected = spnSatuan[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spnSatuan);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSatuanBahanPokok.setAdapter(adapter);

        if(screenState.equals(MyConstants.TAMBAH_DETAIL_BAHAN_POKOK)) {
            getSupportActionBar().setTitle(R.string.menu_tambah_detail_bahan_pokok);
            etNamaBahanPokok.setText(bundle.getString("namaBahanPokok"));
            etJumlahBahanPokok.setText(bundle.getString("jumlahBahanPokok"));

            if((satuan.toUpperCase()).equals((MyConstants.MILI_GRAM).toUpperCase())) {
                satuanSelected = MyConstants.MILI_GRAM;
            } else if ((satuan.toUpperCase()).equals((MyConstants.DESI_GRAM).toUpperCase())) {
                satuanSelected = MyConstants.DESI_GRAM;
            } else if ((satuan.toUpperCase()).equals((MyConstants.CENTI_GRAM).toUpperCase())) {
                satuanSelected = MyConstants.CENTI_GRAM;
            } else if ((satuan.toUpperCase()).equals((MyConstants.GRAM).toUpperCase())) {
                satuanSelected = MyConstants.GRAM;
            } else if ((satuan.toUpperCase()).equals((MyConstants.HEKTO_GRAM).toUpperCase())) {
                satuanSelected = MyConstants.HEKTO_GRAM;
            } else if ((satuan.toUpperCase()).equals((MyConstants.DEKA_GRAM).toUpperCase())) {
                satuanSelected = MyConstants.DEKA_GRAM;
            } else if ((satuan.toUpperCase()).equals((MyConstants.KILO_GRAM).toUpperCase())) {
                satuanSelected = MyConstants.KILO_GRAM;
            }

            int spinnerPosition = adapter.getPosition(satuanSelected);
            spnSatuanBahanPokok.setSelection(spinnerPosition);
        } else {
            getSupportActionBar().setTitle(R.string.menu_tambah_bahan_pokok);
            spnSatuanBahanPokok.setSelection(6);
        }

        btnKirimBahanPokok = findViewById(R.id.btnKirimBahanPokok);
        btnKirimBahanPokok.setOnClickListener(v -> {
            if(String.valueOf(etNamaBahanPokok.getText()).equals("") || String.valueOf(etHargaBahanPokok.getText()).equals("") || String.valueOf(etJumlahBahanPokok.getText()).equals("")) {
                Toast.makeText(getApplicationContext(), R.string.label_data_kosong, Toast.LENGTH_SHORT).show();
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage(R.string.confirmation_dialog_submit);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.label_yes, (dialog, which) -> {
                CommonUtils.showLoading(v.getContext());
                VolleyAPI volleyAPI = new VolleyAPI(v.getContext());

                Map<String, String> params = new HashMap<>();
                params.put("nama", etNamaBahanPokok.getText().toString()); //disable
                params.put("jumlah", etJumlahBahanPokok.getText().toString());
                params.put("harga", etHargaBahanPokok.getText().toString());

                if (screenState.equals(MyConstants.TAMBAH_DETAIL_BAHAN_POKOK)) {
                    params.put("bahan_pokok_id", bundle.getString("idMeja"));
                    //params.put("supplier_id", bundle.getString("idMeja"));
                    params.put("aksi", "1");
                } else {
                    params.put("satuan", satuanSelected);
                }

                if (screenState.equals(MyConstants.TAMBAH_BAHAN_POKOK)) {
                    volleyAPI.postRequest("addBahanPokokBaru", params, result -> {
                        try {
                            JSONObject resultJSON = new JSONObject(result);
                            Intent myIntent = new Intent(getApplicationContext(), BahanPokokActivity.class);
                            startActivityForResult(myIntent, 0);
                            Toast.makeText(getApplicationContext(), resultJSON.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                } else if (screenState.equals(MyConstants.TAMBAH_DETAIL_BAHAN_POKOK)) {
//                    volleyAPI.postRequest("addRiwayatBahanPokok", params, result -> {
//                        try {
//                            JSONObject resultJSON = new JSONObject(result);
//                            Intent myIntent = new Intent(getApplicationContext(), BahanPokokActivity.class);
//                            startActivityForResult(myIntent, 0);
//                            Toast.makeText(getApplicationContext(), resultJSON.getString("message"), Toast.LENGTH_SHORT).show();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    });
                    Toast.makeText(getApplicationContext(), "Tambah Detail Bahan Pokok", Toast.LENGTH_SHORT).show();
                }
                CommonUtils.hideLoading();
            });
            builder.setNegativeButton(R.string.label_no, (dialog, which) -> {
            });
            builder.show();
        });

        etNamaBahanPokok.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });

        etHargaBahanPokok.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });

        etJumlahBahanPokok.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}