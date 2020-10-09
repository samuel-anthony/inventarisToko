package com.example.inventaristoko.Screens.Makanan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.inventaristoko.Adapter.Makanan.MakananAdapter;
import com.example.inventaristoko.Model.Makanan.Makanan;
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


public class MakananActivity extends AppCompatActivity implements View.OnClickListener {
    private FloatingActionButton fabDataMakanan;
    private RecyclerView rvDataMakanan;
    private MakananAdapter makananAdapter;
    private Button btnTambahMakanan;
    private JSONArray elementDownload = new JSONArray();

    private void init() {
        fabDataMakanan = findViewById(R.id.fabDataMakanan);
        rvDataMakanan = findViewById(R.id.rvDataMakanan);
        btnTambahMakanan = findViewById(R.id.btnTambahMakanan);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makanan);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_makanan);

        init();

        fabDataMakanan.setOnClickListener(this);
        btnTambahMakanan.setOnClickListener(this);

        setUp();
    }

    private void setUp() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvDataMakanan.setLayoutManager(mLayoutManager);
        rvDataMakanan.setItemAnimator(new DefaultItemAnimator());
        makananAdapter = new MakananAdapter(new ArrayList<>());

        callDataMakananRequest();
    }

    private void callDataMakananRequest() {
        CommonUtils.showLoading(MakananActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(this);

        Map<String, String> params = new HashMap<>();

        volleyAPI.getRequest(MyConstants.MAKANAN_GET_ACTION, params, result -> {
            try {
                ArrayList<Makanan> mMakanan = new ArrayList<>();
                JSONObject resultJSON = new JSONObject(result);
                JSONArray resultArray = resultJSON.getJSONArray("result");

                for(int i = 0 ; i < resultArray.length() ; i ++ ) {
                    JSONObject dataMakanan = (JSONObject) resultArray.get(i);
                    Makanan makanan = new Makanan();
                    makanan.setId(String.valueOf(i+1));
                    makanan.setIdMakanan(dataMakanan.getString("makanan_id"));
                    makanan.setNamaMakanan(dataMakanan.getString("nama"));
                    makanan.setHargaMakanan(dataMakanan.getString("harga_jual"));
                    makanan.setTanggalTambahMakanan(dataMakanan.getString("created_at"));
                    makanan.setTanggalUbahMakanan(dataMakanan.getString("updated_at"));

                    JSONObject elementToDownload = new JSONObject();
                    elementToDownload.put("number",i+1);
                    elementToDownload.put("nama",dataMakanan.getString("nama"));
                    elementToDownload.put("harga_jual",dataMakanan.getString("harga_jual"));
                    String valueMakanan = "";
                    JSONArray bahanPokoks = dataMakanan.getJSONArray("bahanPokok");
                    for(int j = 0 ; j < bahanPokoks.length() ; j ++){
                        valueMakanan = valueMakanan + bahanPokoks.get(j) + (j == bahanPokoks.length()-1 ? "" : "\n");
                    }
                    elementToDownload.put("bahanPokok",valueMakanan);
                    elementToDownload.put("enter",bahanPokoks.length() == 0 ? 1 : bahanPokoks.length());
                    elementDownload.put(elementToDownload);
                    mMakanan.add(makanan);
                }

                makananAdapter.addItems(mMakanan);
                rvDataMakanan.setAdapter(makananAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            CommonUtils.hideLoading();
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabDataMakanan :
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
                        PDFDownload pdf = new PDFDownload("Data Makanan");

                        List<String> columnName = new ArrayList<>();
                        columnName.add("number");
                        columnName.add("nama");
                        columnName.add("harga jual");
                        columnName.add("bahan pokok");

                        List<String> key = new ArrayList<>();
                        key.add("number");
                        key.add("nama");
                        key.add("harga_jual");
                        key.add("bahanPokok");

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
            case R.id.btnTambahMakanan :
                Intent intent = new Intent(v.getContext(), MakananEntryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("screenState", MyConstants.TAMBAH_MAKANAN);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
                break;
        }
    }
}
