package com.example.inventaristoko.Screens.Front;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.Preferences;
import com.example.inventaristoko.Utils.VolleyAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private Context appContext;
    private EditText etOldPassword, etNewPassword, etConfirmPassword;
    private Button btnKirimPassword;
    private String txtOldPassword, txtNewPassword;

    private void init() {
        appContext = getApplicationContext();
        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnKirimPassword = findViewById(R.id.btnKirimPassword);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_password);

        init();
        btnKirimPassword.setOnClickListener(this);

        etOldPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                CommonUtils.hideKeyboard(appContext, v);
            }
        });

        etNewPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                CommonUtils.hideKeyboard(appContext, v);
            }
        });

        etConfirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                CommonUtils.hideKeyboard(appContext, v);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnKirimPassword) {
            txtOldPassword = etOldPassword.getText().toString();
            txtNewPassword = etNewPassword.getText().toString();
            String txtConfirmPassword = etConfirmPassword.getText().toString();

            if (txtOldPassword.isEmpty() || txtNewPassword.isEmpty() || txtConfirmPassword.isEmpty()) {
                CommonUtils.showToast(appContext, appContext.getString(R.string.label_input_kosong));
                return;
            }

            if (!(txtNewPassword.equals(txtConfirmPassword))) {
                CommonUtils.showToast(appContext, appContext.getString(R.string.label_input_password_baru_salah));
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage(R.string.confirmation_dialog_password);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.label_yes, (dialog, which) -> callChangePasswordRequest(v.getContext()));
            builder.setNegativeButton(R.string.label_no, (dialog, which) -> {
            });
            builder.show();
        }
    }

    private void callChangePasswordRequest(Context context) {
        CommonUtils.showLoading(context);
        VolleyAPI volleyAPI = new VolleyAPI(context);

        Map<String, String> params = new HashMap<>();
        params.put("user_name", Preferences.getLoggedInUser(getBaseContext()));
        params.put("oldPassword", txtOldPassword);
        params.put("newPassword", txtNewPassword);

        volleyAPI.putRequest(MyConstants.CHANGE_PASSWORD_ACTION, params, result -> {
            try {
                JSONObject resultJSON = new JSONObject(result);
                if(resultJSON.getString("is_error").equalsIgnoreCase("0")) {
                    Preferences.clearLoggedInUser(getBaseContext());
                    startActivity(new Intent(context, LoginActivity.class));
                    finish();
                }

                CommonUtils.showToast(context, resultJSON.getString("message"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        CommonUtils.hideLoading();
    }
}
