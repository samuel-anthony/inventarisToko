package com.example.inventaristoko.Screens.Meja;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MejaEntryActivity extends AppCompatActivity implements View.OnClickListener {
    private Context appContext;
    private Bundle bundle;
    private EditText etIdMeja, etNamaMeja;
    private Button btnKirimMeja;
    private String screenState, oldNamaMeja, txtIdMeja, txtNamaMeja;

    private void init() {
        appContext = getApplicationContext();
        bundle = getIntent().getExtras();
        etIdMeja = findViewById(R.id.etIdMeja);
        etNamaMeja = findViewById(R.id.etNamaMeja);
        btnKirimMeja = findViewById(R.id.btnKirimMeja);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meja_entry);

        init();

        assert bundle != null;
        screenState = bundle.getString("screenState");
        oldNamaMeja = bundle.getString("idMeja");

        if(screenState.equals(MyConstants.UBAH_MEJA)) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_ubah_meja);
            etIdMeja.setText(bundle.getString("idMeja"));
            etNamaMeja.setText(bundle.getString("namaMeja"));
        } else {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_tambah_meja);
        }

        btnKirimMeja.setOnClickListener(this);

        etIdMeja.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                CommonUtils.hideKeyboard(appContext, v);
            }
        });

        etNamaMeja.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                CommonUtils.hideKeyboard(appContext, v);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnKirimMeja) {
            txtIdMeja = etIdMeja.getText().toString();
            txtNamaMeja = etNamaMeja.getText().toString();

            if(txtIdMeja.isEmpty() || txtNamaMeja.isEmpty()) {
                CommonUtils.showToast(appContext, appContext.getString(R.string.label_input_password_kosong));
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage(R.string.confirmation_dialog_submit);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.label_yes, (dialog, which) -> callSubmitDataMejaRequest(v.getContext()));
            builder.setNegativeButton(R.string.label_no, (dialog, which) -> {
            });
            builder.show();
        }
    }

    private void callSubmitDataMejaRequest(Context context) {
        CommonUtils.showLoading(context);
        VolleyAPI volleyAPI = new VolleyAPI(context);

        Map<String, String> params = new HashMap<>();
        params.put("nama_meja", txtIdMeja);
        params.put("full_name", txtNamaMeja);

        if (screenState.equals(MyConstants.UBAH_MEJA)) {
            params.put("nama_meja_old", oldNamaMeja);
        }

        if (screenState.equals(MyConstants.UBAH_MEJA)) {
            volleyAPI.putRequest(MyConstants.MEJA_EDIT_ACTION, params, result -> {
                try {
                    JSONObject resultJSON = new JSONObject(result);
                    Intent myIntent = new Intent(context, MejaActivity.class);
                    startActivityForResult(myIntent, 0);

                    CommonUtils.showToast(context, resultJSON.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        } else if (screenState.equals(MyConstants.TAMBAH_MEJA)) {
            volleyAPI.postRequest(MyConstants.MEJA_ADD_ACTION, params, result -> {
                try {
                    JSONObject resultJSON = new JSONObject(result);
                    Intent myIntent = new Intent(context, MejaActivity.class);
                    startActivityForResult(myIntent, 0);

                    CommonUtils.showToast(context, resultJSON.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }

        CommonUtils.hideLoading();
    }
}