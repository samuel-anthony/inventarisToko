package com.example.inventaristoko.Screens;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.MyConstants;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PenjualanDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ListView listView;
    ArrayAdapter<CharSequence> adapter;
    TextView tvRefNo, tvTanggalPesanan, tvStatus, tvTotalHarga, tvTotalDummy;
    String[] statues = { "Dipesan", "Sedang Dibuat", "Sudah Selesai"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setTitle(R.string.label_detil_pesanan);

        listView = findViewById(R.id.listView);

        tvRefNo = findViewById(R.id.noPesanan);
        tvTanggalPesanan = findViewById(R.id.tanggalPesanan);
        tvStatus = findViewById(R.id.status);
        tvTotalHarga = findViewById(R.id.totalHarga);
        tvTotalDummy = findViewById(R.id.total);

        Bundle bundle = getIntent().getExtras();
        tvRefNo.setText(bundle.getString("refNo"));
        tvTanggalPesanan.setText(bundle.getString("createdAt"));
        tvTotalHarga.setText(bundle.getString("totalHarga"));

        String statusCode = bundle.getString("statusCode");
        if(statusCode.equals(MyConstants.ORDER_CODE)) {
            tvStatus.setTextColor(getResources().getColor(R.color.colorBlack));
            tvStatus.setText(MyConstants.ORDER_NAME);
        } else if (statusCode.equals(MyConstants.GOING_CODE)) {
            tvStatus.setTextColor(getResources().getColor(R.color.colorBlack));
            tvStatus.setText(MyConstants.GOING_NAME);
        } else if (statusCode.equals(MyConstants.FINISH_CODE)) {
            tvStatus.setTextColor(getResources().getColor(R.color.colorBlack));
            tvStatus.setText(MyConstants.FINISH_NAME);
        }

        adapter = ArrayAdapter.createFromResource(this, R.array.detil_makanan,android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

//        try {
//            ArrayList<String> detailList = new ArrayList<>();
//            int test = 0;
//            JSONObject resultJSON = new JSONObject();
//            JSONArray resultArray = resultJSON.getJSONArray(bundle.getString("details"));
//            for(int i = 0 ; i < resultArray.length() ; i ++ ) {
//                JSONObject dataDetail = (JSONObject) resultArray.get(i);
//                test = resultArray.length();
//                String hasilnya = dataDetail.getString("pesanan_detail_id");
//                detailList.add(hasilnya);
//            }
//            adapter = ArrayAdapter.createFromResource(this, test,android.R.layout.simple_list_item_1);
//            listView.setAdapter(adapter);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        ExtendedFloatingActionButton floatingActionButton = findViewById(R.id.doneButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PenjualanDetailActivity.this, "Tombol Selesai Telah Ditekan", Toast.LENGTH_SHORT).show();
            }
        });

        Spinner spin = findViewById(R.id.statusDroplist);
        spin.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.status));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.list, menu);
//        return true;
//    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(this, adapter.getItem(position), Toast.LENGTH_SHORT).show();
//    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        Toast.makeText(getApplicationContext(), "Selected User: "+ statues[position] ,Toast.LENGTH_SHORT).show();
        tvTotalDummy.setText(statues[position]);

        if(tvTotalDummy.getText() == MyConstants.ORDER_NAME) {
            tvTotalDummy.setTextColor(getResources().getColor(R.color.colorBlack));
        } else if(tvTotalDummy.getText() == MyConstants.GOING_NAME) {
            tvTotalDummy.setTextColor(getResources().getColor(R.color.colorProcess));
        } else if(tvTotalDummy.getText() == MyConstants.FINISH_NAME) {
            tvTotalDummy.setTextColor(getResources().getColor(R.color.colorSuccess));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO - Custom Code
    }
}
