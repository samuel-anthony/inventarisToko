package com.example.inventaristoko.Screens.Kategori;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.inventaristoko.Adapter.Makanan.MakananAdapter;
import com.example.inventaristoko.Model.Makanan.Makanan;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.VolleyAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class KategoriDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<Makanan> mMakanan = new ArrayList<>();
    private RecyclerView rvKategoriDetail;
    private MakananAdapter makananAdapter;
    private Button btnHapusKategori, btnUbahKategori;
    private TextView tvNamaKategori, tvIdKategori;
    private String idKategori;

    private void init() {
        rvKategoriDetail = findViewById(R.id.rvKategoriDetail);
        tvNamaKategori = findViewById(R.id.tvValueNamaKategori);
        tvIdKategori = findViewById(R.id.tvValueIdKategori);
        btnHapusKategori = findViewById(R.id.btnHapusKategori);
        btnUbahKategori = findViewById(R.id.btnUbahKategori);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori_detail);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_detail_kategori);

        init();

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        tvNamaKategori.setText(bundle.getString("namaKategori"));
        tvIdKategori.setText(bundle.getString("idKategori"));
        idKategori = bundle.getString("idKategori");

        btnHapusKategori.setOnClickListener(this);
        btnUbahKategori.setOnClickListener(this);

        setUp();
    }

    private void setUp() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvKategoriDetail.setLayoutManager(mLayoutManager);
        rvKategoriDetail.setItemAnimator(new DefaultItemAnimator());
        makananAdapter = new MakananAdapter(new ArrayList<>());

        callDataKategoriDetailRequest();
    }

    private void callDataKategoriDetailRequest() {
        CommonUtils.showLoading(KategoriDetailActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(this);

        Map<String, String> params = new HashMap<>();
        params.put("jenis_menu_id", idKategori);

        volleyAPI.getRequest(MyConstants.KATEGORI_GET_DETAILS_ACTION, params, result -> {
            try {
                JSONObject resultJSON = new JSONObject(result);
                JSONObject resultArray = resultJSON.getJSONObject("result");
                JSONArray makananSelected = resultArray.getJSONArray("makanan");

                for(int i = 0 ; i < makananSelected.length() ; i ++ ) {
                    JSONObject dataMakanan = (JSONObject) makananSelected.get(i);
                    Makanan makanan = new Makanan();
                    makanan.setId(String.valueOf(i+1));
                    makanan.setIdMakanan(dataMakanan.getString("makanan_id"));
                    makanan.setNamaMakanan(dataMakanan.getString("nama"));
                    makanan.setHargaMakanan(dataMakanan.getString("harga_jual"));
                    makanan.setTanggalTambahMakanan(dataMakanan.getString("created_at"));
                    makanan.setTanggalUbahMakanan(dataMakanan.getString("updated_at"));

                    mMakanan.add(makanan);
                }

                makananAdapter.addItems(mMakanan);
                rvKategoriDetail.setAdapter(makananAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        CommonUtils.hideLoading();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnHapusKategori :
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage(R.string.confirmation_dialog_delete);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.label_yes, (dialog, which) -> callDeleteDataPenggunaRequest(v.getContext()));
                builder.setNegativeButton(R.string.label_no, (dialog, which) -> {
                });
                builder.show();
                break;
            case R.id.btnUbahKategori :
                Intent intent = new Intent (v.getContext(), KategoriEntryActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("screenState", MyConstants.UBAH_KATEGORI);
                mBundle.putString("idKategori", tvIdKategori.getText().toString());
                mBundle.putString("namaKategori", tvNamaKategori.getText().toString());

                if(mMakanan != null) {
                    mBundle.putSerializable("daftarMakananSelected", mMakanan);
                }

                intent.putExtras(mBundle);
                v.getContext().startActivity(intent);
                break;
        }
    }

    private void callDeleteDataPenggunaRequest(Context context) {
        CommonUtils.showLoading(context);
        VolleyAPI volleyAPI = new VolleyAPI(context);

        Map<String, String> params = new HashMap<>();
        params.put("jenis_menu_id", idKategori);

        volleyAPI.putRequest(MyConstants.KATEGORI_DELETE_ACTION, params, result -> {
            try {
                JSONObject resultJSON = new JSONObject(result);
                Intent myIntent = new Intent(getApplicationContext(), KategoriActivity.class);
                startActivityForResult(myIntent, 0);

                CommonUtils.showToast(context, resultJSON.getString("message"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        CommonUtils.hideLoading();
    }
}
