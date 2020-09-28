package com.example.inventaristoko.Screens.Kategori;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventaristoko.Adapter.Makanan.MakananAdapter;
import com.example.inventaristoko.Model.Makanan.Makanan;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.VolleyAPI;
import com.example.inventaristoko.Utils.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

public class KategoriDetailActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MakananAdapter mMakananAdapter;
    private Button btnHapusKategori, btnUbahKategori;
    private TextView tvNamaKategori, tvIdKategori;
    private String idKategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori_detail);

        getSupportActionBar().setTitle(R.string.menu_detail_kategori);

        tvNamaKategori = findViewById(R.id.tvValueNamaKategori);
        tvIdKategori = findViewById(R.id.tvValueIdKategori);
        btnHapusKategori = findViewById(R.id.btnHapusKategori);
        btnUbahKategori = findViewById(R.id.btnUbahKategori);

        Bundle bundle = getIntent().getExtras();
        tvNamaKategori.setText(bundle.getString("namaKategori"));
        tvIdKategori.setText(bundle.getString("idKategori"));
        idKategori = bundle.getString("idKategori");

        btnHapusKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(KategoriDetailActivity.this);
                builder.setMessage("Anda Yakin Ingin Menghapus Data ini?");
                builder.setCancelable(false);
                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CommonUtils.showLoading(KategoriDetailActivity.this);
                        VolleyAPI volleyAPI = new VolleyAPI(KategoriDetailActivity.this);
                        Map<String, String> params = new HashMap<>();
                        params.put("jenis_menu_id", idKategori);

                        volleyAPI.putRequest("deleteJenisMenu", params, new VolleyCallback() {
                            @Override
                            public void onSuccessResponse(String result) {
                                try {
                                    JSONObject resultJSON = new JSONObject(result);
                                    Intent myIntent = new Intent(getApplicationContext(), KategoriActivity.class);
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

        btnUbahKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent (v.getContext(), KategoriEntryActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("screenState", MyConstants.UBAH_KATEGORI);
//                bundle.putString("userId", tvMejaId.getText().toString());
//                intent.putExtras(bundle);
//                v.getContext().startActivity(intent);

                Toast.makeText(getApplicationContext(), "Ubah Kategori", Toast.LENGTH_SHORT).show();
            }
        });

        mRecyclerView = findViewById(R.id.rvKategoriDetailMakanan);
        ButterKnife.bind(this);
        setUp();
    }

    private void setUp() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mMakananAdapter = new MakananAdapter(new ArrayList<>());

        prepareDataMakanan();
    }

    private void prepareDataMakanan() {
        CommonUtils.showLoading(KategoriDetailActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(this);
        Map<String, String> params = new HashMap<>();
        params.put("jenis_menu_id", idKategori);
        volleyAPI.getRequest("getSemuaJenisMenuDetail", params, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                try {
                    ArrayList<Makanan> mMakanan = new ArrayList<>();
                    JSONObject resultJSON = new JSONObject(result);
                    JSONArray resultArray = resultJSON.getJSONArray("listMakanan");
                    for(int i = 0 ; i < resultArray.length() ; i ++ ) {
                        JSONObject dataMakanan = (JSONObject) resultArray.get(i);
                        Makanan makanan = new Makanan();
                        makanan.setId(String.valueOf(i+1));
                        makanan.setIdMakanan(dataMakanan.getString("makanan_id"));
                        makanan.setNamaMakanan(dataMakanan.getString("nama"));
                        makanan.setHargaMakanan(dataMakanan.getString("harga_jual"));
                        makanan.setGambarMakanan(dataMakanan.getString("gambar_makanan"));
                        makanan.setTanggalTambahMakanan(dataMakanan.getString("created_at"));
                        makanan.setTanggalUbahMakanan(dataMakanan.getString("updated_at"));

                        mMakanan.add(makanan);
                    }
                    mMakananAdapter.addItems(mMakanan);
                    mRecyclerView.setAdapter(mMakananAdapter);
                    CommonUtils.hideLoading();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
