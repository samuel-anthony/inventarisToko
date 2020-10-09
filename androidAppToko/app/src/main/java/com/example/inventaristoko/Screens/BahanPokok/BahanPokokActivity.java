package com.example.inventaristoko.Screens.BahanPokok;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.inventaristoko.Adapter.BahanPokok.BahanPokokAdapter;
import com.example.inventaristoko.Model.BahanPokok.BahanPokok;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class BahanPokokActivity extends AppCompatActivity implements View.OnClickListener {
    private FloatingActionButton fabDataBahanPokok;
    private RecyclerView rvDataBahanPokok;
    private BahanPokokAdapter bahanPokokAdapter;
    private Button btnTambahBahanPokok;
    private JSONArray elementDownload = new JSONArray();

    private void init() {
        rvDataBahanPokok = findViewById(R.id.rvDataBahanPokok);
        fabDataBahanPokok = findViewById(R.id.fabDataBahanPokok);
        btnTambahBahanPokok = findViewById(R.id.btnTambahBahanPokok);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bahan_pokok);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_detail_bahan_pokok);

        init();

        fabDataBahanPokok.setOnClickListener(this);
        btnTambahBahanPokok.setOnClickListener(this);

        setUp();
    }

    private void setUp() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvDataBahanPokok.setLayoutManager(mLayoutManager);
        rvDataBahanPokok.setItemAnimator(new DefaultItemAnimator());
        bahanPokokAdapter = new BahanPokokAdapter(new ArrayList<>());

        callDataBahanPokokRequest();
    }

    private void callDataBahanPokokRequest() {
        CommonUtils.showLoading(BahanPokokActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(this);

        Map<String, String> params = new HashMap<>();

        volleyAPI.getRequest(MyConstants.BAHAN_POKOK_GET_ACTION, params, result -> {
            try {
                ArrayList<BahanPokok> mBahanPokok = new ArrayList<>();
                JSONObject resultJSON = new JSONObject(result);
                JSONArray resultArray = resultJSON.getJSONArray("result");

                for(int i = 0 ; i < resultArray.length() ; i ++ ) {
                    JSONObject dataBahanPokok = (JSONObject) resultArray.get(i);
                    BahanPokok bahanPokok = new BahanPokok();
                    bahanPokok.setId(String.valueOf(i+1));
                    bahanPokok.setIdBahanPokok(dataBahanPokok.getString("bahan_pokok_id"));
                    bahanPokok.setNamaBahanPokok(dataBahanPokok.getString("nama"));
                    bahanPokok.setJumlahBahanPokok(dataBahanPokok.getString("jumlah"));
                    bahanPokok.setSatuanBahanPokok(dataBahanPokok.getString("satuan"));
                    bahanPokok.setTanggalTambahBahanPokok(dataBahanPokok.getString("created_at"));
                    bahanPokok.setTanggalUbahBahanPokok(dataBahanPokok.getString("updated_at"));

                    JSONObject elementToDownload = new JSONObject();
                    elementToDownload.put("number",i+1);
                    elementToDownload.put("nama",dataBahanPokok.getString("nama"));
                    elementToDownload.put("jumlah",dataBahanPokok.getString("jumlah") + " " +dataBahanPokok.getString("satuan"));
                    String valueMakanan = "";
                    JSONArray makanan = dataBahanPokok.getJSONArray("makanans");
                    for(int j = 0 ; j < makanan.length() ; j ++){
                        valueMakanan = valueMakanan + makanan.get(j) + (j == makanan.length()-1 ? "" : "\n");
                    }
                    elementToDownload.put("makanan",valueMakanan);
                    elementToDownload.put("enter",makanan.length()  == 0 ? 1 :makanan.length());
                    elementDownload.put(elementToDownload);
                    mBahanPokok.add(bahanPokok);
                }

                bahanPokokAdapter.addItems(mBahanPokok);
                rvDataBahanPokok.setAdapter(bahanPokokAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        CommonUtils.hideLoading();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabDataBahanPokok :
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage(R.string.confirmation_dialog_download);
                    builder.setCancelable(false);
                    builder.setPositiveButton(R.string.label_yes, (dialog, which) -> {
                        PDFDownload pdf = new PDFDownload("Data Bahan Pokok");

                        List<String> columnName = new ArrayList<>();
                        columnName.add("number");
                        columnName.add("nama");
                        columnName.add("jumlah");
                        columnName.add("bahan untuk makanan");

                        List<String> key = new ArrayList<>();
                        key.add("number");
                        key.add("nama");
                        key.add("jumlah");
                        key.add("makanan");

                        try {
                            pdf.download(columnName, key, elementDownload, this);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                    builder.setNegativeButton(R.string.label_no, (dialog, which) -> {
                    });
                    builder.show();
                }
                break;
            case R.id.btnTambahBahanPokok :
                Intent intent = new Intent(v.getContext(), BahanPokokEntryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("screenState", MyConstants.TAMBAH_BAHAN_POKOK);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
                break;
        }
    }
}