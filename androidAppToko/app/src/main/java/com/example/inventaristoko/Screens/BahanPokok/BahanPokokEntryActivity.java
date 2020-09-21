package com.example.inventaristoko.Screens.BahanPokok;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.VolleyAPI;
import com.example.inventaristoko.Utils.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BahanPokokEntryActivity extends AppCompatActivity{
    private EditText etNamaBahanPokok, etJumlahBahanPokok;
    private Button btnKirimBahanPokok;
    private Spinner spnSatuanBahanPokok;
    private String[] spnSatuan = {"Miligram", "Desigram", "Centigram", "Gram", "Hektogram", "Dekagram", "Kilogram"};
    private String satuanSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bahan_pokok_entry);

        Bundle bundle = getIntent().getExtras();
        String screenState = bundle.getString("screenState");
        String satuan = bundle.getString("satuanBahanPokok");

        etNamaBahanPokok = findViewById(R.id.etNamaBahanPokok);
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
            spnSatuanBahanPokok.setSelection(0);
        }

        btnKirimBahanPokok = findViewById(R.id.btnKirimBahanPokok);
        btnKirimBahanPokok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BahanPokokEntryActivity.this);
                builder.setMessage("Anda Yakin Ingin Kirim Data ini?");
                builder.setCancelable(false);
                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CommonUtils.showLoading(BahanPokokEntryActivity.this);
                        VolleyAPI volleyAPI = new VolleyAPI(BahanPokokEntryActivity.this);
                        Map<String, String> params = new HashMap<>();
                        params.put("nama", etNamaBahanPokok.getText().toString());
                        params.put("jumlah", etJumlahBahanPokok.getText().toString());
                        params.put("satuan", satuanSelected);

                        if (screenState.equals(MyConstants.TAMBAH_BAHAN_POKOK)) {
                            volleyAPI.postRequest("addBahanPokokBaru", params, new VolleyCallback() {
                                @Override
                                public void onSuccessResponse(String result) {
                                    try {
                                        JSONObject resultJSON = new JSONObject(result);
                                        Intent myIntent = new Intent(getApplicationContext(), BahanPokokActivity.class);
                                        startActivityForResult(myIntent, 0);
                                        Toast.makeText(getApplicationContext(), resultJSON.getString("message"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else if (screenState.equals(MyConstants.TAMBAH_DETAIL_BAHAN_POKOK)) {
                            Toast.makeText(getApplicationContext(), MyConstants.TAMBAH_DETAIL_BAHAN_POKOK, Toast.LENGTH_SHORT).show();
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
        });

        etNamaBahanPokok.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        etJumlahBahanPokok.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}