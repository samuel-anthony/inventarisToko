package com.example.inventaristoko.Screens.Penjualan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PenjualanDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private RecyclerView mRecyclerView;
    private PenjualanDetailAdapter mPenjualanDetailAdapter;
    private TextView tvRefNo, tvTanggalPesanan, tvStatus, tvTotalHarga;
    private String[] statues = { "Dipesan", "Sedang Dibuat", "Sudah Selesai"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjualan_detail);

        getSupportActionBar().setTitle(R.string.label_detil_pesanan);

        mRecyclerView = findViewById(R.id.rvPenjualanDetail);

        tvRefNo = findViewById(R.id.noPesanan);
        tvTanggalPesanan = findViewById(R.id.tanggalPesanan);
        tvStatus = findViewById(R.id.status);
        tvTotalHarga = findViewById(R.id.totalHarga);

        Bundle bundle = getIntent().getExtras();
        tvRefNo.setText(bundle.getString("refNo"));
        tvTanggalPesanan.setText(bundle.getString("createdAt"));
        tvTotalHarga.setText(bundle.getString("totalHarga"));

        String statusCode = bundle.getString("statusCode");
        if(statusCode.equals(MyConstants.ORDER_CODE)) {
            tvStatus.setTextColor(getResources().getColor(R.color.colorBlack));
            tvStatus.setText(MyConstants.ORDER_NAME);
        } else if (statusCode.equals(MyConstants.GOING_CODE)) {
            tvStatus.setTextColor(getResources().getColor(R.color.colorProcess));
            tvStatus.setText(MyConstants.GOING_NAME);
        } else if (statusCode.equals(MyConstants.FINISH_CODE)) {
            tvStatus.setTextColor(getResources().getColor(R.color.colorSuccess));
            tvStatus.setText(MyConstants.FINISH_NAME);
        }

        ExtendedFloatingActionButton floatingActionButton = findViewById(R.id.doneButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PenjualanDetailActivity.this);
                builder.setMessage("Anda Yakin Ingin Menyelesaikan Pesanan?");
                builder.setCancelable(false);
                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Map<String, String> params = new HashMap<>();
                        params.put("ref_no", tvRefNo.getText().toString());

                        CommonUtils.showLoading(PenjualanDetailActivity.this);
                        VolleyAPI volleyAPI = new VolleyAPI(PenjualanDetailActivity.this);
//                        volleyAPI.getRequest("logout", params, new VolleyCallback() {
//                            @Override
//                            public void onSuccessResponse(String result) {
//                                try {
//                                    JSONObject resultJSON = new JSONObject(result);
//                                    Toast.makeText(getApplicationContext(), resultJSON.getString("message"), Toast.LENGTH_SHORT).show();
//                                    if (resultJSON.getString("is_error").equalsIgnoreCase("0")) {
//                                        Intent myIntent = new Intent(getApplicationContext(), PenjualanActivity.class);
//                                        startActivityForResult(myIntent, 0);
//                                        Toast.makeText(getApplicationContext(), "Berhasil Keluar", Toast.LENGTH_SHORT).show();
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
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

        Spinner spin = findViewById(R.id.statusDroplist);
        spin.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, statues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        int spinnerPosition = adapter.getPosition((String) tvStatus.getText());
        spin.setSelection(spinnerPosition);

        setUp();
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        Toast.makeText(getApplicationContext(), "Selected User: "+ statues[position] ,Toast.LENGTH_SHORT).show();
        tvStatus.setText(statues[position]);

        if(tvStatus.getText() == MyConstants.ORDER_NAME) {
            tvStatus.setTextColor(getResources().getColor(R.color.colorBlack));
        } else if(tvStatus.getText() == MyConstants.GOING_NAME) {
            tvStatus.setTextColor(getResources().getColor(R.color.colorProcess));
        } else if(tvStatus.getText() == MyConstants.FINISH_NAME) {
            tvStatus.setTextColor(getResources().getColor(R.color.colorSuccess));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
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
        params.put("ref_no", tvRefNo.getText().toString());

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
                        penjualanDetail.setOrderNo(String.valueOf(i+1));
                        penjualanDetail.setOrderMasterId(dataPenjualanDetail.getString("pesanan_master_id"));
                        penjualanDetail.setOrderDetailId(dataPenjualanDetail.getString("pesanan_detail_id"));
                        penjualanDetail.setOrderId(dataPenjualanDetail.getString("makanan_id"));
                        penjualanDetail.setOrderName(dataPenjualanDetail.getString("nama"));
                        penjualanDetail.setAmount(dataPenjualanDetail.getString("jumlah"));
                        penjualanDetail.setPrice(dataPenjualanDetail.getString("harga_jual"));
                        penjualanDetail.setNote(dataPenjualanDetail.getString("notes"));
                        penjualanDetail.setCreatedAt(dataPenjualanDetail.getString("created_at"));
                        penjualanDetail.setUpdatedAt(dataPenjualanDetail.getString("updated_at"));
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
}
