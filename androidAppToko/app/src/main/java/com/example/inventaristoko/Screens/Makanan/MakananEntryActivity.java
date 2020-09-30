package com.example.inventaristoko.Screens.Makanan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.example.inventaristoko.Adapter.Makanan.BahanPokokMakananAdapter;
import com.example.inventaristoko.Model.BahanPokok.BahanPokok;
import com.example.inventaristoko.Model.Makanan.MakananBahanPokok;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.VolleyAPI;
import com.example.inventaristoko.Utils.VolleyCallback;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MakananEntryActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_IMAGE = 100;
    private ArrayList<MakananBahanPokok> makananBahanPokoks = new ArrayList<>();
    private ArrayList<BahanPokok> mBahanPokok = new ArrayList<>();
    private EditText etNamaMakanan, etHargaMakanan;
    private BahanPokokMakananAdapter bahanPokokMakananAdapter;
    private ImageView ivGambarMakanan;
    private int position;
    private RecyclerView mRecyclerView;
    private Button btnTambahGambar, btnKirimMakanan, btnTambahMakananBahanPokok;
    private Spinner spnDaftarBahanPokok;
    private String encodedImage, screenState, idBahanPokokSelected, bahanPokokSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makanan_entry);

        mRecyclerView = findViewById(R.id.rvDataMakananBahanPokok);
        etNamaMakanan = findViewById(R.id.etNamaMakanan);
        etHargaMakanan = findViewById(R.id.etHargaMakanan);
        btnTambahGambar = (Button) findViewById(R.id.btnTambahGambar);
        btnTambahMakananBahanPokok = findViewById(R.id.btnTambahMakananBahanPokok);
        btnKirimMakanan = findViewById(R.id.btnKirimMakanan);
        ivGambarMakanan = findViewById(R.id.ivGambarMakanan);
        spnDaftarBahanPokok = findViewById(R.id.spnDaftarBahanPokok);

        Bundle bundle = getIntent().getExtras();
        screenState = bundle.getString("screenState");
        mBahanPokok = (ArrayList<BahanPokok>) bundle.getSerializable("daftarBahanPokok");

        ArrayList<BahanPokok> bahanPokoks = new ArrayList<>();
        for (int i = 0 ; i < mBahanPokok.size() ; i++) {
            bahanPokoks.add(new BahanPokok(mBahanPokok.get(i).getIdBahanPokok(), mBahanPokok.get(i).getNamaBahanPokok()));
        }

        ArrayAdapter<BahanPokok> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, bahanPokoks);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spnDaftarBahanPokok.setAdapter(adapter);
        spnDaftarBahanPokok.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bahanPokokSelected = bahanPokoks.get(position).getNamaBahanPokok();
                idBahanPokokSelected = bahanPokoks.get(position).getIdBahanPokok();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if(screenState.equals(MyConstants.UBAH_MEJA)) {
            getSupportActionBar().setTitle(R.string.menu_ubah_makanan);
            etNamaMakanan.setText(bundle.getString("namaMakanan"));
            etHargaMakanan.setText(bundle.getString("hargaMakanan"));
        } else {
            getSupportActionBar().setTitle(R.string.menu_tambah_makanan);
        }

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        bahanPokokMakananAdapter = new BahanPokokMakananAdapter(getApplicationContext(), makananBahanPokoks, (makananBahanPokok, pos) -> position = pos);
        mRecyclerView.setAdapter(bahanPokokMakananAdapter);
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
                    Toast.makeText(getApplicationContext(),"MELAMPAUI BATAS JANCUK", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    insertMethod(idBahanPokokSelected, bahanPokokSelected);
                }
            }
            break;
            case R.id.btnKirimMakanan: {
                if(makananBahanPokoks.size() < 1) {
                    Toast.makeText(getApplicationContext(), R.string.label_data_kosong, Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage("Anda Yakin Ingin Kirim Data ini?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Iya", (dialog, which) -> {
                        CommonUtils.showLoading(view.getContext());
                        VolleyAPI volleyAPI = new VolleyAPI(view.getContext());
                        JSONArray jsonArray = new JSONArray();
                        for(int i = 0; i < makananBahanPokoks.size() ; i++){
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("bahan_pokok_id",makananBahanPokoks.get(i).bahan_pokok_id);
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
                            params.put("jenis_menu_id", "");
                        }

                        if (screenState.equals(MyConstants.UBAH_MAKANAN)) {
                            volleyAPI.putRequest("editMakanan", params, new VolleyCallback() {
                                @Override
                                public void onSuccessResponse(String result) {
                                    try {
                                        JSONObject resultJSON = new JSONObject(result);
                                        Intent myIntent = new Intent(getApplicationContext(), MakananActivity.class);
                                        startActivityForResult(myIntent, 0);
                                        Toast.makeText(getApplicationContext(), resultJSON.getString("message"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else if (screenState.equals(MyConstants.TAMBAH_MAKANAN)) {
                            volleyAPI.postRequest("addMakanan", params, new VolleyCallback() {
                                @Override
                                public void onSuccessResponse(String result) {
                                    try {
                                        JSONObject resultJSON = new JSONObject(result);
                                        Intent myIntent = new Intent(getApplicationContext(), MakananActivity.class);
                                        startActivityForResult(myIntent, 0);
                                        Toast.makeText(getApplicationContext(), resultJSON.getString("message"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                }
            }
            break;
        }
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
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            encodedImage = encodeImage(selectedImage);
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

    private void insertMethod(String id, String name) {
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("bahan_pokok_id", id);
            jsonObject.put("name", name);
            MakananBahanPokok makananBahanPokok = gson.fromJson(String.valueOf(jsonObject), MakananBahanPokok.class);
            makananBahanPokoks.add(makananBahanPokok);
            bahanPokokMakananAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}