package com.example.volleydemo.util;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by Administrator on 2018/4/9 0009.
 */

public abstract class VolleyInterface {
    public Response.Listener<String> listener;
    public Response.ErrorListener errorListener;
    public abstract void onMySuccess(String result);

    public abstract void onMyError(VolleyError error);

    public Response.Listener<String> loadingListener() {
        listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                onMySuccess(s);
            }
        };
        return listener;
    }

    public Response.ErrorListener errorListener() {
        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onMyError(volleyError);
            }
        };
        return errorListener;
    }
}
