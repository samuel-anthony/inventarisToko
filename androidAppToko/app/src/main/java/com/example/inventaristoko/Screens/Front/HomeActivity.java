package com.example.inventaristoko.Screens.Front;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.inventaristoko.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnPengunjung, btnAdmin;
    private IntentIntegrator intentIntegrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnPengunjung = findViewById(R.id.btnPengunjung);
        btnAdmin = findViewById(R.id.btnAdmin);

        btnPengunjung.setOnClickListener(this);

        btnAdmin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), LoginActivity.class);
                startActivityForResult(myIntent, 0);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, getResources().getString(R.string.label_data_tidak_ditemukan), Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject object = new JSONObject(result.getContents());
//                    Intent intent = new Intent (v.getContext(), PengunjungActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("namaMeja", object.getString("nama_meja"));
//                    intent.putExtras(bundle);
//                    v.getContext().startActivity(intent);
//                    startActivityForResult(intent, 0);

                    Toast.makeText(this, object.getString("nama"), Toast.LENGTH_SHORT).show();
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
        intentIntegrator.setPrompt(getResources().getString(R.string.label_scan_barcode));
        intentIntegrator.initiateScan();
    }
}