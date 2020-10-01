package com.example.inventaristoko.Screens.Pengguna;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.VolleyAPI;

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

        btnHapusPengguna.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage(R.string.confirmation_dialog_delete);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.label_yes, (dialog, which) -> {
                CommonUtils.showLoading(v.getContext());
                VolleyAPI volleyAPI = new VolleyAPI(v.getContext());

                Map<String, String> params = new HashMap<>();
                params.put("user_name", tvUsernamePengguna.getText().toString());

                volleyAPI.putRequest("deleteAdmin", params, result -> {
                    try {
                        JSONObject resultJSON = new JSONObject(result);
                        Intent myIntent = new Intent(getApplicationContext(), PenggunaActivity.class);
                        startActivityForResult(myIntent, 0);
                        Toast.makeText(getApplicationContext(), resultJSON.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            });
            builder.setNegativeButton(R.string.label_no, (dialog, which) -> {
            });
            builder.show();
        });

        btnUbahPengguna.setOnClickListener(v -> {
            Intent intent = new Intent (v.getContext(), PenggunaEntryActivity.class);
            Bundle bundle1 = new Bundle();
            bundle1.putString("screenState", MyConstants.UBAH_PENGGUNA);
            bundle1.putString("namaPengguna", tvNamaPengguna.getText().toString());
            bundle1.putString("usernamePengguna", tvUsernamePengguna.getText().toString());
            bundle1.putString("emailPengguna", tvEmailPengguna.getText().toString());
            bundle1.putString("nomorTeleponPengguna", tvNomorTeleponPengguna.getText().toString());
            bundle1.putString("tanggalLahirPengguna", tvTanggalLahirPengguna.getText().toString());
            intent.putExtras(bundle1);
            v.getContext().startActivity(intent);
        });
    }
}