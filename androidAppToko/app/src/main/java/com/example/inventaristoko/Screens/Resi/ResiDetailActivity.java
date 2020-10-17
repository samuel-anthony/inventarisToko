package com.example.inventaristoko.Screens.Resi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.inventaristoko.Adapter.Penjualan.PenjualanDetailAdapter;
import com.example.inventaristoko.Model.Penjualan.PenjualanDetail;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Screens.Penjualan.PenjualanDetailActivity;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.PDFDownload;
import com.example.inventaristoko.Utils.VolleyAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ResiDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView rvDataResiDetail;
    private PenjualanDetailAdapter resiDetailAdapter;
    private TextView tvIdDetailResi, tvTanggalDetailResi, tvStatusDetailResi, tvTotalHargaDetailResi;
    private Button btnDownloadResi;
    private String txtIdDetailResi;
    JSONArray elementDownload = new JSONArray();
    private void init() {
        rvDataResiDetail = findViewById(R.id.rvDataResiDetail);
        tvIdDetailResi = findViewById(R.id.tvIdDetailResi);
        tvTanggalDetailResi = findViewById(R.id.tvTanggalDetailResi);
        tvStatusDetailResi = findViewById(R.id.tvStatusDetailResi);
        tvTotalHargaDetailResi = findViewById(R.id.tvTotalHargaDetailResi);
        btnDownloadResi = findViewById(R.id.btnDownloadResi);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resi_detail);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_detail_resi);

        init();

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        tvIdDetailResi.setText(bundle.getString("idPenjualan"));
        tvTanggalDetailResi.setText(bundle.getString("tanggalTambah"));
        tvTotalHargaDetailResi.setText(bundle.getString("totalHarga"));

        if(String.valueOf(bundle.getString("kodeStatus")).equals(MyConstants.PAID_CODE)) {
            tvStatusDetailResi.setTextColor(getResources().getColor(R.color.colorSuccess));
            tvStatusDetailResi.setText(MyConstants.PAID_NAME);
        }

        txtIdDetailResi = tvIdDetailResi.getText().toString();

        btnDownloadResi.setOnClickListener(this);

        setUp();
    }

    private void setUp() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvDataResiDetail.setLayoutManager(mLayoutManager);
        rvDataResiDetail.setItemAnimator(new DefaultItemAnimator());
        resiDetailAdapter = new PenjualanDetailAdapter(new ArrayList<>());

        callDataResiDetailRequest();
    }

    private void callDataResiDetailRequest() {
        CommonUtils.showLoading(ResiDetailActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(ResiDetailActivity.this);

        Map<String, String> params = new HashMap<>();
        params.put("ref_no", txtIdDetailResi);

        volleyAPI.getRequest(MyConstants.PENJUALAN_GET_DETAIL_ACTION, params, result -> {
            try {
                ArrayList<PenjualanDetail> mResiDetail = new ArrayList<>();
                JSONObject resultJSON = new JSONObject(result);
                JSONArray resultArray = resultJSON.getJSONArray("result");

                for(int i = 0 ; i < resultArray.length() ; i ++ ) {
                    JSONObject dataResiDetail = (JSONObject) resultArray.get(i);
                    PenjualanDetail resiDetail = new PenjualanDetail();
                    resiDetail.setIdDetail(String.valueOf(i+1));
                    resiDetail.setIdDetailPenjualan(dataResiDetail.getString("pesanan_detail_id"));
                    resiDetail.setNamaDetailPenjualan(dataResiDetail.getString("nama"));
                    resiDetail.setJumlahDetailPenjualan(dataResiDetail.getString("jumlah"));
                    resiDetail.setHargaDetailMakanan(dataResiDetail.getString("harga_makanan"));
                    resiDetail.setCatatanDetailPenjualan(dataResiDetail.getString("notes"));
                    resiDetail.setTanggalTambahDetailPenjualan(dataResiDetail.getString("created_at"));
                    resiDetail.setTanggalUbahDetailPenjualan(dataResiDetail.getString("updated_at"));
                    mResiDetail.add(resiDetail);

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("makanan",dataResiDetail.getString("nama")+" (@"+CommonUtils.currencyFormatWithoutRupiah(dataResiDetail.getString("harga_makanan"))+")");
                    jsonObject.put("jumlah",dataResiDetail.getString("jumlah"));
                    jsonObject.put("harga",dataResiDetail.getInt("harga_makanan")*dataResiDetail.getInt("jumlah"));
                    elementDownload.put(jsonObject);
                }

                resiDetailAdapter.addItems(mResiDetail);
                rvDataResiDetail.setAdapter(resiDetailAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        CommonUtils.hideLoading();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnDownloadResi) {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage(R.string.confirmation_dialog_download);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.label_yes, (dialog, which) -> callDownloadDataResiRequest(v.getContext()));
            builder.setNegativeButton(R.string.label_no, (dialog, which) -> {
            });
            builder.show();
        }
    }

    private void callDownloadDataResiRequest(Context context) {
        PDFDownload pdf = new PDFDownload(txtIdDetailResi);
        pdf.downloadResi(elementDownload,context);
    }
}
