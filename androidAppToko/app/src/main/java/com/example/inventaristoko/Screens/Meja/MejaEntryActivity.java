package com.example.inventaristoko.Screens.Meja;

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
import android.widget.ImageView;

import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.VolleyAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MejaEntryActivity extends AppCompatActivity {
    private EditText etIdMeja, etNamaMeja;
    private ImageView ivQrCode;
    private Button btnGenerate, btnSubmit;
    private String screenState, idMeja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meja_entry);

        Bundle bundle = getIntent().getExtras();
        screenState = bundle.getString("screenState");

        etIdMeja = findViewById(R.id.etIdMeja);
        etNamaMeja = findViewById(R.id.etNamaMeja);
        ivQrCode = findViewById(R.id.ivQrCode);

        if(screenState.equals(MyConstants.UBAH_MEJA)) {
            getSupportActionBar().setTitle(R.string.menu_ubah_meja);
            etIdMeja.setText(bundle.getString("idMeja"));
            etNamaMeja.setText(bundle.getString("namaMeja"));

            try {
                idMeja = etIdMeja.getText().toString();
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                BitMatrix bitMatrix = multiFormatWriter.encode(idMeja, BarcodeFormat.QR_CODE,150,150);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                ivQrCode.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        } else {
            getSupportActionBar().setTitle(R.string.menu_tambah_meja);
        }

        btnGenerate = findViewById(R.id.btnGenerate);
        btnGenerate.setOnClickListener(v -> {
            try {
                String text = etIdMeja.getText().toString();
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,150,150);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                ivQrCode.setImageBitmap(bitmap);
                hideKeyboard(v);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        });

        btnSubmit = findViewById(R.id.btnKirim);
        btnSubmit.setOnClickListener(v -> {
            if(String.valueOf(etIdMeja.getText()).equals("") || String.valueOf(etNamaMeja.getText()).equals("")) {
                Toast.makeText(getApplicationContext(), R.string.label_data_kosong, Toast.LENGTH_SHORT).show();
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(MejaEntryActivity.this);
            builder.setMessage(R.string.confirmation_dialog_submit);
            builder.setCancelable(false);
            builder.setPositiveButton("Iya", (dialog, which) -> {
                CommonUtils.showLoading(v.getContext());
                VolleyAPI volleyAPI = new VolleyAPI(v.getContext());

                Map<String, String> params = new HashMap<>();
                params.put("nama_meja", etIdMeja.getText().toString());
                params.put("full_name", etNamaMeja.getText().toString());

                if (screenState.equals(MyConstants.UBAH_MEJA)) {
                    params.put("nama_meja_old", bundle.getString("idMeja"));
                }

                if (screenState.equals(MyConstants.UBAH_MEJA)) {
                    volleyAPI.putRequest("updateUser", params, result -> {
                        try {
                            JSONObject resultJSON = new JSONObject(result);
                            Intent myIntent = new Intent(getApplicationContext(), MejaActivity.class);
                            startActivityForResult(myIntent, 0);
                            Toast.makeText(getApplicationContext(), resultJSON.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                } else if (screenState.equals(MyConstants.TAMBAH_MEJA)) {
                    volleyAPI.postRequest("register", params, result -> {
                        try {
                            JSONObject resultJSON = new JSONObject(result);
                            Intent myIntent = new Intent(getApplicationContext(), MejaActivity.class);
                            startActivityForResult(myIntent, 0);
                            Toast.makeText(getApplicationContext(), resultJSON.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
            builder.setNegativeButton(R.string.label_no, (dialog, which) -> {
            });
            builder.show();
        });

        etIdMeja.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });

        etNamaMeja.setOnFocusChangeListener((v, hasFocus) -> {
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