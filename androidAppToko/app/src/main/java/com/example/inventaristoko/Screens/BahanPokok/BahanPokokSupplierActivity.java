package com.example.inventaristoko.Screens.BahanPokok;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.VolleyAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BahanPokokSupplierActivity extends AppCompatActivity implements View.OnClickListener {
    private Context appContext;
    private EditText etNamaPemasok, etAlamatPemasok, etNomorTeleponPemasok;
    private String txtNamaPemasok, txtAlamatPemasok, txtNomorTeleponPemasok;
    private Button btnTambahSupplier;

    private void init() {
        appContext = getApplicationContext();
        etNamaPemasok = findViewById(R.id.etNamaPemasok);
        etAlamatPemasok = findViewById(R.id.etAlamatPemasok);
        etNomorTeleponPemasok = findViewById(R.id.etNomorTeleponPemasok);
        btnTambahSupplier = findViewById(R.id.btnTambahSupplier);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bahan_pokok_supplier);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_tambah_pemasok);

        init();

        btnTambahSupplier.setOnClickListener(this);

        etNamaPemasok.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                CommonUtils.hideKeyboard(appContext, v);
            }
        });

        etAlamatPemasok.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                CommonUtils.hideKeyboard(appContext, v);
            }
        });

        etNomorTeleponPemasok.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                CommonUtils.hideKeyboard(appContext, v);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnTambahSupplier) {
            txtNamaPemasok = etNamaPemasok.getText().toString();
            txtAlamatPemasok = etAlamatPemasok.getText().toString();
            txtNomorTeleponPemasok = etNomorTeleponPemasok.getText().toString();

            if (txtNamaPemasok.isEmpty() || txtAlamatPemasok.isEmpty() || txtNomorTeleponPemasok.isEmpty()) {
                Toast.makeText(getApplicationContext(), R.string.label_data_kosong, Toast.LENGTH_SHORT).show();
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage(R.string.confirmation_dialog_submit);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.label_yes, (dialog, which) -> callSubmitDataBahanPokokSupplierRequest(v.getContext()));
            builder.setNegativeButton(R.string.label_no, (dialog, which) -> {
            });
            builder.show();
        }
    }

    private void callSubmitDataBahanPokokSupplierRequest(Context context) {
        CommonUtils.showLoading(context);
        VolleyAPI volleyAPI = new VolleyAPI(context);

        Map<String, String> params = new HashMap<>();
        params.put("nama_supplier", txtNamaPemasok);
        params.put("alamat_supplier", txtAlamatPemasok);
        params.put("nomor_telepon_supplier", txtNomorTeleponPemasok);

        volleyAPI.postRequest(MyConstants.BAHAN_POKOK_ADD_SUPPLIER_ACTION, params, result -> {
            try {
                JSONObject resultJSON = new JSONObject(result);
                Intent myIntent = new Intent(getApplicationContext(), BahanPokokActivity.class);
                startActivityForResult(myIntent, 0);

                CommonUtils.showToast(context, resultJSON.getString("message"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        CommonUtils.hideLoading();
    }
}