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
    private Button btnSubmit;
    private EditText etFullName, etUSerName, etEmail, etPhone, etBirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengguna_entry);

        Bundle bundle = getIntent().getExtras();
        String screenState = bundle.getString("screenState");

        etFullName = findViewById(R.id.inputFullName);
        etUSerName = findViewById(R.id.inputUserName);
        etEmail = findViewById(R.id.inputEmail);
        etPhone = findViewById(R.id.inputPhoneNumber);
        etBirth = findViewById(R.id.inputBirthDate);

        etBirth.setInputType(InputType.TYPE_NULL);

        if(screenState.equals(MyConstants.EDIT_PENGGUNA)) {
            getSupportActionBar().setTitle(R.string.label_edit_pengguna);
            etFullName.setText(bundle.getString("fullName"));
            etUSerName.setText(bundle.getString("userName"));
            etEmail.setText(bundle.getString("email"));
            etPhone.setText(bundle.getString("phoneNumber"));
            etBirth.setText(bundle.getString("birthDate"));
        } else {
            getSupportActionBar().setTitle(R.string.label_add_pengguna);
        }

        etBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog

                datePicker = new DatePickerDialog(PenggunaEntryActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String monthLabel, dayLabel;

                                monthLabel = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
                                dayLabel  = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);

                                etBirth.setText(year + "-" + monthLabel + "-" + dayLabel);
                            }
                        }, year, month, day);
                datePicker.show();
            }
        });

        btnSubmit = findViewById(R.id.buttonSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PenggunaEntryActivity.this);
                builder.setMessage("Anda Yakin Ingin Kirim Data ini?");
                builder.setCancelable(false);
                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CommonUtils.showLoading(PenggunaEntryActivity.this);
                        VolleyAPI volleyAPI = new VolleyAPI(PenggunaEntryActivity.this);
                        Map<String, String> params = new HashMap<>();
                        params.put("user_name", etUSerName.getText().toString());

                        if (screenState.equals(MyConstants.EDIT_PENGGUNA)) {
                            params.put("user_name_old", bundle.getString("userName"));
                        }

                        params.put("full_name", etFullName.getText().toString());
                        params.put("email", etEmail.getText().toString());
                        params.put("phone_number", etPhone.getText().toString());
                        params.put("birth_date", etBirth.getText().toString());

                        if (screenState.equals(MyConstants.EDIT_PENGGUNA)) {
                            volleyAPI.putRequest("updateAdminUser", params, new VolleyCallback() {
                                @Override
                                public void onSuccessResponse(String result) {
                                    try {
                                        JSONObject resultJSON = new JSONObject(result);
                                        Intent myIntent = new Intent(getApplicationContext(), PenggunaActivity.class);
                                        startActivityForResult(myIntent, 0);
                                        Toast.makeText(getApplicationContext(), resultJSON.getString("message"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else if (screenState.equals(MyConstants.ADD_PENGGUNA)) {
                            volleyAPI.postRequest("registerAdmin", params, new VolleyCallback() {
                                @Override
                                public void onSuccessResponse(String result) {
                                    try {
                                        JSONObject resultJSON = new JSONObject(result);
                                        Intent myIntent = new Intent(getApplicationContext(), PenggunaActivity.class);
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
        });

        etFullName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        etUSerName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        etPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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