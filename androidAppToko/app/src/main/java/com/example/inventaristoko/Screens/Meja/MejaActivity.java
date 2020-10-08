package com.example.inventaristoko.Screens.Meja;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.inventaristoko.Adapter.Meja.MejaAdapter;
import com.example.inventaristoko.Model.Meja.Meja;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.PDFDownload;
import com.example.inventaristoko.Utils.VolleyAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class MejaActivity extends AppCompatActivity implements View.OnClickListener {
    private FloatingActionButton fabDataMeja;
    private RecyclerView rvDataMeja;
    private MejaAdapter mejaAdapter;
    private Button btnTambahMeja;
    private JSONArray elementDownload = new JSONArray();

    private void init() {
        rvDataMeja = findViewById(R.id.rvDataMeja);
        fabDataMeja = findViewById(R.id.fabDataMeja);
        btnTambahMeja = findViewById(R.id.btnTambahMeja);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meja);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_meja);

        init();

        fabDataMeja.setOnClickListener(this);
        btnTambahMeja.setOnClickListener(this);

        setUp();
    }

    private void setUp() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvDataMeja.setLayoutManager(mLayoutManager);
        rvDataMeja.setItemAnimator(new DefaultItemAnimator());
        mejaAdapter = new MejaAdapter(new ArrayList<>());

        callDataMejaRequest();
    }

    private void callDataMejaRequest() {
        CommonUtils.showLoading(MejaActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(MejaActivity.this);

        Map<String, String> params = new HashMap<>();

        volleyAPI.getRequest(MyConstants.MEJA_GET_ACTION, params, result -> {
            try {
                ArrayList<Meja> mMeja = new ArrayList<>();
                JSONObject resultJSON = new JSONObject(result);
                JSONArray resultArray = resultJSON.getJSONArray("result");

                for(int i = 0 ; i < resultArray.length() ; i ++ ) {
                    JSONObject dataMeja = (JSONObject) resultArray.get(i);
                    Meja meja = new Meja();
                    meja.setId(String.valueOf(i+1));
                    meja.setIdMeja(dataMeja.getString("user_id"));
                    meja.setNamaMeja(dataMeja.getString("full_name"));
                    meja.setTanggalTambahMeja(dataMeja.getString("created_at"));
                    meja.setTanggalUbahMeja(dataMeja.getString("updated_at"));

                    JSONObject elementToDownload = new JSONObject();
                    elementToDownload.put("number",i+1);
                    elementToDownload.put("full_name",dataMeja.getString("full_name"));
                    elementToDownload.put("user_id",dataMeja.getString("user_id"));
                    elementDownload.put(elementToDownload);
                    mMeja.add(meja);
                }

                mejaAdapter.addItems(mMeja);
                rvDataMeja.setAdapter(mejaAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        CommonUtils.hideLoading();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabDataMeja :
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage(R.string.confirmation_dialog_download);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.label_yes, (dialog, which) -> {
                    PDFDownload pdf = new PDFDownload("Data Meja");

                    List<String> columnName = new ArrayList<>();
                    columnName.add("number");
                    columnName.add("id meja");
                    columnName.add("nama meja");

                    List<String> key = new ArrayList<>();
                    key.add("number");
                    key.add("user_id");
                    key.add("full_name");

                    try {
                        pdf.download(columnName, key, elementDownload, this);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
                builder.setNegativeButton(R.string.label_no, (dialog, which) -> {
                });
                builder.show();
                break;
            case R.id.btnTambahMeja :
                Intent intent = new Intent (v.getContext(), MejaEntryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("screenState", MyConstants.TAMBAH_MEJA);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
                break;
        }
    }
}
