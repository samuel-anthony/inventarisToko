package com.example.inventaristoko.Screens.Pengunjung;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.CommonUtils;

import java.util.Objects;

public class PengunjungActivity extends AppCompatActivity {
    private Context appContext;
    private Toolbar toolbarPengunjung;
    private TextView tvNamaMejaPengunjung;
    private long backPressedTime;

    private void init() {
        appContext = getApplicationContext();
        toolbarPengunjung = findViewById(R.id.toolbarPengunjung);
        tvNamaMejaPengunjung = findViewById(R.id.tvNamaMejaPengunjung);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengunjung);

        init();

        setSupportActionBar(toolbarPengunjung);
//        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.label_welcome));

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        tvNamaMejaPengunjung.setText(bundle.getString("namaMeja"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.string.tombol_history :
//                Intent intent = new Intent(getApplicationContext(), PengunjungHistoryActivity.class);
//                startActivityForResult(intent, 0);

                CommonUtils.showToast(appContext, "Menu Riwayat");
                break;
            case R.string.tombol_notification :
//                intent = new Intent(getApplicationContext(), PengunjungDaftarActivity.class);
//                startActivityForResult(intent, 0);

                CommonUtils.showToast(appContext, "Menu Daftar");
                break;
            case R.string.tombol_keluar :
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.confirmation_dialog_logout);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.label_yes, (dialog, which) -> {
                    CommonUtils.showLoading(PengunjungActivity.this);
//                    Preferences.clearLoggedInUser(getBaseContext());
                    startActivity(new Intent(getBaseContext(), PengunjungActivity.class));
                    finish();
                    CommonUtils.hideLoading();
                });
                builder.setNegativeButton(R.string.label_no, (dialog, which) -> {
                });
                builder.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_menu_pengunjung, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            CommonUtils.showToast(appContext, appContext.getString(R.string.press_once_again));
        }

        backPressedTime = System.currentTimeMillis();
    }
}
