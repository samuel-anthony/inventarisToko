package com.example.inventaristoko.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.inventaristoko.R;
import com.example.inventaristoko.VolleyCallback;
import com.example.inventaristoko.volleyAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText etLoginId, etLoginPassword;
    private Button btnLogin;

    com.example.inventaristoko.volleyAPI volleyAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        volleyAPI volleyAPI = new volleyAPI(this);
        etLoginId = findViewById(R.id.loginId);
        etLoginPassword = findViewById(R.id.loginPass);
        btnLogin = findViewById(R.id.buttonLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<>();
                params.put("user_id",etLoginId.getText().toString());
                params.put("password",etLoginPassword.getText().toString());
                volleyAPI.postRequest("login",params, new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(String result) {
                        try {
                            JSONObject resultJSON = new JSONObject(result);
                            Toast.makeText(getApplicationContext(),resultJSON.getString("message"),Toast.LENGTH_SHORT).show();
                            if(resultJSON.getString("is_error").equalsIgnoreCase("0")) {
                                Intent myIntent = new Intent(v.getContext(), HomeActivity.class);
                                startActivityForResult(myIntent, 0);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, FrontActivity.class);
        startActivity(intent);
        finish();
    }
}
