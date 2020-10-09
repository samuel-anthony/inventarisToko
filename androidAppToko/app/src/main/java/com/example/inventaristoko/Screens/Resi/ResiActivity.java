package com.example.inventaristoko.Screens.Resi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.VolleyAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ResiActivity extends AppCompatActivity implements View.OnClickListener {
    private Context appContext;
    private Button btnCariResi;
    private EditText etTanggalDariResi, etTanggalSampaiResi;
    private String txtTanggalDariResi, txtTanggalSampaiResi;
    private int year, month, day;

    private void init() {
        appContext = getApplicationContext();
        etTanggalDariResi = findViewById(R.id.etTanggalDariResi);
        etTanggalSampaiResi = findViewById(R.id.etTanggalSampaiResi);
        btnCariResi = findViewById(R.id.btnCariResi);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resi);

        init();

        etTanggalDariResi.setOnClickListener(this);
        etTanggalSampaiResi.setOnClickListener(this);
        btnCariResi.setOnClickListener(this);

        etTanggalDariResi.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                CommonUtils.hideKeyboard(appContext, v);
            }
        });

        etTanggalSampaiResi.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                CommonUtils.hideKeyboard(appContext, v);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCariResi :
                txtTanggalDariResi = etTanggalDariResi.getText().toString();
                txtTanggalSampaiResi = etTanggalSampaiResi.getText().toString();

                if (txtTanggalDariResi.isEmpty() || txtTanggalSampaiResi.isEmpty()) {
                    CommonUtils.showToast(appContext, appContext.getString(R.string.label_input_kosong));
                    return;
                }

                callCariDataResiRequest(v.getContext());
                break;
            case R.id.etTanggalDariResi :
                final Calendar calDari = Calendar.getInstance();
                day = calDari.get(Calendar.DAY_OF_MONTH);
                month = calDari.get(Calendar.MONTH);
                year = calDari.get(Calendar.YEAR);

                DatePickerDialog datePicker1 = new DatePickerDialog(v.getContext(), (view, year1, monthOfYear, dayOfMonth) -> {
                    String monthLabel, dayLabel;

                    monthLabel = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
                    dayLabel = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);

                    etTanggalDariResi.setText(String.format(Locale.getDefault(), "%d-%s-%s", year1, monthLabel, dayLabel));
                }, year, month, day);
                datePicker1.show();
                break;
            case R.id.etTanggalSampaiResi :
                final Calendar calSampai = Calendar.getInstance();
                day = calSampai.get(Calendar.DAY_OF_MONTH);
                month = calSampai.get(Calendar.MONTH);
                year = calSampai.get(Calendar.YEAR);

                DatePickerDialog datePicker2 = new DatePickerDialog(v.getContext(), (view, year1, monthOfYear, dayOfMonth) -> {
                    String monthLabel, dayLabel;

                    monthLabel = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
                    dayLabel = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);

                    etTanggalSampaiResi.setText(String.format(Locale.getDefault(), "%d-%s-%s", year1, monthLabel, dayLabel));
                }, year, month, day);
                datePicker2.show();
                break;
        }
    }

    private void callCariDataResiRequest(Context context) {
        CommonUtils.showLoading(context);
        VolleyAPI volleyAPI = new VolleyAPI(context);

        Map<String, String> params = new HashMap<>();
        params.put("tanggal_dari", txtTanggalDariResi);
        params.put("tanggal_sampai", txtTanggalSampaiResi);

//        volleyAPI.getRequest(MyConstants.RESI_GET_ACTION, params, result -> {
//            try {
//                ArrayList<Resi> mResiResponse = new ArrayList<>();
//                JSONObject resultJSON = new JSONObject(result);
//                JSONArray resultArray = resultJSON.getJSONArray("result");
//
//                for (int i = 0; i < resultArray.length(); i++) {
//                    JSONObject dataResi = (JSONObject) resultArray.get(i);
//                    Resi resi = new Resi();
//                    resi.setIdResi(dataResi.getString("makanan_id"));
//                    resi.setNamaResi(dataResi.getString("nama"));
//                    mResiResponse.add(resi);
//                }
//
//                Intent intent = new Intent(context, ResiDaftarActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("daftarResi", mResiResponse);
//                intent.putExtras(bundle);
//                v.getContext().startActivity(intent);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        });

        CommonUtils.showToast(context, "Tekan Cari Resi");

        CommonUtils.hideLoading();
    }
}