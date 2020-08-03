package com.example.inventaristoko;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import static android.content.ContentValues.TAG;

public class volleyAPI {

    private static final String TAG = volleyAPI.class.getName();
    private String ip = "10.254.135.86/";
    private String URL = ip+"api/";

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private Context context;

    public volleyAPI(Context context){
        this.context = context;
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public void testApi(){
        mStringRequest = new StringRequest(Request.Method.GET, URL+"bahanPokok", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context,"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG,"Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);
    }
}
