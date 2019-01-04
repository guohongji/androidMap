package com.example.myinfo;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.RecordAdapter;
import com.example.database.helper.MyDBHelper;
import com.example.debristravels.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WZJSB-01 on 2017/12/5.
 */

public class Fragment_One extends Fragment {

    private String[] texts;
    private Bitmap[] bitmaps;
    private RecyclerView mRecyclerView;

    Button addDiary;

    //获取数据库的数据
    SQLiteDatabase db;
    MyDBHelper myDBHelper = null;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_viewpager_layout, null);
        myDBHelper = new MyDBHelper(getActivity());

        //初始化数据库
        db =  myDBHelper.getDB();




        initContext(view);
        addDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"您选择了", Toast.LENGTH_SHORT).show();
                //TODO




            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        RecordAdapter adapter = new RecordAdapter(getActivity(),texts,bitmaps);

        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecordAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, RecordAdapter.ViewName viewName, int position) {
               //Toast.makeText(getActivity(), viewName.toString()+position, Toast.LENGTH_SHORT).show();
                //TODO

                String describe="";
                TextView mTextView;
                if(viewName.toString().equals(RecordAdapter.ViewName.PRACTISE.toString())){
                    Toast.makeText(getActivity(),viewName.toString() , Toast.LENGTH_SHORT).show();

                }else {


                    if (view.getId() == R.id.diary_text) {
                        mTextView = (TextView) view;
                        describe = mTextView.getText().toString();
                        Toast.makeText(getActivity(), describe, Toast.LENGTH_SHORT).show();

                    } else {
                        mTextView = (TextView) view.findViewById(R.id.diary_text);
                        describe = mTextView.getText().toString();
                        Toast.makeText(getActivity(), describe, Toast.LENGTH_SHORT).show();
                    }
                }//else结束


            }
        }

        );




        return view;
    }

    private void initContext(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_image);
        addDiary = (Button) view.findViewById(R.id.add_diary);
        //TODO

        String filePath = Environment.getExternalStorageDirectory() + "/images/";
        File fileAll = new File(filePath);
        File[] files = fileAll.listFiles();
        //计数
        int num = files.length;
        //这里省去判断文件下下面是否是图片的逻辑,前提是文件下下面存放的必须都是图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        if(num!=0){
            bitmaps = new Bitmap[num];
            texts = new String[num];
            //将所有文件存入ArrayList
            for(int i = 0;i<num;i++){
                File file = files[i];
                if(checkIsImageFile(file.getPath())){
                    BitmapFactory.decodeFile(file.getPath(),options);
                    int imageHeight = options.outHeight;
                    int imageWidth = options.outWidth;
                    options.inSampleSize = 1;
                    int w = 320;
                    int h = 480;
                   // h = w*imageHeight/imageWidth;//计算出宽高等比率
                    int a = imageWidth / w;
                    int b = imageHeight / h;
                    options.inSampleSize = Math.max(a, b);
                    options.inJustDecodeBounds = false;
                    bitmaps[i] = BitmapFactory.decodeFile(file.getPath(), options);


                    String fileName = getFileNameWithoutSuffix(file);
                    Cursor cursor = db.rawQuery("select * from pic_des_tb where fileName='"+fileName+"'",null);

                    //Log.i("shylockout",cursor.getString(cursor.getColumnIndex("describe"))+"");
                    if(cursor.moveToFirst()) {
                        Log.i("shylockout",cursor.getString(cursor.getColumnIndex("describe"))+"");
                        texts[i] = cursor.getString(cursor.getColumnIndex("describe")) + "";
                    }else{
                        texts[i] =null;
                    }


                }
        }
        //结束if
        }
    }
    public static String getFileNameWithoutSuffix(File file){
        String file_name = file.getName();
        return file_name.substring(0, file_name.lastIndexOf("."));
    }
    /*
    * 过滤图片文件
    * */
    private boolean checkIsImageFile(String fName) {
        boolean isImageFile = false;
        // 获取扩展名
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                fName.length()).toLowerCase();
        if (FileEnd.equals("jpg") || FileEnd.equals("png") || FileEnd.equals("gif")
                || FileEnd.equals("jpeg")|| FileEnd.equals("bmp") ) {
            isImageFile = true;
        } else {
            isImageFile = false;
        }
        return isImageFile;
    }




}
