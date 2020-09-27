package com.example.inventaristoko.Screens.Front;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.inventaristoko.R;
import com.example.inventaristoko.Screens.Penjualan.PenjualanActivity;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.Preferences;
import com.example.inventaristoko.Utils.VolleyCallback;
import com.example.inventaristoko.Utils.VolleyAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsernameLogin, etPasswordLogin;
    private Button btnMasukLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsernameLogin = findViewById(R.id.etUsernameLogin);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        btnMasukLogin = findViewById(R.id.btnMasukLogin);

        btnMasukLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callLogin(v.getContext());
            }
        });

        etUsernameLogin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        etPasswordLogin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Preferences.getLoggedInStatus(getBaseContext())){
            startActivity(new Intent(getBaseContext(), PenjualanActivity.class));
            finish();
        }
    }

    public void callLogin(Context context){
        CommonUtils.showLoading(context);
        VolleyAPI volleyAPI = new VolleyAPI(context);
        Map<String, String> params = new HashMap<>();
        params.put("user_id", etUsernameLogin.getText().toString());
        params.put("password", etPasswordLogin.getText().toString());
        volleyAPI.getRequest("login", params, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                try {
                    JSONObject resultJSON = new JSONObject(result);
                    Toast.makeText(getApplicationContext(),resultJSON.getString("message"),Toast.LENGTH_SHORT).show();
                    if(resultJSON.getString("is_error").equalsIgnoreCase("0")) {
                        Preferences.setLoggedInUser(getBaseContext(), etUsernameLogin.getText().toString());
                        Preferences.setLoggedInStatus(getBaseContext(),true);
                        Intent myIntent = new Intent(context, PenjualanActivity.class);
                        startActivityForResult(myIntent, 0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        CommonUtils.hideLoading();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
