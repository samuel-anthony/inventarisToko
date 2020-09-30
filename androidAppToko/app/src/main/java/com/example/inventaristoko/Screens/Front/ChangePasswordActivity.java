package com.example.inventaristoko.Screens.Front;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.Preferences;
import com.example.inventaristoko.Utils.VolleyAPI;

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
        btnKirimPassword.setOnClickListener(v -> {
            if(String.valueOf(etOldPassword.getText()).equals("") || String.valueOf(etNewPassword.getText()).equals("") || String.valueOf(etConfirmPassword.getText()).equals("")) {
                Toast.makeText(getApplicationContext(), R.string.label_data_tidak_boleh_kosong, Toast.LENGTH_SHORT).show();
                return;
            }

            if(!(String.valueOf(etNewPassword.getText()).equals(String.valueOf(etConfirmPassword.getText())))) {
                Toast.makeText(getApplicationContext(), R.string.label_data_password_baru_salah, Toast.LENGTH_SHORT).show();
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage(R.string.confirmation_dialog_password);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.label_yes, (dialog, which) -> {
                CommonUtils.showLoading(v.getContext());
                VolleyAPI volleyAPI = new VolleyAPI(v.getContext());

                Map<String, String> params = new HashMap<>();
                params.put("user_name", Preferences.getLoggedInUser(getBaseContext()));
                params.put("oldPassword", etOldPassword.getText().toString());
                params.put("newPassword", etNewPassword.getText().toString());

                volleyAPI.putRequest("updateUserPassword", params, result -> {
                    try {
                        JSONObject resultJSON = new JSONObject(result);
                        Toast.makeText(getApplicationContext(),resultJSON.getString("message"),Toast.LENGTH_SHORT).show();
                        if(resultJSON.getString("is_error").equalsIgnoreCase("0")) {
                            Preferences.clearLoggedInUser(getBaseContext());
                            startActivity(new Intent(v.getContext(), LoginActivity.class));
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
                CommonUtils.hideLoading();
            });
            builder.setNegativeButton(R.string.label_no, (dialog, which) -> {
            });
            builder.show();
        });

        etOldPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });

        etNewPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });

        etConfirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
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
