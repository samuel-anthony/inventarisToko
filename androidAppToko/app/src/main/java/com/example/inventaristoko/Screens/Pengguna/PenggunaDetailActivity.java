package com.example.inventaristoko.Screens.Pengguna;

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

public class PenggunaDetailActivity extends AppCompatActivity {
    private Button btnHapusPengguna, btnUbahPengguna;
    private TextView tvNamaPengguna, tvUsernamePengguna, tvEmailPengguna, tvNomorTeleponPengguna, tvTanggalLahirPengguna;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengguna_detail);

        getSupportActionBar().setTitle(R.string.menu_detail_pengguna);

        tvNamaPengguna = findViewById(R.id.tvValueNamaPengguna);
        tvUsernamePengguna = findViewById(R.id.tvValueUsernamePengguna);
        tvEmailPengguna = findViewById(R.id.tvValueEmailPengguna);
        tvNomorTeleponPengguna = findViewById(R.id.tvValueNomorTeleponPengguna);
        tvTanggalLahirPengguna = findViewById(R.id.tvValueTanggalLahirPengguna);

        btnHapusPengguna = findViewById(R.id.btnHapusPengguna);
        btnUbahPengguna = findViewById(R.id.btnUbahPengguna);

        Bundle bundle = getIntent().getExtras();
        tvNamaPengguna.setText(bundle.getString("namaPengguna"));
        tvUsernamePengguna.setText(bundle.getString("usernamePengguna"));
        tvEmailPengguna.setText(bundle.getString("emailPengguna"));
        tvNomorTeleponPengguna.setText(bundle.getString("nomorTeleponPengguna"));
        tvTanggalLahirPengguna.setText(bundle.getString("tanggalLahirPengguna"));

        btnHapusPengguna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PenggunaDetailActivity.this);
                builder.setMessage("Anda Yakin Ingin Menghapus Data ini?");
                builder.setCancelable(false);
                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CommonUtils.showLoading(PenggunaDetailActivity.this);
                        VolleyAPI volleyAPI = new VolleyAPI(PenggunaDetailActivity.this);
                        Map<String, String> params = new HashMap<>();
                        params.put("user_name", tvUsernamePengguna.getText().toString());

                        volleyAPI.putRequest("deleteAdmin", params, new VolleyCallback() {
                            @Override
                            public void onSuccessResponse(String result) {
                                try {
                                    JSONObject resultJSON = new JSONObject(result);
                                    Intent myIntent = new Intent(getApplicationContext(), PenggunaActivity.class);
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

        btnUbahPengguna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), PenggunaEntryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("screenState", MyConstants.UBAH_PENGGUNA);
                bundle.putString("namaPengguna", tvNamaPengguna.getText().toString());
                bundle.putString("usernamePengguna", tvUsernamePengguna.getText().toString());
                bundle.putString("emailPengguna", tvEmailPengguna.getText().toString());
                bundle.putString("nomorTeleponPengguna", tvNomorTeleponPengguna.getText().toString());
                bundle.putString("tanggalLahirPengguna", tvTanggalLahirPengguna.getText().toString());
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
    }
}