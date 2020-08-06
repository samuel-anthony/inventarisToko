package com.example.inventaristoko;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FrontActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonGuest;
    private TextView textViewNama;
    private IntentIntegrator intentIntegrator;

    volleyAPI volleyAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);

//        volleyAPI = new volleyAPI(this);
//        volleyAPI.putRequest("bahanPokok", new VolleyCallback() {
//            @Override
//            public void onSuccessResponse(String result) {
//                System.out.println(result);
//            }
//        });
//        Map<String, String> params = new HashMap<>();
//        params.put("test","hehe");
//        volleyAPI.postRequest("testApi",params, new VolleyCallback() {
//            @Override
//            public void onSuccessResponse(String result) {
//                System.out.println(result);
//            }
//        });
        buttonGuest = (Button) findViewById(R.id.buttonGuest);
        textViewNama = (TextView) findViewById(R.id.logoName);

        buttonGuest.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Result is not Found", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject object = new JSONObject(result.getContents());
                    textViewNama.setText(object.getString("nama"));
                } catch(JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View view) {
        intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan a Barcode");
        intentIntegrator.initiateScan();
    }
}