package com.example.inventaristoko.Screens.Makanan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
        decodeImage = bundle.getString("gambarMakanan");
        tvHargaMakanan.setText( CommonUtils.currencyFormat(hargaMakanan));

        byte[] decodedString = Base64.decode(decodeImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ivGambarMakanan.setImageBitmap(decodedByte);

        btnHapusMakanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MakananDetailActivity.this);
                builder.setMessage("Anda Yakin Ingin Menghapus Data ini?");
                builder.setCancelable(false);
                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CommonUtils.showLoading(MakananDetailActivity.this);
                        VolleyAPI volleyAPI = new VolleyAPI(MakananDetailActivity.this);
                        Map<String, String> params = new HashMap<>();
                        params.put("makanan_id", idMakanan);

                        volleyAPI.putRequest("deleteMakanan", params, new VolleyCallback() {
                            @Override
                            public void onSuccessResponse(String result) {
                                try {
                                    JSONObject resultJSON = new JSONObject(result);
                                    Intent myIntent = new Intent(getApplicationContext(), MakananActivity.class);
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

        btnUbahMakanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), MakananEntryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("screenState", MyConstants.UBAH_MAKANAN);
                bundle.putString("idMakanan", idMakanan);
                bundle.putString("namaMakanan", tvNamaMakanan.getText().toString());
                bundle.putString("hargaMakanan", hargaMakanan);
                bundle.putString("gambarMakanan", decodeImage);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);

                Toast.makeText(getApplicationContext(), "Ubah Makanan", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
