package com.example.inventaristoko.Screens.Penjualan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventaristoko.Adapter.Penjualan.PenjualanDetailAdapter;
import com.example.inventaristoko.Model.Penjualan.PenjualanDetail;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.VolleyCallback;
import com.example.inventaristoko.Utils.VolleyAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

public class PenjualanDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private RecyclerView mRecyclerView;
    private PenjualanDetailAdapter mPenjualanDetailAdapter;
    private TextView tvIdPenjualan, tvTanggalPenjualan, tvStatusPenjualan, tvTotalHargaPenjualan;
    private String[] statues = { "Dipesan", "Sedang Dibuat"};
    private Button btnSelesaiPesanan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjualan_detail);

        getSupportActionBar().setTitle(R.string.menu_detail_pesanan);

        tvIdPenjualan = findViewById(R.id.tvIdDetailPenjualan);
        tvTanggalPenjualan = findViewById(R.id.tvTanggalDetailPenjualan);
        tvStatusPenjualan = findViewById(R.id.tvStatusDetailPenjualan);
        tvTotalHargaPenjualan = findViewById(R.id.tvTotalHargaDetailPenjualan);

        Bundle bundle = getIntent().getExtras();
        tvIdPenjualan.setText(bundle.getString("idPenjualan"));
        tvTanggalPenjualan.setText(bundle.getString("tanggalTambah"));
        tvTotalHargaPenjualan.setText(bundle.getString("totalHarga"));

        String kodeStatus = bundle.getString("kodeStatus");
        if(kodeStatus.equals(MyConstants.ORDER_CODE)) {
            tvStatusPenjualan.setTextColor(getResources().getColor(R.color.colorBlack));
            tvStatusPenjualan.setText(MyConstants.ORDER_NAME);
        } else if (kodeStatus.equals(MyConstants.GOING_CODE)) {
            tvStatusPenjualan.setTextColor(getResources().getColor(R.color.colorProcess));
            tvStatusPenjualan.setText(MyConstants.GOING_NAME);
        }

        btnSelesaiPesanan = findViewById(R.id.btnSelesaiPesanan);
        btnSelesaiPesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PenjualanDetailActivity.this);
                builder.setMessage("Anda Yakin Ingin Menyelesaikan Pesanan?");
                builder.setCancelable(false);
                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Map<String, String> params = new HashMap<>();
                        params.put("ref_no", tvIdPenjualan.getText().toString());

                        CommonUtils.showLoading(PenjualanDetailActivity.this);
                        VolleyAPI volleyAPI = new VolleyAPI(PenjualanDetailActivity.this);
                        volleyAPI.putRequest("updateStatusPesananSelesai", params, new VolleyCallback() {
                            @Override
                            public void onSuccessResponse(String result) {
                                try {
                                    JSONObject resultJSON = new JSONObject(result);
                                    Intent myIntent = new Intent(getApplicationContext(), PenjualanActivity.class);
                                    startActivityForResult(myIntent, 0);
                                    Toast.makeText(getApplicationContext(), resultJSON.getString("message"), Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        CommonUtils.hideLoading();
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

        Spinner spin = findViewById(R.id.spnDroplistStatus);
        spin.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, statues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        int spinnerPosition = adapter.getPosition((String) tvStatusPenjualan.getText());
        spin.setSelection(spinnerPosition);

        mRecyclerView = findViewById(R.id.rvDataPenjualanDetail);
        ButterKnife.bind(this);
        setUp();
    }

    private void setUp() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mPenjualanDetailAdapter = new PenjualanDetailAdapter(new ArrayList<>());

        prepareDataPenjualanDetail();
    }

    private void prepareDataPenjualanDetail() {
        Map<String, String> params = new HashMap<>();
        params.put("ref_no", tvIdPenjualan.getText().toString());

        VolleyAPI volleyAPI = new VolleyAPI(this);
        volleyAPI.getRequest("getPesananDetailRefNo", params, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                try {
                    ArrayList<PenjualanDetail> mPenjualanDetail = new ArrayList<>();
                    JSONObject resultJSON = new JSONObject(result);
                    JSONArray resultArray = resultJSON.getJSONArray("result");
                    for(int i = 0 ; i < resultArray.length() ; i ++ ) {
                        JSONObject dataPenjualanDetail = (JSONObject) resultArray.get(i);
                        PenjualanDetail penjualanDetail = new PenjualanDetail();
                        penjualanDetail.setIdDetail(String.valueOf(i+1));
                        penjualanDetail.setIdDetailPenjualan(dataPenjualanDetail.getString("pesanan_detail_id"));
                        penjualanDetail.setNamaDetailPenjualan(dataPenjualanDetail.getString("nama"));
                        penjualanDetail.setJumlahDetailPenjualan(dataPenjualanDetail.getString("jumlah"));
                        penjualanDetail.setHargaDetailMakanan(dataPenjualanDetail.getString("harga_makanan"));
                        penjualanDetail.setCatatanDetailPenjualan(dataPenjualanDetail.getString("notes"));
                        penjualanDetail.setTanggalTambahDetailPenjualan(dataPenjualanDetail.getString("created_at"));
                        penjualanDetail.setTanggalUbahDetailPenjualan(dataPenjualanDetail.getString("updated_at"));
                        mPenjualanDetail.add(penjualanDetail);
                    }
                    mPenjualanDetailAdapter.addItems(mPenjualanDetail);
                    mRecyclerView.setAdapter(mPenjualanDetailAdapter);
                    CommonUtils.hideLoading();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PenjualanDetailActivity.this);
        builder.setMessage("Anda Yakin Ingin Mengubah Status Pesanan Menjadi " + tvStatusPenjualan.getText().toString() + "?");
        builder.setCancelable(false);
        builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvStatusPenjualan.setText(statues[position]);

                String statusCode = "";
                if(tvStatusPenjualan.getText() == MyConstants.ORDER_NAME) {
                    statusCode = "001";
                    tvStatusPenjualan.setTextColor(getResources().getColor(R.color.colorBlack));
                } else if(tvStatusPenjualan.getText() == MyConstants.GOING_NAME) {
                    statusCode = "002";
                    tvStatusPenjualan.setTextColor(getResources().getColor(R.color.colorProcess));
                }

                Map<String, String> params = new HashMap<>();
                params.put("ref_no", tvIdPenjualan.getText().toString());
                params.put("status_code", statusCode);

                CommonUtils.showLoading(PenjualanDetailActivity.this);
                VolleyAPI volleyAPI = new VolleyAPI(PenjualanDetailActivity.this);
                volleyAPI.putRequest("updateStatusPesanan", params, new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(String result) {
                        try {
                            JSONObject resultJSON = new JSONObject(result);
                            Intent myIntent = new Intent(getApplicationContext(), PenjualanActivity.class);
                            startActivityForResult(myIntent, 0);
                            Toast.makeText(getApplicationContext(), resultJSON.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                CommonUtils.hideLoading();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }
}
