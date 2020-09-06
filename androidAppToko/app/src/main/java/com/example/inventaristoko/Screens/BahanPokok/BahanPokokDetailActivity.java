package com.example.inventaristoko.Screens.BahanPokok;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventaristoko.Adapter.BahanPokok.BahanPokokDetailAdapter;
import com.example.inventaristoko.Model.BahanPokok.BahanPokok;
import com.example.inventaristoko.Model.BahanPokok.BahanPokokDetail;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.VolleyAPI;
import com.example.inventaristoko.Utils.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BahanPokokDetailActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private BahanPokokDetailAdapter mBahanPokokDetailAdapter;
    private Button btnDelete, btnEdit, btnAddDetail;
    private TextView tvStapleName, tvStapleAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bahan_pokok_detail);

        getSupportActionBar().setTitle(R.string.label_detil_bahan_pokok);

        tvStapleName = findViewById(R.id.labelValueBahanPokokNama);
        tvStapleAmount = findViewById(R.id.labelValueBahanPokokSatuan);

        btnDelete = findViewById(R.id.buttonDelete);
        btnEdit = findViewById(R.id.buttonEdit);
        btnAddDetail = findViewById(R.id.buttonAddDetail);

        Bundle bundle = getIntent().getExtras();
        tvStapleName.setText(bundle.getString("stapleName"));
        tvStapleAmount.setText(bundle.getString("stapleAmount"));

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BahanPokokDetailActivity.this);
                builder.setMessage("Anda Yakin Ingin Menghapus Data ini?");
                builder.setCancelable(false);
                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        CommonUtils.showLoading(BahanPokokDetailActivity.this);
//                        VolleyAPI volleyAPI = new VolleyAPI(BahanPokokDetailActivity.this);
//                        Map<String, String> params = new HashMap<>();
//                        params.put("user_name", tvUserName.getText().toString());
//
//                        volleyAPI.putRequest("deleteBahanPokok", params, new VolleyCallback() {
//                            @Override
//                            public void onSuccessResponse(String result) {
//                                try {
//                                    JSONObject resultJSON = new JSONObject(result);
//                                    Intent myIntent = new Intent(getApplicationContext(), PenggunaActivity.class);
//                                    startActivityForResult(myIntent, 0);
//                                    Toast.makeText(getApplicationContext(), resultJSON.getString("message"), Toast.LENGTH_SHORT).show();
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
                        Toast.makeText(getApplicationContext(), "Ups! Something Wrong", Toast.LENGTH_SHORT).show();
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

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), BahanPokokEntryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("screenState", MyConstants.EDIT_BAHAN_POKOK);
                bundle.putString("stapleName", tvStapleName.getText().toString());
                bundle.putString("stapleAmount", tvStapleAmount.getText().toString());
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });

        btnAddDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent (v.getContext(), BahanPokokDetailEntryActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("screenState", MyConstants.EDIT_BAHAN_POKOK);
//                bundle.putString("stapleName", tvStapleName.getText().toString());
//                bundle.putString("stapleAmount", tvStapleAmount.getText().toString());
//                intent.putExtras(bundle);
//                v.getContext().startActivity(intent);

                Toast.makeText(getApplicationContext(), "Add Detail Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        mRecyclerView = findViewById(R.id.rvBahanPokokDetail);
        ButterKnife.bind(this);
        setUp();
    }

    private void setUp() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mBahanPokokDetailAdapter = new BahanPokokDetailAdapter(new ArrayList<>());

        prepareDataBahanPokokDetail();
    }

    private void prepareDataBahanPokokDetail() {
        CommonUtils.showLoading(BahanPokokDetailActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(this);
        Map<String, String> params = new HashMap<>();
        volleyAPI.getRequest("getSemuaDetailBahanPokok", params, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                try {
                    ArrayList<BahanPokokDetail> mBahanPokokDetail = new ArrayList<>();
                    JSONObject resultJSON = new JSONObject(result);
                    JSONArray resultArray = resultJSON.getJSONArray("result");
                    for(int i = 0 ; i < resultArray.length() ; i ++ ) {
                        JSONObject dataBahanPokokDetail = (JSONObject) resultArray.get(i);
                        BahanPokokDetail bahanPokokDetail = new BahanPokokDetail();
                        bahanPokokDetail.setDetailId(String.valueOf(i+1));
                        bahanPokokDetail.setStapleDetailId(dataBahanPokokDetail.getString("bahan_pokok_detail_id"));
                        bahanPokokDetail.setStapleDetailName(dataBahanPokokDetail.getString("nama"));
                        bahanPokokDetail.setStapleDetailStoreName(dataBahanPokokDetail.getString("nama_toko"));
                        bahanPokokDetail.setStapleDetailAmount(dataBahanPokokDetail.getString("jumlah"));
                        bahanPokokDetail.setStapleDetailPrice(dataBahanPokokDetail.getString("harga"));
                        bahanPokokDetail.setStapleDetailCreatedAt(dataBahanPokokDetail.getString("created_at"));
                        bahanPokokDetail.setStapleDetailUpdatedAt(dataBahanPokokDetail.getString("updated_at"));

                        mBahanPokokDetail.add(bahanPokokDetail);
                    }
                    mBahanPokokDetailAdapter.addItems(mBahanPokokDetail);
                    mRecyclerView.setAdapter(mBahanPokokDetailAdapter);
                    CommonUtils.hideLoading();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}