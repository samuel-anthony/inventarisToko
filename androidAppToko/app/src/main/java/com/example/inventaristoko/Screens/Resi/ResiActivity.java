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

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

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

        final Calendar calDefault = Calendar.getInstance();
        day = calDefault.get(Calendar.DAY_OF_MONTH);
        month = calDefault.get(Calendar.MONTH)+1;
        year = calDefault.get(Calendar.YEAR);

        if(etTanggalDariResi.getText().toString().matches("") && etTanggalSampaiResi.getText().toString().matches("")) {
            etTanggalDariResi.setText(String.format(Locale.getDefault(), "%d-%s-%s", year, month, day));
            etTanggalSampaiResi.setText(String.format(Locale.getDefault(), "%d-%s-%s", year, month, day));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resi);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_resi);

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

                Intent intent = new Intent (v.getContext(), ResiDaftarActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("tanggalDari", txtTanggalDariResi);
                bundle.putString("tanggalSampai", txtTanggalSampaiResi);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
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
}