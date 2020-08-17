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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class PenjualanDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ListView listView;
    ArrayAdapter<CharSequence> adapter;
    TextView tvStatus;
    String[] statues = { "Ordering", "On Going", "Finished"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setTitle(R.string.label_detil_pesanan);

        listView = findViewById(R.id.listView);
        tvStatus = findViewById(R.id.status);
        adapter = ArrayAdapter.createFromResource(this, R.array.detil_makanan,android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

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
        tvStatus.setText(statues[position]);

        if(tvStatus.getText() == "Ordering") {
            tvStatus.setTextColor(getResources().getColor(R.color.colorBlack));
        } else if(tvStatus.getText() == "On Going") {
            tvStatus.setTextColor(getResources().getColor(R.color.colorProcess));
        } else if(tvStatus.getText() == "Finished") {
            tvStatus.setTextColor(getResources().getColor(R.color.colorSuccess));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO - Custom Code
    }
}
