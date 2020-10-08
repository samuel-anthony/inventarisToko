package com.example.inventaristoko.Screens.Makanan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.inventaristoko.Adapter.BahanPokok.BahanPokokAdapter;
import com.example.inventaristoko.Model.BahanPokok.BahanPokok;
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


public class MakananDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView rvMakananDetail;
    private BahanPokokAdapter makananBahanPokokDetailAdapter;
    private ArrayList<BahanPokok> mBahanPokok = new ArrayList<>();
    private Button btnHapusMakanan, btnUbahMakanan;
    private TextView tvNamaMakanan, tvHargaMakanan;
    private ImageView ivGambarMakanan;
    private String idMakanan, hargaMakanan, decodeImage;

    private void init() {
        rvMakananDetail = findViewById(R.id.rvMakananDetail);
        tvNamaMakanan = findViewById(R.id.tvValueNamaMakanan);
        tvHargaMakanan = findViewById(R.id.tvValueHargaMakanan);
        ivGambarMakanan = findViewById(R.id.ivGambarMakanan);
        btnHapusMakanan = findViewById(R.id.btnHapusMakanan);
        btnUbahMakanan = findViewById(R.id.btnUbahMakanan);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makanan_detail);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_detail_makanan);

        init();

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        idMakanan = bundle.getString("idMakanan");
        tvNamaMakanan.setText(bundle.getString("namaMakanan"));
        hargaMakanan = bundle.getString("hargaMakanan");
        tvHargaMakanan.setText( CommonUtils.currencyFormat(hargaMakanan));

        btnHapusMakanan.setOnClickListener(this);
        btnUbahMakanan.setOnClickListener(this);

        getGambarMakananDetail();
        setUp();
    }

    private void getGambarMakananDetail() {
        CommonUtils.showLoading(MakananDetailActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(MakananDetailActivity.this);

        Map<String, String> params = new HashMap<>();
        params.put("makanan_id", idMakanan);

        volleyAPI.getRequest(MyConstants.MAKANAN_GET_IMAGE_DETAIL_ACTION, params, result -> {
            decodeImage =  result;

            if(decodeImage != null) {
                byte[] decodedString = Base64.decode(decodeImage, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                ivGambarMakanan.setImageBitmap(decodedByte);
            }
        });

        CommonUtils.hideLoading();
    }

    private void setUp() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvMakananDetail.setLayoutManager(mLayoutManager);
        rvMakananDetail.setItemAnimator(new DefaultItemAnimator());
        makananBahanPokokDetailAdapter = new BahanPokokAdapter(new ArrayList<>());

        callDataMakananDetailRequest();
    }

    private void callDataMakananDetailRequest() {
        CommonUtils.showLoading(MakananDetailActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(this);

        Map<String, String> params = new HashMap<>();
        params.put("makanan_id", idMakanan);

        volleyAPI.getRequest(MyConstants.MAKANAN_GET_DETAIL_ACTION, params, result -> {
            try {
                JSONObject resultJSON = new JSONObject(result);
                JSONObject resultArray = resultJSON.getJSONObject("result");
                JSONArray detailBahanPokok = resultArray.getJSONArray("bahanPokoks");

                for(int i = 0 ; i < detailBahanPokok.length() ; i ++ ) {
                    JSONObject dataBahanPokok = (JSONObject) detailBahanPokok.get(i);
                    BahanPokok bahanPokok = new BahanPokok();
                    bahanPokok.setId(String.valueOf(i+1));
                    bahanPokok.setIdBahanPokok(dataBahanPokok.getString("bahan_pokok_id"));
                    bahanPokok.setNamaBahanPokok(dataBahanPokok.getString("nama"));
                    bahanPokok.setJumlahBahanPokok(dataBahanPokok.getString("jumlah"));
                    bahanPokok.setSatuanBahanPokok(dataBahanPokok.getString("satuan"));

                    mBahanPokok.add(bahanPokok);
                }

                makananBahanPokokDetailAdapter.addItems(mBahanPokok);
                rvMakananDetail.setAdapter(makananBahanPokokDetailAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            CommonUtils.hideLoading();
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnHapusMakanan :
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage(R.string.confirmation_dialog_delete);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.label_yes, (dialog, which) -> callDeleteDataMakananRequest(v.getContext()));
                builder.setNegativeButton(R.string.label_no, (dialog, which) -> {
                });
                builder.show();
                break;
            case R.id.btnUbahMakanan :
                Intent intent = new Intent (v.getContext(), MakananEntryActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("screenState", MyConstants.UBAH_MAKANAN);
                bundle1.putString("idMakanan", idMakanan);
                bundle1.putString("namaMakanan", tvNamaMakanan.getText().toString());
                bundle1.putString("hargaMakanan", hargaMakanan);
//                bundle1.putString("gambarMakanan", decodeImage);

                if(mBahanPokok != null) {
                    bundle1.putSerializable("daftarBahanPokokSelected", mBahanPokok);
                }

                intent.putExtras(bundle1);
                v.getContext().startActivity(intent);
                break;
        }
    }

    private void callDeleteDataMakananRequest(Context context) {
        CommonUtils.showLoading(context);
        VolleyAPI volleyAPI = new VolleyAPI(context);

        Map<String, String> params = new HashMap<>();
        params.put("makanan_id", idMakanan);

        volleyAPI.putRequest(MyConstants.MAKANAN_DELETE_ACTION, params, result -> {
            try {
                JSONObject resultJSON = new JSONObject(result);
                Intent myIntent = new Intent(getApplicationContext(), MakananActivity.class);
                startActivityForResult(myIntent, 0);

                CommonUtils.showToast(context, resultJSON.getString("message"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        CommonUtils.hideLoading();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
