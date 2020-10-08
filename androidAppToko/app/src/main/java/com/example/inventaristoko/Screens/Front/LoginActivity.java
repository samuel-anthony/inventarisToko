package com.example.inventaristoko.Screens.Front;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.inventaristoko.R;
import com.example.inventaristoko.Screens.Penjualan.PenjualanActivity;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.Preferences;
import com.example.inventaristoko.Utils.VolleyAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Context appContext;
    private EditText etUsernameLogin, etPasswordLogin;
    private Button btnMasukLogin;
    private String txtUsernameLogin, txtPasswordLogin;

    private void init() {
        appContext = getApplicationContext();
        etUsernameLogin = findViewById(R.id.etUsernameLogin);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        btnMasukLogin = findViewById(R.id.btnMasukLogin);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        btnMasukLogin.setOnClickListener(this);

        etUsernameLogin.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                CommonUtils.hideKeyboard(appContext, v);
            }
        });

        etPasswordLogin.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                CommonUtils.hideKeyboard(appContext, v);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnMasukLogin) {
            txtUsernameLogin = etUsernameLogin.getText().toString();
            txtPasswordLogin = etPasswordLogin.getText().toString();

            if (txtUsernameLogin.isEmpty() || txtPasswordLogin.isEmpty()) {
                CommonUtils.showToast(appContext, appContext.getString(R.string.label_input_password_kosong));
                return;
            }

            callLoginRequest(v.getContext());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getBaseContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void callLoginRequest(Context context) {
        CommonUtils.showLoading(context);
        VolleyAPI volleyAPI = new VolleyAPI(context);

        Map<String, String> params = new HashMap<>();
        params.put("user_id", txtUsernameLogin);
        params.put("password", txtPasswordLogin);

        volleyAPI.getRequest(MyConstants.LOGIN_ACTION, params, result -> {
            try {
                JSONObject resultJSON = new JSONObject(result);
                if(resultJSON.getString("is_error").equalsIgnoreCase("0")) {
                    Preferences.setLoggedInUser(context, etUsernameLogin.getText().toString());
                    Preferences.setLoggedInStatus(context,true);
                    startActivity(new Intent(context, PenjualanActivity.class));
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
