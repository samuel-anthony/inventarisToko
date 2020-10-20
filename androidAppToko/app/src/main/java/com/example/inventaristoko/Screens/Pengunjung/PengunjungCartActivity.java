package com.example.inventaristoko.Screens.Pengunjung;

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

import com.example.inventaristoko.Adapter.Pengunjung.PengunjungKeranjangAdapter;
import com.example.inventaristoko.Model.Penjualan.PenjualanDetail;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.Preferences;
import com.example.inventaristoko.Utils.VolleyAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PengunjungCartActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView rvDataPengunjungMakananCart;
    private PengunjungKeranjangAdapter makananCartAdapter;
    private TextView tvTotalMakananPengunjungCart;
    private Button btnPesanMakanan, btnHapusMakananCart;
    private int amount, price, totalPrice;

    private void init() {
        rvDataPengunjungMakananCart = findViewById(R.id.rvDataPengunjungMakananCart);
        btnPesanMakanan = findViewById(R.id.btnPesanMakanan);
        btnHapusMakananCart = findViewById(R.id.btnHapusMakananCart);
        tvTotalMakananPengunjungCart = findViewById(R.id.tvTotalMakananPengunjungCart);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengunjung_cart);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_cart);

        init();

        btnPesanMakanan.setOnClickListener(this);
        btnHapusMakananCart.setOnClickListener(this);

        setUp();
    }

    private void setUp() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvDataPengunjungMakananCart.setLayoutManager(mLayoutManager);
        rvDataPengunjungMakananCart.setItemAnimator(new DefaultItemAnimator());
        makananCartAdapter = new PengunjungKeranjangAdapter(new ArrayList<>());

        callDataPengunjungKeranjangRequest();
    }

    private void callDataPengunjungKeranjangRequest() {
        CommonUtils.showLoading(PengunjungCartActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(PengunjungCartActivity.this);

        Map<String, String> params = new HashMap<>();
        params.put("user_id", Preferences.getLoggedInUserCustomer(getBaseContext()));

        volleyAPI.getRequest(MyConstants.PENGUNJUNG_GET_CART_ACTION, params, result -> {
            try {
                ArrayList<PenjualanDetail> mMakananCart = new ArrayList<>();
                JSONObject resultJSON = new JSONObject(result);
                JSONArray resultArray = resultJSON.getJSONArray("result");

                for(int i = 0 ; i < resultArray.length() ; i ++ ) {
                    JSONObject dataMakananCart = (JSONObject) resultArray.get(i);
                    PenjualanDetail makananCart = new PenjualanDetail();
                    makananCart.setIdDetail(String.valueOf(i+1));
                    makananCart.setIdDetailPenjualan(dataMakananCart.getString("cart_id"));
                    makananCart.setNamaDetailPenjualan(dataMakananCart.getString("nama_makanan"));
                    makananCart.setJumlahDetailPenjualan(dataMakananCart.getString("jumlah"));
                    makananCart.setHargaDetailMakanan(dataMakananCart.getString("harga_makanan"));

                    amount = Integer.parseInt(dataMakananCart.getString("jumlah"));
                    price = Integer.parseInt(dataMakananCart.getString("harga_makanan"));

                    totalPrice += amount * price;

                    makananCart.setCatatanDetailPenjualan(dataMakananCart.getString("notes"));
                    makananCart.setTanggalTambahDetailPenjualan(dataMakananCart.getString("created_at"));
                    makananCart.setTanggalUbahDetailPenjualan(dataMakananCart.getString("updated_at"));
                    mMakananCart.add(makananCart);
                }

                tvTotalMakananPengunjungCart.setText(CommonUtils.currencyFormat(String.valueOf(totalPrice)));

                makananCartAdapter.addItems(mMakananCart);
                rvDataPengunjungMakananCart.setAdapter(makananCartAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        CommonUtils.hideLoading();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPesanMakanan:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(v.getContext());
                builder1.setMessage(R.string.confirmation_dialog_submit);
                builder1.setCancelable(false);
                builder1.setPositiveButton(R.string.label_yes, (dialog, which) -> callSubmitDataPengunjungPesanan(v.getContext()));
                builder1.setNegativeButton(R.string.label_no, (dialog, which) -> {
                });
                builder1.show();
                break;
            case R.id.btnHapusMakananCart:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(v.getContext());
                builder2.setMessage(R.string.confirmation_dialog_delete);
                builder2.setCancelable(false);
                builder2.setPositiveButton(R.string.label_yes, (dialog, which) -> callDeleteDataPengunjungPesanan(v.getContext()));
                builder2.setNegativeButton(R.string.label_no, (dialog, which) -> {
                });
                builder2.show();
                break;
        }
    }

    private void callDeleteDataPengunjungPesanan(Context context) {
        CommonUtils.showLoading(context);
        VolleyAPI volleyAPI = new VolleyAPI(context);

        Map<String, String> params = new HashMap<>();
        params.put("user_id", Preferences.getLoggedInUserCustomer(getBaseContext()));

        volleyAPI.postRequest(MyConstants.PENGUNJUNG_DELETE_CART_ACTION, params, result -> {
            try {
                JSONObject resultJSON = new JSONObject(result);
                Intent myIntent = new Intent(context, PengunjungActivity.class);
                startActivityForResult(myIntent, 0);

                CommonUtils.showToast(context, resultJSON.getString("message"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        CommonUtils.hideLoading();
    }

    private void callSubmitDataPengunjungPesanan(Context context) {
        CommonUtils.showLoading(context);
        VolleyAPI volleyAPI = new VolleyAPI(context);

        Map<String, String> params = new HashMap<>();
        params.put("user_id", Preferences.getLoggedInUserCustomer(getBaseContext()));
        params.put("total_harga", String.valueOf(totalPrice));

        volleyAPI.postRequest(MyConstants.PENGUNJUNG_ADD_NEW_ORDER_ACTION, params, result -> {
            try {
                JSONObject resultJSON = new JSONObject(result);
                Intent myIntent = new Intent(context, PengunjungActivity.class);
                startActivityForResult(myIntent, 0);

                CommonUtils.showToast(context, resultJSON.getString("message"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        CommonUtils.hideLoading();
    }
}