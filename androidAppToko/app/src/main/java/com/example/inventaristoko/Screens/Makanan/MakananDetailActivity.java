package com.example.inventaristoko.Screens.Makanan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventaristoko.Model.BahanPokok.BahanPokok;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Screens.Front.HomeActivity;
import com.example.inventaristoko.Screens.Penjualan.PenjualanActivity;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.Preferences;
import com.example.inventaristoko.Utils.VolleyAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MakananDetailActivity extends AppCompatActivity {
    private Button btnHapusMakanan, btnUbahMakanan;
    private TextView tvNamaMakanan, tvHargaMakanan;
    private ImageView ivGambarMakanan;
    private String idMakanan, hargaMakanan, decodeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makanan_detail);

        getSupportActionBar().setTitle(R.string.menu_detail_makanan);

        tvNamaMakanan = findViewById(R.id.tvValueNamaMakanan);
        tvHargaMakanan = findViewById(R.id.tvValueHargaMakanan);
        ivGambarMakanan = findViewById(R.id.ivGambarMakanan);
        btnHapusMakanan = findViewById(R.id.btnHapusMakanan);
        btnUbahMakanan = findViewById(R.id.btnUbahMakanan);

        Bundle bundle = getIntent().getExtras();
        idMakanan = bundle.getString("idMakanan");
        tvNamaMakanan.setText(bundle.getString("namaMakanan"));
        hargaMakanan = bundle.getString("hargaMakanan");
        tvHargaMakanan.setText( CommonUtils.currencyFormat(hargaMakanan));

        getGambarMakananDetail();

        btnHapusMakanan.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage(R.string.confirmation_dialog_delete);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.label_yes, (dialog, which) -> {
                CommonUtils.showLoading(v.getContext());
                VolleyAPI volleyAPI = new VolleyAPI(v.getContext());

                Map<String, String> params = new HashMap<>();
                params.put("makanan_id", idMakanan);

                volleyAPI.putRequest("deleteMakanan", params, result -> {
                    try {
                        JSONObject resultJSON = new JSONObject(result);
                        Intent myIntent = new Intent(getApplicationContext(), MakananActivity.class);
                        startActivityForResult(myIntent, 0);
                        Toast.makeText(getApplicationContext(), resultJSON.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
                CommonUtils.hideLoading();
            });
            builder.setNegativeButton(R.string.label_no, (dialog, which) -> {
            });
            builder.show();
        });

        btnUbahMakanan.setOnClickListener(v -> {
            Intent intent = new Intent (v.getContext(), MakananEntryActivity.class);
            Bundle bundle1 = new Bundle();
            bundle1.putString("screenState", MyConstants.UBAH_MAKANAN);
            bundle1.putString("idMakanan", idMakanan);
            bundle1.putString("namaMakanan", tvNamaMakanan.getText().toString());
            bundle1.putString("hargaMakanan", hargaMakanan);
            bundle1.putString("gambarMakanan", decodeImage);
            intent.putExtras(bundle1);
            v.getContext().startActivity(intent);

            Toast.makeText(getApplicationContext(), "Ubah Makanan", Toast.LENGTH_SHORT).show();
        });
    }

    private void getGambarMakananDetail() {
        VolleyAPI volleyAPI = new VolleyAPI(MakananDetailActivity.this);

        Map<String, String> params = new HashMap<>();
        params.put("makanan_id", idMakanan);

        volleyAPI.getRequest("getGambarMakananDetail", params, result -> {
            try {
                JSONObject resultJSON = new JSONObject(result);
                decodeImage = resultJSON.getJSONObject("result").getString("gambar_makanan");

                if(decodeImage != null) {
                    decodeImage.replace("\n", "");
                    byte[] decodedString = Base64.decode(decodeImage, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    ivGambarMakanan.setImageBitmap(decodedByte);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }
}
