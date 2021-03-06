package com.example.inventaristoko.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.example.inventaristoko.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class CommonUtils {
    static ProgressDialog progressDialog;
    private static final String TAG = "CommonUtils";

    private CommonUtils() { }

    public static ProgressDialog showLoading(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.show();

        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        return progressDialog;
    }

    public static void hideLoading() {
        if (progressDialog == null) {
            return;
        }
        progressDialog.dismiss();
    }

    public static void showToast(Context appContext, String message) {
        Toast.makeText(appContext, message, Toast.LENGTH_SHORT).show();
    }

    public static String currencyFormat(String nominal){
        DecimalFormat toRupiah = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatAngka = new DecimalFormatSymbols();

        formatAngka.setCurrencySymbol("Rp. ");
        formatAngka.setMonetaryDecimalSeparator('.');
        formatAngka.setGroupingSeparator('.');

        toRupiah.setDecimalFormatSymbols(formatAngka);
        String hasil = toRupiah.format(Double.valueOf(nominal));

        return hasil;
    }

    public static String currencyFormatWithoutRupiah(String nominal){
        DecimalFormat toRupiah = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatAngka = new DecimalFormatSymbols();

        formatAngka.setMonetaryDecimalSeparator('.');
        formatAngka.setGroupingSeparator('.');

        toRupiah.setDecimalFormatSymbols(formatAngka);
        String hasil = toRupiah.format(Double.valueOf(nominal));

        return hasil.substring(1);
    }

    public static String dateFormat() {
        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        String hasil = df.format(d);

        return hasil;
    }

    public static String dateFormat(String input) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Date date = Calendar.getInstance().getTime();
        try {
            date = format.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        String hasil = df.format(date);

        return hasil;
    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
