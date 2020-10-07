package com.example.inventaristoko.Screens.Makanan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MakananEntryActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_IMAGE = 100;
    private ArrayList<MakananBahanPokok> makananBahanPokoks = new ArrayList<>();
    private ArrayList<BahanPokok> mBahanPokok = new ArrayList<>();
    private ArrayList<BahanPokok> mBahanPokokNew = new ArrayList<>();
    private ArrayList<BahanPokok> bahanPokoks = new ArrayList<>();
    private EditText etNamaMakanan, etHargaMakanan, etJumlahBahanPokok;
    private TextView tvSatuanBahanPokok;
    private MakananBahanPokokAdapter makananBahanPokokAdapter;
    private ImageView ivGambarMakanan;
    private int position;
    private RecyclerView mRecyclerView;
    private Button btnTambahGambar, btnKirimMakanan, btnTambahMakananBahanPokok;
    private Spinner spnDaftarBahanPokok;
    private String encodedImage, screenState, idBahanPokokSelected, idMakanan, bahanPokokSelected, jumlahBahanPokok, satuanBahanPokokSelected, decodeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makanan_entry);

        mRecyclerView = findViewById(R.id.rvDataMakananBahanPokok);
        etNamaMakanan = findViewById(R.id.etNamaMakanan);
        etHargaMakanan = findViewById(R.id.etHargaMakanan);
        etJumlahBahanPokok = findViewById(R.id.etJumlahBahanPokok);
        tvSatuanBahanPokok = findViewById(R.id.tvSatuanBahanPokok);
        btnTambahGambar = findViewById(R.id.btnTambahGambar);
        btnTambahMakananBahanPokok = findViewById(R.id.btnTambahMakananBahanPokok);
        btnKirimMakanan = findViewById(R.id.btnKirimMakanan);
        ivGambarMakanan = findViewById(R.id.ivGambarMakanan);
        spnDaftarBahanPokok = findViewById(R.id.spnDaftarBahanPokok);

        Bundle bundle = getIntent().getExtras();
        screenState = bundle.getString("screenState");
        idMakanan = bundle.getString("idMakanan");

        getSemuaBahanPokok();
        getGambarMakananDetail();

        if(screenState.equals(MyConstants.UBAH_MAKANAN)) {
            getSupportActionBar().setTitle(R.string.menu_ubah_makanan);
            etNamaMakanan.setText(bundle.getString("namaMakanan"));
            etHargaMakanan.setText(bundle.getString("hargaMakanan"));
        } else {
            getSupportActionBar().setTitle(R.string.menu_tambah_makanan);
        }

        btnTambahMakananBahanPokok.setOnClickListener(this);
        btnTambahGambar.setOnClickListener(this);
        btnKirimMakanan.setOnClickListener(this);

        etNamaMakanan.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });

        etHargaMakanan.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });

        etJumlahBahanPokok.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnTambahGambar: {
                openGallery();
            }
            break;

            case R.id.btnTambahMakananBahanPokok: {
                if(makananBahanPokoks.size() > mBahanPokok.size()-1) {
                    Toast.makeText(getApplicationContext(),R.string.label_data_melampaui, Toast.LENGTH_SHORT).show();
                    return;
                }

                for(int i = 0 ; i < makananBahanPokoks.size() ; i++) {
                    if(String.valueOf(makananBahanPokoks.get(i).getBahan_pokok_id()).equals(idBahanPokokSelected)) {
                        Toast.makeText(getApplicationContext(), R.string.label_data_sama, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                jumlahBahanPokok = etJumlahBahanPokok.getText().toString();

                insertMethod(idBahanPokokSelected, bahanPokokSelected, jumlahBahanPokok, satuanBahanPokokSelected);
            }
            break;

            case R.id.btnKirimMakanan: {
                if(String.valueOf(etNamaMakanan.getText()).equals("") || String.valueOf(etHargaMakanan.getText()).equals("") || makananBahanPokoks.size() < 1) {
                    Toast.makeText(getApplicationContext(), R.string.label_data_kosong, Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage(R.string.confirmation_dialog_submit);
                    builder.setCancelable(false);
                    builder.setPositiveButton(R.string.label_yes, (dialog, which) -> {
                        CommonUtils.showLoading(view.getContext());
                        VolleyAPI volleyAPI = new VolleyAPI(view.getContext());
                        JSONArray jsonArray = new JSONArray();
                        for(int i = 0; i < makananBahanPokoks.size() ; i++){
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("bahan_pokok_id",makananBahanPokoks.get(i).getBahan_pokok_id());
                                jsonObject.put("jumlah",makananBahanPokoks.get(i).getJumlah());
                                jsonArray.put(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Map<String, String> params = new HashMap<>();
                        params.put("nama_makanan", etNamaMakanan.getText().toString());
                        params.put("harga_jual", etHargaMakanan.getText().toString());
                        params.put("gambar_makanan", encodedImage);
                        params.put("bahanMakanan", jsonArray.toString());

                        if (screenState.equals(MyConstants.UBAH_MAKANAN)) {
                            params.put("makanan_id", idMakanan);
                        }

                        if (screenState.equals(MyConstants.UBAH_MAKANAN)) {
                            volleyAPI.putRequest("updateMakanan", params, result -> {
                                try {
                                    JSONObject resultJSON = new JSONObject(result);
                                    Intent myIntent = new Intent(getApplicationContext(), MakananActivity.class);
                                    startActivityForResult(myIntent, 0);
                                    Toast.makeText(getApplicationContext(), resultJSON.getString("message"), Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            });
                        } else if (screenState.equals(MyConstants.TAMBAH_MAKANAN)) {
                            volleyAPI.postRequest("addMakanan", params, result -> {
                                try {
                                    JSONObject resultJSON = new JSONObject(result);
                                    Intent myIntent = new Intent(getApplicationContext(), MakananActivity.class);
                                    startActivityForResult(myIntent, 0);
//                                    finish();
                                    Toast.makeText(getApplicationContext(), resultJSON.getString("message"), Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                        CommonUtils.hideLoading();
                    });
                    builder.setNegativeButton(R.string.label_no, (dialog, which) -> {
                    });
                    builder.show();
                }
            }
            break;
        }
    }

    private void getSemuaBahanPokok() {
        ArrayList<BahanPokok> mBahanPokoks = new ArrayList<>();
        VolleyAPI volleyAPI = new VolleyAPI(MakananEntryActivity.this);

        Map<String, String> params = new HashMap<>();

        volleyAPI.getRequest("getSemuaBahanPokok", params, result -> {
            try {
                JSONObject resultJSON = new JSONObject(result);
                JSONArray resultArray = resultJSON.getJSONArray("result");

                for(int i = 0 ; i < resultArray.length() ; i ++ ) {
                    JSONObject dataBahanPokok = (JSONObject) resultArray.get(i);
                    BahanPokok bahanPokok = new BahanPokok();
                    bahanPokok.setIdBahanPokok(dataBahanPokok.getString("bahan_pokok_id"));
                    bahanPokok.setNamaBahanPokok(dataBahanPokok.getString("nama"));
                    bahanPokok.setSatuanBahanPokok(dataBahanPokok.getString("satuan"));
                    mBahanPokoks.add(bahanPokok);
                }

                mBahanPokok = mBahanPokoks;

                if(mBahanPokok != null) {
                    for (int i = 0; i < mBahanPokok.size(); i++) {
                        bahanPokoks.add(new BahanPokok(mBahanPokok.get(i).getIdBahanPokok(), mBahanPokok.get(i).getNamaBahanPokok(), mBahanPokok.get(i).getSatuanBahanPokok()));
                    }
                }

                ArrayAdapter<BahanPokok> adapterBahanPokok = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, bahanPokoks);
                adapterBahanPokok.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                spnDaftarBahanPokok.setAdapter(adapterBahanPokok);
                spnDaftarBahanPokok.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        bahanPokokSelected = bahanPokoks.get(position).getNamaBahanPokok();
                        idBahanPokokSelected = bahanPokoks.get(position).getIdBahanPokok();
                        satuanBahanPokokSelected = bahanPokoks.get(position).getSatuanBahanPokok();
                        tvSatuanBahanPokok.setText(satuanBahanPokokSelected);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                mRecyclerView.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                mRecyclerView.setLayoutManager(layoutManager);
                makananBahanPokokAdapter = new MakananBahanPokokAdapter(getApplicationContext(), makananBahanPokoks, (makananBahanPokok, pos) -> position = pos);
                mRecyclerView.setAdapter(makananBahanPokokAdapter);

                if(screenState.equals(MyConstants.UBAH_MAKANAN)) {
                    Bundle bundle = getIntent().getExtras();
                    mBahanPokokNew = (ArrayList<BahanPokok>) bundle.getSerializable("daftarBahanPokokSelected");
                    for (int i = 0; i < mBahanPokokNew.size(); i++) {
                        insertMethod(mBahanPokokNew.get(i).getIdBahanPokok(), mBahanPokokNew.get(i).getNamaBahanPokok(), mBahanPokokNew.get(i).getJumlahBahanPokok(), mBahanPokokNew.get(i).getSatuanBahanPokok());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
            selectedImage.compress(Bitmap.CompressFormat.JPEG,100,baos);
            byte[] b = baos.toByteArray();
            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            ivGambarMakanan.setImageURI(imageUri);
        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    private void getGambarMakananDetail() {
        VolleyAPI volleyAPI = new VolleyAPI(MakananEntryActivity.this);

        Map<String, String> params = new HashMap<>();
        params.put("makanan_id", idMakanan);

        volleyAPI.getRequest("getGambarMakananDetail", params, result -> {
            decodeImage =  result;

            if(decodeImage != null) {
                byte[] decodedString = Base64.decode(decodeImage, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                ivGambarMakanan.setImageBitmap(decodedByte);
                encodedImage = decodeImage;
            }
        });
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
            makananBahanPokoks.add(makananBahanPokok);
            makananBahanPokokAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}