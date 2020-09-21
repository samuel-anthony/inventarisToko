package com.example.inventaristoko.Screens.Front;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText etOldPassword, etNewPassword, etConfirmPassword;
    private Button btnKirimPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getSupportActionBar().setTitle(R.string.menu_password);

        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnKirimPassword = findViewById(R.id.btnKirimPassword);
        btnKirimPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(String.valueOf(etOldPassword.getText()).equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.label_data_tidak_boleh_kosong, Toast.LENGTH_SHORT).show();
                    return;
                }

                if(String.valueOf(etNewPassword.getText()).equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.label_data_tidak_boleh_kosong, Toast.LENGTH_SHORT).show();
                    return;
                }

                if(String.valueOf(etConfirmPassword.getText()).equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.label_data_tidak_boleh_kosong, Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!(String.valueOf(etNewPassword.getText()).equals(String.valueOf(etConfirmPassword.getText())))) {
                    Toast.makeText(getApplicationContext(), R.string.label_data_password_baru_salah, Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this);
                builder.setMessage("Anda Yakin Ingin Mengganti Password?");
                builder.setCancelable(false);
                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CommonUtils.showLoading(ChangePasswordActivity.this);
                        VolleyAPI volleyAPI = new VolleyAPI(ChangePasswordActivity.this);
                        Map<String, String> params = new HashMap<>();
                        params.put("password", etOldPassword.getText().toString());
                        params.put("new_password", etNewPassword.getText().toString());

                        volleyAPI.postRequest("gantiPasswordBaru", params, new VolleyCallback() {
                            @Override
                            public void onSuccessResponse(String result) {
                                try {
                                    JSONObject resultJSON = new JSONObject(result);
                                    Intent myIntent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                                    startActivityForResult(myIntent, 0);
                                    Toast.makeText(getApplicationContext(), resultJSON.getString("message"), Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
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

        etOldPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        etNewPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        etConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
