package com.example.inventaristoko;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

public class volleyAPI {

    private static final String TAG = volleyAPI.class.getName();
    private String ip = "http://192.168.0.104:8000/";
    private String URL = ip+"api/";

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private Context context;

    public volleyAPI(Context context){
        this.context = context;
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public void putRequest(String service,Map<String, String> params,final VolleyCallback callback){
        mStringRequest = new StringRequest(Request.Method.GET, URL+service, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context,"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen
                callback.onSuccessResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG,"Error :" + error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        mStringRequest.setRetryPolicy(policy);
        mRequestQueue.add(mStringRequest);
    }

    public void postRequest(String service,Map<String, String> params,final VolleyCallback callback){
        mStringRequest = new StringRequest(Request.Method.POST, URL+service, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context,"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen
                callback.onSuccessResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG,"Error :" + error.toString());
                Log.i(TAG,"Error :" + error.getMessage());
                Log.i(TAG,"Error :" + error.getCause());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        int socketTimeout = 10000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        mStringRequest.setRetryPolicy(policy);
        mRequestQueue.add(mStringRequest);
    }
}
