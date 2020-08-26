package com.example.inventaristoko.Screens.Pengguna;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.MyConstants;

public class PenggunaDetailActivity extends AppCompatActivity {
    private Button btnDelete, btnEdit;
    private TextView tvFullName, tvUserName, tvEmail, tvPhone, tvBirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengguna_detail);

        getSupportActionBar().setTitle(R.string.label_detil_pengguna);

        tvFullName = findViewById(R.id.labelValueFullName);
        tvUserName = findViewById(R.id.labelValueUserName);
        tvEmail = findViewById(R.id.labelValueEmail);
        tvPhone = findViewById(R.id.labelValuePhone);
        tvBirth = findViewById(R.id.labelValueBirth);

        btnDelete = findViewById(R.id.buttonDelete);
        btnEdit = findViewById(R.id.buttonEdit);

        Bundle bundle = getIntent().getExtras();
        tvFullName.setText(bundle.getString("fullName"));
        tvUserName.setText(bundle.getString("userName"));
        tvEmail.setText(bundle.getString("email"));
        tvPhone.setText(bundle.getString("phoneNumber"));
        tvBirth.setText(bundle.getString("birthDate"));

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PenggunaDetailActivity.this);
                builder.setMessage("Anda Yakin Ingin Menghapus Data ini?");
                builder.setCancelable(false);
                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(v.getContext(), "Hapus Berhasil", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent (v.getContext(), PenggunaEntryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("screenState", MyConstants.EDIT_PENGGUNA);
                bundle.putString("fullName", tvFullName.getText().toString());
                bundle.putString("userName", tvUserName.getText().toString());
                bundle.putString("email", tvEmail.getText().toString());
                bundle.putString("phoneNumber", tvPhone.getText().toString());
                bundle.putString("birthDate", tvBirth.getText().toString());
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
    }
}