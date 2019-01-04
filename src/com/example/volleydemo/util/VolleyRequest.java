package com.example.volleydemo.util;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.volleydemo.MyApplication;

import java.util.Map;

/**
 * Created by Administrator on 2018/4/9 0009.
 */

public class VolleyRequest {
    public static StringRequest request;


    public static void requestGet(String url,String tag,VolleyInterface vif) {
        MyApplication.getHttpQueues().cancelAll(tag);

        request = new StringRequest(Request.Method.GET, url,vif.loadingListener() , vif.errorListener());
        request.setTag(tag);
        MyApplication.getHttpQueues().add(request);
        MyApplication.getHttpQueues().start();
    }

    public static void RequestPost(String url, String tag, final Map<String,String> params,VolleyInterface vif) {
        MyApplication.getHttpQueues().cancelAll(tag);
        request = new StringRequest(Request.Method.POST, url,vif.loadingListener(),vif.errorListener()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        request.setTag(tag);
        MyApplication.getHttpQueues().add(request);
        MyApplication.getHttpQueues().start();
    }
}
