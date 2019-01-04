package com.example.volleydemo.initdata;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cover.attractions.Attraction;
import com.example.cover.attractions.Attractions;
import com.example.volleydemo.MyApplication;
import com.example.volleydemo.util.VolleyInterface;
import com.example.volleydemo.util.VolleyRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/9 0009.
 */

public class AttractionData {
    private boolean flag=true;
    private Context context;
    private VolleyCallBack volleyCallBack;
    Attractions attrations = Attractions.getAttractions();
    public AttractionData(Context context){
        this.context = context;
    }

    //接口:
    public interface VolleyCallBack{
        public void getAttractions(Attractions attractions);
    }

    //传入接口:
    public void setVolleyCallBack(VolleyCallBack volleyCallBack){
          this.volleyCallBack = volleyCallBack;
    }



    public void getAttrationData(String url){
        VolleyRequest.requestGet(url, "Volley_package", new VolleyInterface() {
            @Override
            public void onMySuccess(String result) {
               //Attractions attrations = JSON.parseObject(result,Attractions.class);
               // ArrayList<Attraction> mList= (ArrayList<Attraction>) JSON.parseArray(result,Attraction.class);
//              List<Attractions> mList = JSONArray.parseArray(result,Attractions.class);
               //com.alibaba.fastjson.JSONObject jsonObkect = JSON.parseObject(result);

                //将数据写到集合里面:
                attrations = JSON.parseObject(result,Attractions.class);

                    if (volleyCallBack != null) {
                        volleyCallBack.getAttractions(attrations);
                    }

                //Toast.makeText(context, "result=" + attrations.getAttractionList().get(0).getName().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMyError(VolleyError error) {
                Toast.makeText(context, "服务器宕机", Toast.LENGTH_SHORT).show();

            }
        });


    }




}
