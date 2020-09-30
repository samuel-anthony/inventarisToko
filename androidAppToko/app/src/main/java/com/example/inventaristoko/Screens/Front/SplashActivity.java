package com.example.inventaristoko.Screens.Front;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.inventaristoko.Screens.Penjualan.PenjualanActivity;
import com.example.inventaristoko.Utils.Preferences;


public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Preferences.getLoggedInStatus(getBaseContext())){
            startActivity(new Intent(getBaseContext(), PenjualanActivity.class));
        } else {
            startActivity(new Intent(getBaseContext(), HomeActivity.class));
        }
        finish();
    }
}
