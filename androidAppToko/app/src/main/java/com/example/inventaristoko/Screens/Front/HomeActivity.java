package com.example.inventaristoko.Screens.Front;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.inventaristoko.R;
import com.example.inventaristoko.Screens.Pengunjung.PengunjungActivity;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.VolleyAPI;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private Context appContext;
    private Button btnPengunjung, btnAdmin;
    private IntentIntegrator intentIntegrator;
    private String idMeja, namaMeja, passwordMeja;

    private void init() {
        appContext = getApplicationContext();
        btnPengunjung = findViewById(R.id.btnPengunjung);
        btnAdmin = findViewById(R.id.btnAdmin);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

        btnPengunjung.setOnClickListener(this);
        btnAdmin.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult resultIntent = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(resultIntent != null) {
            if(resultIntent.getContents() == null) {
                CommonUtils.showToast(appContext, getResources().getString(R.string.label_data_tidak_ditemukan));
            } else {
                try {
                    CommonUtils.showLoading(HomeActivity.this);
                    VolleyAPI volleyAPI = new VolleyAPI(HomeActivity.this);

                    JSONObject object = new JSONObject(resultIntent.getContents());
                    idMeja = object.getString("user_id");
                    namaMeja = object.getString("user_name");
                    passwordMeja = "udin123";

                    Map<String, String> params = new HashMap<>();
                    params.put("user_id", idMeja);
                    params.put("password", passwordMeja);

                    volleyAPI.getRequest(MyConstants.LOGIN_CUSTOMER_ACTION, params, result -> {
                        try {
                            JSONObject resultJSON = new JSONObject(result);
                            if(resultJSON.getString("is_error").equalsIgnoreCase("0")) {
                                Intent intent = new Intent (HomeActivity.this, PengunjungActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("namaMeja", namaMeja);
                                intent.putExtras(bundle);
                                HomeActivity.this.startActivity(intent);
                            }

                            CommonUtils.showToast(appContext, resultJSON.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                } catch(JSONException e) {
                    e.printStackTrace();
                    CommonUtils.showToast(appContext, resultIntent.getContents());
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPengunjung :
                intentIntegrator = new IntentIntegrator(this);
                intentIntegrator.setPrompt(getResources().getString(R.string.label_scan_barcode));
                intentIntegrator.initiateScan();
                break;
            case R.id.btnAdmin :
                startActivity(new Intent(v.getContext(), LoginActivity.class));
                finish();
                break;
        }
    }
}