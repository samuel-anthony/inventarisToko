package com.example.inventaristoko.Screens.Meja;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.VolleyAPI;
import com.example.inventaristoko.Utils.VolleyCallback;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import android.graphics.Bitmap;
import android.widget.ImageView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MejaDetailActivity extends AppCompatActivity {
    private Button btnHapusMeja, btnUbahMeja;
    private TextView tvIdMeja, tvNamaMeja;
    private ImageView ivQrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meja_detail);

        getSupportActionBar().setTitle(R.string.menu_detail_meja);

        tvIdMeja = findViewById(R.id.tvValueIdMeja);
        tvNamaMeja = findViewById(R.id.tvValueNamaMeja);

        ivQrCode = findViewById(R.id.ivQrCode);

        btnHapusMeja = findViewById(R.id.btnHapusMeja);
        btnUbahMeja = findViewById(R.id.btnUbahMeja);

        Bundle bundle = getIntent().getExtras();
        tvIdMeja.setText(bundle.getString("idMeja"));
        tvNamaMeja.setText(bundle.getString("namaMeja"));

        String text = tvIdMeja.getText().toString();
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,150,150);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            ivQrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        btnHapusMeja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MejaDetailActivity.this);
                builder.setMessage("Anda Yakin Ingin Menghapus Data ini?");
                builder.setCancelable(false);
                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CommonUtils.showLoading(MejaDetailActivity.this);
                        VolleyAPI volleyAPI = new VolleyAPI(MejaDetailActivity.this);
                        Map<String, String> params = new HashMap<>();
                        params.put("user_name", tvIdMeja.getText().toString());

                        volleyAPI.putRequest("deleteUser", params, new VolleyCallback() {
                            @Override
                            public void onSuccessResponse(String result) {
                                try {
                                    JSONObject resultJSON = new JSONObject(result);
                                    Intent myIntent = new Intent(getApplicationContext(), MejaActivity.class);
                                    startActivityForResult(myIntent, 0);
                                    Toast.makeText(getApplicationContext(), resultJSON.getString("message"), Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });

        btnUbahMeja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), MejaEntryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("screenState", MyConstants.UBAH_MEJA);
                bundle.putString("idMeja", tvIdMeja.getText().toString());
                bundle.putString("namaMeja", tvNamaMeja.getText().toString());
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
    }
}