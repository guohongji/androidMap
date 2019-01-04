package com.example.database.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2018/4/13 0013.
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "pic.db";
    private static final String TBL_NAME = "pic_des_tb";

    private SQLiteDatabase db;
    public DBOpenHelper(Context context){
        super(context, DB_NAME, null, 2);
    }

    public DBOpenHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }
    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override//�״ε������ݿ��ʱ�����,һ����ԷŽ���,����Ĳ���
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        //TODO ������
        String CREATE_TBL = "create table if not exists pic_des_tb(_id integer primary key autoincrement,fileName text,describe text)";

        db.execSQL(CREATE_TBL);

    }
    //����
    public void insert(ContentValues values){
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TBL_NAME,null,values);
        db.close();
    }
    //��ѯ
    public Cursor query(){
        SQLiteDatabase db = getReadableDatabase();
        //Cursor cursor = db.query(TBL_NAME,null,null,null,null,null,null);
        Cursor cursor   = db.rawQuery("select * from pic_des_tb", null);
        return cursor;
    }
    //ɾ��
    public void del(int id){
        if(db == null)
            db = getWritableDatabase();
        db.delete(TBL_NAME,"_id=?",new String[]{String.valueOf(id)});
    }
    //�ر����ݿ�
    public void close(){
        if(db != null){
            db.close();
        }
    }






    @Override//�����ݿ�汾�����仯��ʱ��,���Զ�ִ��
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
