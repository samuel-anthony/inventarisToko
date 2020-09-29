package com.example.inventaristoko.Screens.Makanan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.VolleyAPI;
import com.example.inventaristoko.Utils.VolleyCallback;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MakananEntryActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 100;
    private EditText etNamaMakanan, etHargaMakanan;
    private ImageView ivGambarMakanan;
    private Button btnTambahGambar, btnKirimMakanan;
    private String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makanan_entry);

        Bundle bundle = getIntent().getExtras();
        String screenState = bundle.getString("screenState");

        etNamaMakanan = findViewById(R.id.etNamaMakanan);
        etHargaMakanan = findViewById(R.id.etHargaMakanan);
        ivGambarMakanan = findViewById(R.id.ivGambarMakanan);

        if(screenState.equals(MyConstants.UBAH_MEJA)) {
            getSupportActionBar().setTitle(R.string.menu_ubah_makanan);
            etNamaMakanan.setText(bundle.getString("namaMakanan"));
            etHargaMakanan.setText(bundle.getString("hargaMakanan"));
        } else {
            getSupportActionBar().setTitle(R.string.menu_tambah_makanan);
        }

        btnTambahGambar = (Button)findViewById(R.id.btnTambahGambar);
        btnTambahGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btnKirimMakanan = findViewById(R.id.btnKirimMakanan);
        btnKirimMakanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MakananEntryActivity.this);
                builder.setMessage("Anda Yakin Ingin Kirim Data ini?");
                builder.setCancelable(false);
                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CommonUtils.showLoading(MakananEntryActivity.this);
                        VolleyAPI volleyAPI = new VolleyAPI(MakananEntryActivity.this);
                        Map<String, String> params = new HashMap<>();
                        params.put("nama_makanan", etNamaMakanan.getText().toString());
                        params.put("harga_jual", etHargaMakanan.getText().toString());
                        params.put("gambar_makanan", encodedImage);

                        if (screenState.equals(MyConstants.UBAH_MEJA)) {
                            params.put("makanan_id", bundle.getString("idMakanan"));
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
                            volleyAPI.postRequest("addNewMakanan", params, new VolleyCallback() {
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

        etNamaMakanan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        etHargaMakanan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
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
}