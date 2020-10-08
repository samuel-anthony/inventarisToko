package com.example.inventaristoko.Screens.Pengguna;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.VolleyAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class PenggunaEntryActivity extends AppCompatActivity implements View.OnClickListener {
    private Context appContext;
    private Bundle bundle;
    private Button btnKirimPengguna;
    private EditText etNamaPengguna, etUsernamePengguna, etEmailPengguna, etNomorTeleponPengguna, etTanggalLahirPengguna;
    private String screenState, oldUsernamePengguna, txtUsernamePengguna, txtNamaPengguna, txtEmailPengguna, txtNomorTeleponPengguna, txtTanggalLahirPengguna;

    private void init() {
        appContext = getApplicationContext();
        bundle = getIntent().getExtras();
        etNamaPengguna = findViewById(R.id.etNamaPengguna);
        etUsernamePengguna = findViewById(R.id.etUsernamePengguna);
        etEmailPengguna = findViewById(R.id.etEmailPengguna);
        etNomorTeleponPengguna = findViewById(R.id.etNomorTeleponPengguna);
        etTanggalLahirPengguna = findViewById(R.id.etTanggalLahirPengguna);
        etTanggalLahirPengguna.setInputType(InputType.TYPE_NULL);
        btnKirimPengguna = findViewById(R.id.btnKirimPengguna);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengguna_entry);

        init();

        assert bundle != null;
        screenState = bundle.getString("screenState");
        oldUsernamePengguna = bundle.getString("usernamePengguna");

        if(screenState.equals(MyConstants.UBAH_PENGGUNA)) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_ubah_pengguna);
            etNamaPengguna.setText(bundle.getString("namaPengguna"));
            etUsernamePengguna.setText(bundle.getString("usernamePengguna"));
            etEmailPengguna.setText(bundle.getString("emailPengguna"));
            etNomorTeleponPengguna.setText(bundle.getString("nomorTeleponPengguna"));
            etTanggalLahirPengguna.setText(bundle.getString("tanggalLahirPengguna"));
        } else {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_tambah_pengguna);
        }

        etTanggalLahirPengguna.setOnClickListener(this);
        btnKirimPengguna.setOnClickListener(this);

        etNamaPengguna.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                CommonUtils.hideKeyboard(appContext, v);
            }
        });

        etUsernamePengguna.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                CommonUtils.hideKeyboard(appContext, v);
            }
        });

        etEmailPengguna.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                CommonUtils.hideKeyboard(appContext, v);
            }
        });

        etNomorTeleponPengguna.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                CommonUtils.hideKeyboard(appContext, v);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnKirimPengguna :
                txtUsernamePengguna = etUsernamePengguna.getText().toString();
                txtNamaPengguna = etNamaPengguna.getText().toString();
                txtEmailPengguna = etEmailPengguna.getText().toString();
                txtNomorTeleponPengguna = etNomorTeleponPengguna.getText().toString();
                txtTanggalLahirPengguna = etTanggalLahirPengguna.getText().toString();

                if (txtUsernamePengguna.isEmpty() || txtNamaPengguna.isEmpty() || txtEmailPengguna.isEmpty() || txtNomorTeleponPengguna.isEmpty() || txtTanggalLahirPengguna.isEmpty()) {
                    CommonUtils.showToast(appContext, appContext.getString(R.string.label_input_kosong));
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage(R.string.confirmation_dialog_submit);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.label_yes, (dialog, which) -> callSubmitDataPenggunaRequest(v.getContext()));
                builder.setNegativeButton(R.string.label_no, (dialog, which) -> {
                });
                builder.show();
                break;
            case R.id.etTanggalLahirPengguna :
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                DatePickerDialog datePicker = new DatePickerDialog(v.getContext(), (view, year1, monthOfYear, dayOfMonth) -> {
                    String monthLabel, dayLabel;

                    monthLabel = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
                    dayLabel = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);

                    etTanggalLahirPengguna.setText(String.format(Locale.getDefault(), "%d-%s-%s", year1, monthLabel, dayLabel));
                }, year, month, day);
                datePicker.show();
                break;
        }
    }

    private void callSubmitDataPenggunaRequest(Context context) {
        CommonUtils.showLoading(context);
        VolleyAPI volleyAPI = new VolleyAPI(context);

        Map<String, String> params = new HashMap<>();
        params.put("user_name", txtUsernamePengguna);
        params.put("full_name", txtNamaPengguna);
        params.put("email", txtEmailPengguna);
        params.put("phone_number", txtNomorTeleponPengguna);
        params.put("birth_date", txtTanggalLahirPengguna);

        if (screenState.equals(MyConstants.UBAH_PENGGUNA)) {
            params.put("user_name_old", oldUsernamePengguna);
        }

        if (screenState.equals(MyConstants.UBAH_PENGGUNA)) {
            volleyAPI.putRequest(MyConstants.PENGGUNA_EDIT_ACTION, params, result -> {
                try {
                    JSONObject resultJSON = new JSONObject(result);
                    Intent myIntent = new Intent(getApplicationContext(), PenggunaActivity.class);
                    startActivityForResult(myIntent, 0);

                    CommonUtils.showToast(context, resultJSON.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        } else if (screenState.equals(MyConstants.TAMBAH_PENGGUNA)) {
            volleyAPI.postRequest(MyConstants.PENGGUNA_ADD_ACTION, params, result -> {
                try {
                    JSONObject resultJSON = new JSONObject(result);
                    Intent myIntent = new Intent(context, PenggunaActivity.class);
                    startActivityForResult(myIntent, 0);

                    CommonUtils.showToast(context, resultJSON.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }

        CommonUtils.hideLoading();
    }
}