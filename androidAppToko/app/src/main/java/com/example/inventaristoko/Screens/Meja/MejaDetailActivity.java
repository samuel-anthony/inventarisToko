package com.example.inventaristoko.Screens.Meja;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MejaDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnHapusMeja, btnUbahMeja;
    private TextView tvIdMeja, tvNamaMeja;
    private ImageView ivQrCode;
    private String txtIdMeja, txtNamaMeja;

    private void init() {
        tvIdMeja = findViewById(R.id.tvValueIdMeja);
        tvNamaMeja = findViewById(R.id.tvValueNamaMeja);
        ivQrCode = findViewById(R.id.ivQrCode);
        btnHapusMeja = findViewById(R.id.btnHapusMeja);
        btnUbahMeja = findViewById(R.id.btnUbahMeja);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meja_detail);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_detail_meja);

        init();

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        tvIdMeja.setText(bundle.getString("idMeja"));
        tvNamaMeja.setText(bundle.getString("namaMeja"));

        txtIdMeja = tvIdMeja.getText().toString();
        txtNamaMeja = tvNamaMeja.getText().toString();

        btnHapusMeja.setOnClickListener(this);
        btnUbahMeja.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(txtIdMeja, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            ivQrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUbahMeja :
                Intent intent = new Intent (v.getContext(), MejaEntryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("screenState", MyConstants.UBAH_MEJA);
                bundle.putString("idMeja", txtIdMeja);
                bundle.putString("namaMeja", txtNamaMeja);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
                break;
            case R.id.btnHapusMeja :
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage(R.string.confirmation_dialog_delete);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.label_yes, (dialog, which) -> callDeleteDataMejaRequest(v.getContext()));
                builder.setNegativeButton(R.string.label_no, (dialog, which) -> {
                });
                builder.show();
                break;
        }
    }

    private void callDeleteDataMejaRequest(Context context) {
        CommonUtils.showLoading(context);
        VolleyAPI volleyAPI = new VolleyAPI(context);

        Map<String, String> params = new HashMap<>();
        params.put("user_name", txtIdMeja);

        volleyAPI.putRequest(MyConstants.MEJA_DELETE_ACTION, params, result -> {
            try {
                JSONObject resultJSON = new JSONObject(result);
                if(resultJSON.getString("is_error").equalsIgnoreCase("0")) {
                    Preferences.clearLoggedInUser(getBaseContext());
                    startActivity(new Intent(context, MejaActivity.class));
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