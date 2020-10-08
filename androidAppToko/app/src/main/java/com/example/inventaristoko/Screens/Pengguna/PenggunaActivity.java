package com.example.inventaristoko.Screens.Pengguna;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.inventaristoko.Adapter.Pengguna.PenggunaAdapter;
import com.example.inventaristoko.Model.Pengguna.Pengguna;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.PDFDownload;
import com.example.inventaristoko.Utils.VolleyAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class PenggunaActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView rvDataPengguna;
    private FloatingActionButton fabDataPengguna;
    private PenggunaAdapter penggunaAdapter;
    private Button btnTambahPengguna;
    private JSONArray elementDownload = new JSONArray();

    private void init() {
        rvDataPengguna = findViewById(R.id.rvDataPengguna);
        fabDataPengguna = findViewById(R.id.fabDataPengguna);
        btnTambahPengguna = findViewById(R.id.btnTambahPengguna);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengguna);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_pengguna);

        init();

        fabDataPengguna.setOnClickListener(this);
        btnTambahPengguna.setOnClickListener(this);

        setUp();
    }

    private void setUp() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvDataPengguna.setLayoutManager(mLayoutManager);
        rvDataPengguna.setItemAnimator(new DefaultItemAnimator());
        penggunaAdapter = new PenggunaAdapter(new ArrayList<>());

        callDataPenggunaRequest();
    }

    private void callDataPenggunaRequest() {
        CommonUtils.showLoading(PenggunaActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(this);

        Map<String, String> params = new HashMap<>();

        volleyAPI.getRequest(MyConstants.PENGGUNA_GET_ACTION, params, result -> {
            try {
                ArrayList<Pengguna> mPengguna = new ArrayList<>();
                JSONObject resultJSON = new JSONObject(result);
                JSONArray resultArray = resultJSON.getJSONArray("result");

                for(int i = 0 ; i < resultArray.length() ; i ++ ) {
                    JSONObject dataPengguna = (JSONObject) resultArray.get(i);
                    Pengguna pengguna = new Pengguna();
                    pengguna.setId(String.valueOf(i+1));
                    pengguna.setNamaPengguna(dataPengguna.getString("full_name"));
                    pengguna.setUsernamePengguna(dataPengguna.getString("user_name"));
                    pengguna.setEmailPengguna(dataPengguna.getString("email"));
                    pengguna.setNomorTeleponPengguna(dataPengguna.getString("phone_number"));
                    pengguna.setTanggalLahirPengguna(dataPengguna.getString("birth_date"));
                    pengguna.setTanggalTambahPengguna(dataPengguna.getString("created_at"));
                    pengguna.setTanggalUbahPengguna(dataPengguna.getString("updated_at"));

                    JSONObject elementToDownload = new JSONObject();
                    elementToDownload.put("number",i+1);
                    elementToDownload.put("full_name",dataPengguna.getString("full_name"));
                    elementToDownload.put("user_name",dataPengguna.getString("user_name"));
                    elementToDownload.put("email",dataPengguna.getString("email"));
                    elementToDownload.put("phone_number",dataPengguna.getString("phone_number"));
                    elementToDownload.put("birth_date",dataPengguna.getString("birth_date"));
                    elementDownload.put(elementToDownload);
                    mPengguna.add(pengguna);
                }

                penggunaAdapter.addItems(mPengguna);
                rvDataPengguna.setAdapter(penggunaAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        CommonUtils.hideLoading();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabDataPengguna :
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage(R.string.confirmation_dialog_download);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.label_yes, (dialog, which) -> {
                    PDFDownload pdf = new PDFDownload("Pengguna");

                    List<String> columnName = new ArrayList<>();
                    columnName.add("number");
                    columnName.add("full name");
                    columnName.add("user name");
                    columnName.add("nomer telepon");
                    columnName.add("tanggal lahir");
                    columnName.add("email");

                    List<String> key = new ArrayList<>();
                    key.add("number");
                    key.add("full_name");
                    key.add("user_name");
                    key.add("phone_number");
                    key.add("birth_date");
                    key.add("email");

                    try {
                        pdf.download(columnName, key, elementDownload, this);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
                builder.setNegativeButton(R.string.label_no, (dialog, which) -> {
                });
                builder.show();
                break;
            case R.id.btnTambahPengguna :
                Intent intent = new Intent (v.getContext(), PenggunaEntryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("screenState", MyConstants.TAMBAH_PENGGUNA);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
                break;
        }
    }
}
