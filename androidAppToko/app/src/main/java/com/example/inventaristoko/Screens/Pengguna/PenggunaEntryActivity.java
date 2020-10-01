package com.example.inventaristoko.Screens.Pengguna;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.VolleyAPI;
import com.example.inventaristoko.Utils.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PenggunaEntryActivity extends AppCompatActivity {
    private DatePickerDialog datePicker;
    private Button btnKirimPengguna;
    private EditText etNamaPengguna, etUsernamePengguna, etEmailPengguna, etNomorTeleponPengguna, etTanggalLahirPengguna;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengguna_entry);

        Bundle bundle = getIntent().getExtras();
        String screenState = bundle.getString("screenState");

        etNamaPengguna = findViewById(R.id.etNamaPengguna);
        etUsernamePengguna = findViewById(R.id.etUsernamePengguna);
        etEmailPengguna = findViewById(R.id.etEmailPengguna);
        etNomorTeleponPengguna = findViewById(R.id.etNomorTeleponPengguna);
        etTanggalLahirPengguna = findViewById(R.id.etTanggalLahirPengguna);
        etTanggalLahirPengguna.setInputType(InputType.TYPE_NULL);

        if(screenState.equals(MyConstants.UBAH_PENGGUNA)) {
            getSupportActionBar().setTitle(R.string.menu_ubah_pengguna);
            etNamaPengguna.setText(bundle.getString("namaPengguna"));
            etUsernamePengguna.setText(bundle.getString("usernamePengguna"));
            etEmailPengguna.setText(bundle.getString("emailPengguna"));
            etNomorTeleponPengguna.setText(bundle.getString("nomorTeleponPengguna"));
            etTanggalLahirPengguna.setText(bundle.getString("tanggalLahirPengguna"));
        } else {
            getSupportActionBar().setTitle(R.string.menu_tambah_pengguna);
        }

        etTanggalLahirPengguna.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);

            datePicker = new DatePickerDialog(PenggunaEntryActivity.this, (view, year1, monthOfYear, dayOfMonth) -> {
                String monthLabel, dayLabel;

                monthLabel = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
                dayLabel  = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);

                etTanggalLahirPengguna.setText(year1 + "-" + monthLabel + "-" + dayLabel);
            }, year, month, day);
            datePicker.show();
        });

        btnKirimPengguna = findViewById(R.id.btnKirimPengguna);
        btnKirimPengguna.setOnClickListener(v -> {
            if(String.valueOf(etUsernamePengguna.getText()).equals("") ||
                    String.valueOf(etNamaPengguna.getText()).equals("") ||
                    String.valueOf(etEmailPengguna.getText()).equals("") ||
                    String.valueOf(etNomorTeleponPengguna.getText()).equals("") ||
                    String.valueOf(etTanggalLahirPengguna.getText()).equals("")) {
                Toast.makeText(getApplicationContext(), R.string.label_data_kosong, Toast.LENGTH_SHORT).show();
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(PenggunaEntryActivity.this);
            builder.setMessage(R.string.confirmation_dialog_submit);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.label_yes, (dialog, which) -> {
                CommonUtils.showLoading(PenggunaEntryActivity.this);
                VolleyAPI volleyAPI = new VolleyAPI(PenggunaEntryActivity.this);

                Map<String, String> params = new HashMap<>();
                params.put("user_name", etUsernamePengguna.getText().toString());

                if (screenState.equals(MyConstants.UBAH_PENGGUNA)) {
                    params.put("user_name_old", bundle.getString("usernamePengguna"));
                }

                params.put("full_name", etNamaPengguna.getText().toString());
                params.put("email", etEmailPengguna.getText().toString());
                params.put("phone_number", etNomorTeleponPengguna.getText().toString());
                params.put("birth_date", etTanggalLahirPengguna.getText().toString());

                if (screenState.equals(MyConstants.UBAH_PENGGUNA)) {
                    volleyAPI.putRequest("updateAdminUser", params, result -> {
                        try {
                            JSONObject resultJSON = new JSONObject(result);
                            Intent myIntent = new Intent(getApplicationContext(), PenggunaActivity.class);
                            startActivityForResult(myIntent, 0);
                            Toast.makeText(getApplicationContext(), resultJSON.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                } else if (screenState.equals(MyConstants.TAMBAH_PENGGUNA)) {
                    volleyAPI.postRequest("registerAdmin", params, result -> {
                        try {
                            JSONObject resultJSON = new JSONObject(result);
                            Intent myIntent = new Intent(getApplicationContext(), PenggunaActivity.class);
                            startActivityForResult(myIntent, 0);
                            Toast.makeText(getApplicationContext(), resultJSON.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
            builder.setNegativeButton(R.string.label_no, (dialog, which) -> {
            });
            builder.show();
        });

        etNamaPengguna.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });

        etUsernamePengguna.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });

        etEmailPengguna.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });

        etNomorTeleponPengguna.setOnFocusChangeListener((v, hasFocus) -> {
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