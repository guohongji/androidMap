package com.example.debristravels;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2018/3/24 0024.
 */

public class MyCamera extends Activity implements SurfaceHolder.Callback{

    private Camera mCamera;
    private SurfaceView mPreview;
    private SurfaceHolder mHolder;
    private int degrees;//设置相机的旋转角度

    //回调
    private Camera.PictureCallback mPictureCallBack = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //  String tempFilePath = "/images/"+System.currentTimeMillis()+".png";
            String fileName = System.currentTimeMillis()+"";

            File tempFile = new File(Environment.getExternalStorageDirectory() + "/images/"+fileName+".png");

            FileOutputStream fos = null;

            if(!tempFile.exists()){
                tempFile.getParentFile().mkdir();
            }
            try {
                Log.i("hello","here");
                fos = new FileOutputStream(tempFile);
                fos.write(data);
                fos.close();
                Log.i("hello","here");


                Intent intent = new Intent();
                intent.putExtra("picPath",tempFile.getAbsolutePath());
                intent.putExtra("fileName",fileName);

                setResult(RESULT_OK,intent);
                Log.i("path---",tempFile.getAbsolutePath());
                finish();



            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_camera);
        mPreview = (SurfaceView) findViewById(R.id.preview);
        degrees = 90;
        //初始化Holder对象
        mHolder = mPreview.getHolder();
        mHolder.addCallback(this);

        mPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.autoFocus(null);
            }
        });


    }
    public void capture(View view){


        //获取相机的参数
        Camera.Parameters parameters = mCamera.getParameters();
        //设置拍照的格式为jpeg
        parameters.setPictureFormat(ImageFormat.JPEG);
        //设置预览格式的大小
        parameters.setPreviewSize(800,400);
        //设置对焦功能
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        //获取最清晰的焦距的时候再进行拍摄
//        Log.i("hello","here");
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if(success){
                    //对角成功后拍照,进行通过mPictureCallBack进行回调
                    mCamera.takePicture(null,null,mPictureCallBack);
                }
            }
        });


    }
    /*
    * 获取系统的Camera对象
    * 导入硬件Camera包
    * */
    private Camera getCamera(){

        Camera camera = null;
        try{
            camera = camera.open();
        }catch (RuntimeException e){
            camera = null;
        }

        return camera;

    }


    /*
    * 开始预览相机的内容
    * 需要将相机的内容显示在我们的activity之中
    *camera:传入一个相机对象
    * holder:传入控制器
    * */
    private void setStartPreview(Camera camera,SurfaceHolder holder){
        try {
            camera.setPreviewDisplay(holder);
            //系统的camera默认是横屏的,添加方法使之竖着显示
            camera.setDisplayOrientation(degrees);
            camera.startPreview();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /*
    * 释放相机的资源,请求相机必须关闭
    * 将其与activity生命周期绑定
    * */
    private void releaseCamera(){
        if(mCamera != null){
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(mCamera == null){
            mCamera = getCamera();
            if(mHolder != null){
                Log.i("hello","here");
                setStartPreview(mCamera,mHolder);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseCamera();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setStartPreview(mCamera,mHolder);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();
        setStartPreview(mCamera,mHolder);

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();

    }

    }

