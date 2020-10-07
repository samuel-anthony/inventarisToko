package com.example.inventaristoko.Screens.Makanan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import butterknife.ButterKnife;

public class MakananDetailActivity extends AppCompatActivity {
    ArrayList<BahanPokok> mBahanPokok = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private BahanPokokAdapter mBahanPokokAdapter;
    private Button btnHapusMakanan, btnUbahMakanan;
    private TextView tvNamaMakanan, tvHargaMakanan;
    private ImageView ivGambarMakanan;
    private String idMakanan, hargaMakanan, decodeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makanan_detail);

        getSupportActionBar().setTitle(R.string.menu_detail_makanan);

        tvNamaMakanan = findViewById(R.id.tvValueNamaMakanan);
        tvHargaMakanan = findViewById(R.id.tvValueHargaMakanan);
        ivGambarMakanan = findViewById(R.id.ivGambarMakanan);
        btnHapusMakanan = findViewById(R.id.btnHapusMakanan);
        btnUbahMakanan = findViewById(R.id.btnUbahMakanan);

        Bundle bundle = getIntent().getExtras();
        idMakanan = bundle.getString("idMakanan");
        tvNamaMakanan.setText(bundle.getString("namaMakanan"));
        hargaMakanan = bundle.getString("hargaMakanan");
        tvHargaMakanan.setText( CommonUtils.currencyFormat(hargaMakanan));

        getGambarMakananDetail();

        btnHapusMakanan.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage(R.string.confirmation_dialog_delete);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.label_yes, (dialog, which) -> {
                CommonUtils.showLoading(v.getContext());
                VolleyAPI volleyAPI = new VolleyAPI(v.getContext());

                Map<String, String> params = new HashMap<>();
                params.put("makanan_id", idMakanan);

                volleyAPI.putRequest("deleteMakanan", params, result -> {
                    try {
                        JSONObject resultJSON = new JSONObject(result);
                        Intent myIntent = new Intent(getApplicationContext(), MakananActivity.class);
                        startActivityForResult(myIntent, 0);
                        Toast.makeText(getApplicationContext(), resultJSON.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
                CommonUtils.hideLoading();
            });
            builder.setNegativeButton(R.string.label_no, (dialog, which) -> {
            });
            builder.show();
        });

        btnUbahMakanan.setOnClickListener(v -> {
            Intent intent = new Intent (v.getContext(), MakananEntryActivity.class);
            Bundle bundle1 = new Bundle();
            bundle1.putString("screenState", MyConstants.UBAH_MAKANAN);
            bundle1.putString("idMakanan", idMakanan);
            bundle1.putString("namaMakanan", tvNamaMakanan.getText().toString());
            bundle1.putString("hargaMakanan", hargaMakanan);
            bundle1.putString("gambarMakanan", decodeImage);

            if(mBahanPokok != null) {
                bundle1.putSerializable("daftarBahanPokokSelected", mBahanPokok);
            }

            intent.putExtras(bundle1);
            v.getContext().startActivity(intent);
        });

        mRecyclerView = findViewById(R.id.rvDataMakananBahanPokokDetail);
        ButterKnife.bind(this);
        setUp();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        // or call onBackPressed()
        return true;
    }

    private void getGambarMakananDetail() {
        VolleyAPI volleyAPI = new VolleyAPI(MakananDetailActivity.this);

        Map<String, String> params = new HashMap<>();
        params.put("makanan_id", idMakanan);

        volleyAPI.getRequest("getGambarMakananDetail", params, result -> {
            decodeImage =  result;

            if(decodeImage != null) {
                byte[] decodedString = Base64.decode(decodeImage, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                ivGambarMakanan.setImageBitmap(decodedByte);
            }
        });
    }

    private void setUp() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mBahanPokokAdapter = new BahanPokokAdapter(new ArrayList<>());

        prepareDataDetailBahanPokokById();
    }

    private void prepareDataDetailBahanPokokById() {
        CommonUtils.showLoading(MakananDetailActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(this);

        Map<String, String> params = new HashMap<>();
        params.put("makanan_id", idMakanan);

        volleyAPI.getRequest("getMakananDetail", params, result -> {
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

                mBahanPokokAdapter.addItems(mBahanPokok);
                mRecyclerView.setAdapter(mBahanPokokAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            CommonUtils.hideLoading();
        });
    }
}
