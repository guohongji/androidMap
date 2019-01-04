package com.example.database.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2018/5/11 0011.
 */

public class MyDBHelper {
    public static final String DB_NAME = "pic.db";
    public static final String TBL_NAME = "pic_des_tb";

    private Context context = null;
    private SQLiteDatabase db = null;

    public MyDBHelper(Context context){
        this.context = context;
    }

public SQLiteDatabase getDB(){

    db = context.openOrCreateDatabase("pic.db", context.MODE_PRIVATE, null);
    db.execSQL("create table if not exists pic_des_tb(_id integer primary key autoincrement,fileName text,describe text)");
    return db;

}





}
