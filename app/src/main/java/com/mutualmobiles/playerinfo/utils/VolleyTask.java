package com.mutualmobiles.playerinfo.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Map;

public abstract class VolleyTask {

    private Context context;
    private ProgressDialog mDialog;
    private String progressMessage;

    //constructor with parameters
    public VolleyTask(Context context, String url, Map<String, String> params, String progressMessage) {
        this.context = context;
        this.progressMessage = progressMessage;
        if (progressMessage != null && progressMessage.trim().length() > 1)
            showDialog(this.progressMessage);
        if (Networking.isNetworkAvailable(context))
            performTask(params, url);
        else
            Toast.makeText(context, "Internet is required", Toast.LENGTH_LONG).show();
    }

    private void performTask(final Map<String, String> params, final String url) {
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest mRequest = new JsonObjectRequest(Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(url, response.toString());
                hideDialog();
                handleResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                handleError(error);
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError,NullPointerException {
                Log.e("Params", params.toString());
                return params;
            }
        };
        mRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        mRequest.setShouldCache(true);
        mRequestQueue.add(mRequest);
    }

    //abstract methods for handling the error
    protected abstract void handleError(VolleyError error);

    //abstract method for handling the response
    protected abstract void handleResponse(JSONObject response);

    // to show the dialog while waiting for the response
    public void showDialog(String message) {
        mDialog = new ProgressDialog(context);
        mDialog.setMessage(message);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    //dismissing the dialog after the response or error is received
    public void hideDialog() {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
    }

}