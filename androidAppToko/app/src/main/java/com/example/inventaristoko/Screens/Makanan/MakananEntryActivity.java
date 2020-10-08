package com.example.inventaristoko.Screens.Makanan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.inventaristoko.Adapter.Makanan.MakananBahanPokokAdapter;
import com.example.inventaristoko.Model.BahanPokok.BahanPokok;
import com.example.inventaristoko.Model.Makanan.MakananBahanPokok;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.VolleyAPI;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class MakananEntryActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_IMAGE = 100;
    private Context appContext;
    private RecyclerView rvMakananBahanPokok;
    private ArrayList<MakananBahanPokok> mMakananBahanPokok = new ArrayList<>();
    private ArrayList<BahanPokok> mBahanPokokResponse = new ArrayList<>();
    private ArrayList<BahanPokok> mBahanPokok = new ArrayList<>();
    private ArrayList<BahanPokok> mBahanPokokSelected = new ArrayList<>();
    private ArrayList<BahanPokok> mSpnBahanPokok = new ArrayList<>();
    private EditText etNamaMakanan, etHargaMakanan, etJumlahBahanPokok;
    private TextView tvSatuanBahanPokok;
    private MakananBahanPokokAdapter makananBahanPokokAdapter;
    private ImageView ivGambarMakanan;
    private Button btnTambahGambar, btnKirimMakanan, btnTambahMakananBahanPokok;
    private Spinner spnDaftarBahanPokok;
    private String encodedImage, screenState, idBahanPokokSelected, idMakanan, bahanPokokSelected, txtNamaMakanan, txtHargaMakanan, satuanBahanPokokSelected, decodeImage;

    private void init() {
        appContext = getApplicationContext();
        rvMakananBahanPokok = findViewById(R.id.rvMakananBahanPokok);
        tvSatuanBahanPokok = findViewById(R.id.tvSatuanBahanPokok);
        etNamaMakanan = findViewById(R.id.etNamaMakanan);
        etHargaMakanan = findViewById(R.id.etHargaMakanan);
        etJumlahBahanPokok = findViewById(R.id.etJumlahBahanPokok);
        btnTambahGambar = findViewById(R.id.btnTambahGambar);
        btnTambahMakananBahanPokok = findViewById(R.id.btnTambahMakananBahanPokok);
        btnKirimMakanan = findViewById(R.id.btnKirimMakanan);
        ivGambarMakanan = findViewById(R.id.ivGambarMakanan);
        spnDaftarBahanPokok = findViewById(R.id.spnDaftarBahanPokok);

        getSemuaBahanPokok();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makanan_entry);

        init();

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        screenState = bundle.getString("screenState");
        idMakanan = bundle.getString("idMakanan");

        getGambarMakananDetail();

        if(screenState.equals(MyConstants.UBAH_MAKANAN)) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_ubah_makanan);
            etNamaMakanan.setText(bundle.getString("namaMakanan"));
            etHargaMakanan.setText(bundle.getString("hargaMakanan"));
        } else {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_tambah_makanan);
        }

        btnTambahMakananBahanPokok.setOnClickListener(this);
        btnTambahGambar.setOnClickListener(this);
        btnKirimMakanan.setOnClickListener(this);

        etNamaMakanan.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                CommonUtils.hideKeyboard(appContext, v);
            }
        });

        etHargaMakanan.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                CommonUtils.hideKeyboard(appContext, v);
            }
        });

        etJumlahBahanPokok.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                CommonUtils.hideKeyboard(appContext, v);
            }
        });
    }

    private void getSemuaBahanPokok() {
        CommonUtils.showLoading(MakananEntryActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(MakananEntryActivity.this);

        Map<String, String> params = new HashMap<>();

        volleyAPI.getRequest(MyConstants.BAHAN_POKOK_GET_ACTION, params, result -> {
            try {
                JSONObject resultJSON = new JSONObject(result);
                JSONArray resultArray = resultJSON.getJSONArray("result");

                for(int i = 0 ; i < resultArray.length() ; i ++ ) {
                    JSONObject dataBahanPokok = (JSONObject) resultArray.get(i);
                    BahanPokok bahanPokok = new BahanPokok();
                    bahanPokok.setIdBahanPokok(dataBahanPokok.getString("bahan_pokok_id"));
                    bahanPokok.setNamaBahanPokok(dataBahanPokok.getString("nama"));
                    bahanPokok.setSatuanBahanPokok(dataBahanPokok.getString("satuan"));
                    mBahanPokokResponse.add(bahanPokok);
                }

                mBahanPokok = mBahanPokokResponse;

                for (int i = 0; i < mBahanPokok.size(); i++) {
                    mSpnBahanPokok.add(new BahanPokok(mBahanPokok.get(i).getIdBahanPokok(), mBahanPokok.get(i).getNamaBahanPokok(), mBahanPokok.get(i).getSatuanBahanPokok()));
                }

                ArrayAdapter<BahanPokok> adapterBahanPokok = new ArrayAdapter<>(appContext, android.R.layout.simple_spinner_dropdown_item, mSpnBahanPokok);
                adapterBahanPokok.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                spnDaftarBahanPokok.setAdapter(adapterBahanPokok);
                spnDaftarBahanPokok.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        bahanPokokSelected = mSpnBahanPokok.get(position).getNamaBahanPokok();
                        idBahanPokokSelected = mSpnBahanPokok.get(position).getIdBahanPokok();
                        satuanBahanPokokSelected = mSpnBahanPokok.get(position).getSatuanBahanPokok();
                        tvSatuanBahanPokok.setText(satuanBahanPokokSelected);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                rvMakananBahanPokok.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                rvMakananBahanPokok.setLayoutManager(layoutManager);
                makananBahanPokokAdapter = new MakananBahanPokokAdapter(appContext, mMakananBahanPokok, (makananBahanPokok, pos) -> {
                });
                rvMakananBahanPokok.setAdapter(makananBahanPokokAdapter);

                if(screenState.equals(MyConstants.UBAH_MAKANAN)) {
                    Bundle bundle = getIntent().getExtras();
                    mBahanPokokSelected = (ArrayList<BahanPokok>) bundle.getSerializable("daftarBahanPokokSelected");
                    for (int i = 0; i < mBahanPokokSelected.size(); i++) {
                        insertMethod(mBahanPokokSelected.get(i).getIdBahanPokok(), mBahanPokokSelected.get(i).getNamaBahanPokok(), mBahanPokokSelected.get(i).getJumlahBahanPokok(), mBahanPokokSelected.get(i).getSatuanBahanPokok());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        CommonUtils.hideLoading();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTambahGambar: {
                openGallery();
            }
            break;
            case R.id.btnTambahMakananBahanPokok: {
                String txtJumlahBahanPokok = etJumlahBahanPokok.getText().toString();

                if(txtJumlahBahanPokok.isEmpty()) {
                    CommonUtils.showToast(appContext, appContext.getString(R.string.label_input_kosong));
                    return;
                }

                if(mMakananBahanPokok.size() > mBahanPokok.size()-1) {
                    CommonUtils.showToast(appContext, appContext.getString(R.string.label_data_melampaui));
                    return;
                }

                for(int i = 0 ; i < mMakananBahanPokok.size() ; i++) {
                    if(String.valueOf(mMakananBahanPokok.get(i).getBahan_pokok_id()).equals(idBahanPokokSelected)) {
                        CommonUtils.showToast(appContext, appContext.getString(R.string.label_data_sama));
                        return;
                    }
                }

                insertMethod(idBahanPokokSelected, bahanPokokSelected, txtJumlahBahanPokok, satuanBahanPokokSelected);
            }
            break;
            case R.id.btnKirimMakanan: {
                txtNamaMakanan = etNamaMakanan.getText().toString();
                txtHargaMakanan = etHargaMakanan.getText().toString();

                if(txtNamaMakanan.isEmpty() || txtHargaMakanan.isEmpty() || mMakananBahanPokok.size() < 1) {
                    CommonUtils.showToast(appContext, appContext.getString(R.string.label_input_kosong));
                    return;
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage(R.string.confirmation_dialog_submit);
                    builder.setCancelable(false);
                    builder.setPositiveButton(R.string.label_yes, (dialog, which) -> callSubmitDataMakananRequest(v.getContext()));
                    builder.setNegativeButton(R.string.label_no, (dialog, which) -> {
                    });
                    builder.show();
                }
            }
            break;
        }
    }

    private void callSubmitDataMakananRequest(Context context){
        CommonUtils.showLoading(context);
        VolleyAPI volleyAPI = new VolleyAPI(context);
        JSONArray jsonArray = new JSONArray();

        for(int i = 0; i < mMakananBahanPokok.size() ; i++){
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("bahan_pokok_id",mMakananBahanPokok.get(i).getBahan_pokok_id());
                jsonObject.put("jumlah",mMakananBahanPokok.get(i).getJumlah());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Map<String, String> params = new HashMap<>();
        params.put("nama_makanan", txtNamaMakanan);
        params.put("harga_jual", txtHargaMakanan);
        params.put("gambar_makanan", encodedImage);
        params.put("bahanMakanan", jsonArray.toString());

        if (screenState.equals(MyConstants.UBAH_MAKANAN)) {
            params.put("makanan_id", idMakanan);
        }

        if (screenState.equals(MyConstants.UBAH_MAKANAN)) {
            volleyAPI.putRequest(MyConstants.MAKANAN_EDIT_ACTION, params, result -> {
                try {
                    JSONObject resultJSON = new JSONObject(result);
                    Intent myIntent = new Intent(context, MakananActivity.class);
                    startActivityForResult(myIntent, 0);

                    CommonUtils.showToast(context, resultJSON.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        } else if (screenState.equals(MyConstants.TAMBAH_MAKANAN)) {
            volleyAPI.postRequest(MyConstants.MAKANAN_ADD_ACTION, params, result -> {
                try {
                    JSONObject resultJSON = new JSONObject(result);
                    Intent myIntent = new Intent(context, MakananActivity.class);
                    startActivityForResult(myIntent, 0);

                    CommonUtils.showToast(context, resultJSON.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }

        CommonUtils.hideLoading();
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            final Uri imageUri = data.getData();
            Bitmap selectedImage = null;
            try {
                selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            assert selectedImage != null;
            selectedImage.compress(Bitmap.CompressFormat.JPEG,100,baos);
            byte[] b = baos.toByteArray();
            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            ivGambarMakanan.setImageURI(imageUri);
        }
    }

    private void getGambarMakananDetail() {
        CommonUtils.showLoading(MakananEntryActivity.this);
        VolleyAPI volleyAPI = new VolleyAPI(MakananEntryActivity.this);

        Map<String, String> params = new HashMap<>();
        params.put("makanan_id", idMakanan);

        volleyAPI.getRequest(MyConstants.MAKANAN_GET_IMAGE_DETAIL_ACTION, params, result -> {
            decodeImage =  result;

            if(decodeImage != null) {
                byte[] decodedString = Base64.decode(decodeImage, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                ivGambarMakanan.setImageBitmap(decodedByte);
                encodedImage = decodeImage;
            }
        });

        CommonUtils.hideLoading();
    }

    private void insertMethod(String id, String name, String amount, String unit) {
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("bahan_pokok_id", id);
            jsonObject.put("name", name);
            jsonObject.put("jumlah", amount);
            jsonObject.put("satuan", unit);

            MakananBahanPokok makananBahanPokok = gson.fromJson(String.valueOf(jsonObject), MakananBahanPokok.class);
            mMakananBahanPokok.add(makananBahanPokok);
            makananBahanPokokAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}