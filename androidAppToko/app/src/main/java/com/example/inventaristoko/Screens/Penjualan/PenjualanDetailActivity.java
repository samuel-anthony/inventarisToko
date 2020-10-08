package com.example.inventaristoko.Screens.Penjualan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.example.inventaristoko.Utils.VolleyAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class PenjualanDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private Context appContext;
    private RecyclerView rvDataPenjualanDetail;
    private PenjualanDetailAdapter penjualanDetailAdapter;
    private TextView tvIdPenjualan, tvTanggalPenjualan, tvStatusPenjualan, tvTotalHargaPenjualan;
    private String[] statues = { "Dipesan", "Sedang Dibuat" };
    private Button btnSelesaiPesanan;
    private Spinner spnDaftarStatus;
    private int positionStatus;
    private String txtIdDetailPenjualan;

    private void init() {
        appContext = getApplicationContext();
        rvDataPenjualanDetail = findViewById(R.id.rvDataPenjualanDetail);
        tvIdPenjualan = findViewById(R.id.tvIdDetailPenjualan);
        tvTanggalPenjualan = findViewById(R.id.tvTanggalDetailPenjualan);
        tvStatusPenjualan = findViewById(R.id.tvStatusDetailPenjualan);
        tvTotalHargaPenjualan = findViewById(R.id.tvTotalHargaDetailPenjualan);
        spnDaftarStatus = findViewById(R.id.spnDroplistStatus);
        btnSelesaiPesanan = findViewById(R.id.btnSelesaiPesanan);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjualan_detail);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_detail_pesanan);

        init();

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        tvIdPenjualan.setText(bundle.getString("idPenjualan"));
        tvTanggalPenjualan.setText(bundle.getString("tanggalTambah"));
        tvTotalHargaPenjualan.setText(bundle.getString("totalHarga"));

        if(String.valueOf(bundle.getString("kodeStatus")).equals(MyConstants.ORDER_CODE)) {
            tvStatusPenjualan.setTextColor(getResources().getColor(R.color.colorBlack));
            tvStatusPenjualan.setText(MyConstants.ORDER_NAME);
        } else {
            tvStatusPenjualan.setTextColor(getResources().getColor(R.color.colorProcess));
            tvStatusPenjualan.setText(MyConstants.GOING_NAME);
        }

        txtIdDetailPenjualan = tvIdPenjualan.getText().toString();

        btnSelesaiPesanan.setOnClickListener(this);
        spnDaftarStatus.setOnItemSelectedListener(this);

        setUp();
    }

    private void setUp() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvDataPenjualanDetail.setLayoutManager(mLayoutManager);
        rvDataPenjualanDetail.setItemAnimator(new DefaultItemAnimator());
        penjualanDetailAdapter = new PenjualanDetailAdapter(new ArrayList<>());

        callDataPenjualanDetailRequest();

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statues);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDaftarStatus.setAdapter(statusAdapter);
        positionStatus = statusAdapter.getPosition((String) tvStatusPenjualan.getText());
        spnDaftarStatus.setSelection(positionStatus);
    }

    private void callDataPenjualanDetailRequest() {
        CommonUtils.showLoading(PenjualanDetailActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(PenjualanDetailActivity.this);

        Map<String, String> params = new HashMap<>();
        params.put("ref_no", txtIdDetailPenjualan);

        volleyAPI.getRequest(MyConstants.PENJUALAN_GET_DETAIL_ACTION, params, result -> {
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

                penjualanDetailAdapter.addItems(mPenjualanDetail);
                rvDataPenjualanDetail.setAdapter(penjualanDetailAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        CommonUtils.hideLoading();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSelesaiPesanan) {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage(R.string.confirmation_dialog_finish_order);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.label_yes, (dialog, which) -> callSubmitDataPenjualanRequest(v.getContext()));
            builder.setNegativeButton(R.string.label_no, (dialog, which) -> {
            });
            builder.show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View v, int position, long id) {
        if(positionStatus == position) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage(appContext.getString(R.string.confirmation_dialog_edit_status) + " " + spnDaftarStatus.getSelectedItem().toString() + "?");
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.label_yes, (dialog, which) -> callEditStatusDataPenjualanRequest(v.getContext(), position));
        builder.setNegativeButton(R.string.label_no, (dialog, which) -> spnDaftarStatus.setSelection(positionStatus));
        builder.show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    private void callEditStatusDataPenjualanRequest(Context context, int position) {
        CommonUtils.showLoading(context);
        VolleyAPI volleyAPI = new VolleyAPI(context);

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
        params.put("ref_no", txtIdDetailPenjualan);
        params.put("status_code", statusCode);

        volleyAPI.putRequest(MyConstants.PENJUALAN_EDIT_STATUS_ACTION, params, result -> {
            try {
                JSONObject resultJSON = new JSONObject(result);
                Intent myIntent = new Intent(context, PenjualanActivity.class);
                startActivityForResult(myIntent, 0);
                spnDaftarStatus.setSelection(position);

                CommonUtils.showToast(context, resultJSON.getString("message"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        CommonUtils.hideLoading();
    }

    private void callSubmitDataPenjualanRequest(Context context) {
        CommonUtils.showLoading(context);
        VolleyAPI volleyAPI = new VolleyAPI(context);

        Map<String, String> params = new HashMap<>();
        params.put("ref_no", txtIdDetailPenjualan);

        volleyAPI.putRequest(MyConstants.PENJUALAN_SUBMIT_PENJUALAN_ACTION, params, result -> {
            try {
                JSONObject resultJSON = new JSONObject(result);
                Intent myIntent = new Intent(context, PenjualanActivity.class);
                startActivityForResult(myIntent, 0);

                CommonUtils.showToast(context, resultJSON.getString("message"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        CommonUtils.hideLoading();
    }
}
