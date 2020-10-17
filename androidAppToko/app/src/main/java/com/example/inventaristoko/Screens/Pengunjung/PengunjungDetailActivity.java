package com.example.inventaristoko.Screens.Pengunjung;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class PengunjungDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private Context appContext;
    private TextView tvNamaMakananPengunjungDetail, tvHargaMakananPengunjungDetail, tvJumlahMakananPengunjungDetail, tvTotalMakananPengunjungDetail;
    private ImageView ivGambarMakananPengunjungDetail;
    private EditText etCatatanMakananPengunjungDetail;
    private Button btnKurangJumlah, btnTambahJumlah, btnTambahPesanan;
    private String idMakanan, hargaMakanan, decodeImage, txtCatatanMakananPengunjungDetail;
    private int amount = 1;
    private int harga;

    private void init() {
        appContext = getApplicationContext();
        tvNamaMakananPengunjungDetail = findViewById(R.id.tvNamaMakananPengunjungDetail);
        tvHargaMakananPengunjungDetail = findViewById(R.id.tvHargaMakananPengunjungDetail);
        tvJumlahMakananPengunjungDetail = findViewById(R.id.tvJumlahMakananPengunjungDetail);
        tvTotalMakananPengunjungDetail = findViewById(R.id.tvTotalMakananPengunjungDetail);
        ivGambarMakananPengunjungDetail = findViewById(R.id.ivGambarMakananPengunjungDetail);
        etCatatanMakananPengunjungDetail = findViewById(R.id.etCatatanMakananPengunjungDetail);
        btnKurangJumlah = findViewById(R.id.btnKurangJumlah);
        btnTambahJumlah = findViewById(R.id.btnTambahJumlah);
        btnTambahPesanan = findViewById(R.id.btnTambahPesanan);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengunjung_detail);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_detail);

        init();

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        idMakanan = bundle.getString("idMakanan");
        hargaMakanan = bundle.getString("hargaMakanan");

        tvNamaMakananPengunjungDetail.setText(bundle.getString("namaMakanan"));
        tvHargaMakananPengunjungDetail.setText(CommonUtils.currencyFormat(hargaMakanan));
        tvJumlahMakananPengunjungDetail.setText(String.valueOf(amount));
        tvTotalMakananPengunjungDetail.setText(CommonUtils.currencyFormat(hargaMakanan));

        harga = Integer.parseInt(Objects.requireNonNull(bundle.getString("hargaMakanan")));

        btnKurangJumlah.setOnClickListener(this);
        btnTambahJumlah.setOnClickListener(this);
        btnTambahPesanan.setOnClickListener(this);

        etCatatanMakananPengunjungDetail.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                CommonUtils.hideKeyboard(appContext, v);
            }
        });

        getGambarMakananDetail();
    }

    private void getGambarMakananDetail() {
        CommonUtils.showLoading(PengunjungDetailActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(PengunjungDetailActivity.this);

        Map<String, String> params = new HashMap<>();
        params.put("makanan_id", idMakanan);

        volleyAPI.getRequest(MyConstants.MAKANAN_GET_IMAGE_DETAIL_ACTION, params, result -> {
            decodeImage =  result;

            if(decodeImage != null) {
                byte[] decodedString = Base64.decode(decodeImage, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                ivGambarMakananPengunjungDetail.setImageBitmap(decodedByte);
            }
        });

        CommonUtils.hideLoading();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnKurangJumlah :
                if(amount == 1) {
                    CommonUtils.showToast(appContext, appContext.getString(R.string.label_jumlah_makanan));
                    return;
                }

                amount = amount - 1;
                tvJumlahMakananPengunjungDetail.setText(String.valueOf(amount));

                int totalHarga = amount * harga;

                tvTotalMakananPengunjungDetail.setText(CommonUtils.currencyFormat(String.valueOf(totalHarga)));
                break;
            case R.id.btnTambahJumlah :
                amount = amount + 1;
                tvJumlahMakananPengunjungDetail.setText(String.valueOf(amount));

                totalHarga = amount * harga;

                tvTotalMakananPengunjungDetail.setText(CommonUtils.currencyFormat(String.valueOf(totalHarga)));
                break;
            case R.id.btnTambahPesanan :
                txtCatatanMakananPengunjungDetail = etCatatanMakananPengunjungDetail.getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage(R.string.confirmation_dialog_add);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.label_yes, (dialog, which) -> callAddDataMakananRequest(v.getContext()));
                builder.setNegativeButton(R.string.label_no, (dialog, which) -> {
                });
                builder.show();
                break;
        }
    }

    private void callAddDataMakananRequest(Context context) {
        CommonUtils.showLoading(context);
        VolleyAPI volleyAPI = new VolleyAPI(context);

        Map<String, String> params = new HashMap<>();
        params.put("user_id", Preferences.getLoggedInUserCustomer(getBaseContext()));
        params.put("makanan_id", idMakanan);
        params.put("harga_makanan", hargaMakanan);
        params.put("jumlah", String.valueOf(amount));
        params.put("notes", txtCatatanMakananPengunjungDetail);

        volleyAPI.putRequest(MyConstants.PENGUNJUNG_ADD_CART_ACTION, params, result -> {
            try {
                JSONObject resultJSON = new JSONObject(result);
                Intent myIntent = new Intent(getApplicationContext(), PengunjungActivity.class);
                startActivityForResult(myIntent, 0);

                CommonUtils.showToast(context, resultJSON.getString("message"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        CommonUtils.hideLoading();
    }
}