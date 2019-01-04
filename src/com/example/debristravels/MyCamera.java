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
    private int degrees;//�����������ת�Ƕ�

    //�ص�
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
        //��ʼ��Holder����
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


        //��ȡ����Ĳ���
        Camera.Parameters parameters = mCamera.getParameters();
        //�������յĸ�ʽΪjpeg
        parameters.setPictureFormat(ImageFormat.JPEG);
        //����Ԥ����ʽ�Ĵ�С
        parameters.setPreviewSize(800,400);
        //���öԽ�����
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        //��ȡ�������Ľ����ʱ���ٽ�������
//        Log.i("hello","here");
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if(success){
                    //�Խǳɹ�������,����ͨ��mPictureCallBack���лص�
                    mCamera.takePicture(null,null,mPictureCallBack);
                }
            }
        });


    }
    /*
    * ��ȡϵͳ��Camera����
    * ����Ӳ��Camera��
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
    * ��ʼԤ�����������
    * ��Ҫ�������������ʾ�����ǵ�activity֮��
    *camera:����һ���������
    * holder:���������
    * */
    private void setStartPreview(Camera camera,SurfaceHolder holder){
        try {
            camera.setPreviewDisplay(holder);
            //ϵͳ��cameraĬ���Ǻ�����,��ӷ���ʹ֮������ʾ
            camera.setDisplayOrientation(degrees);
            camera.startPreview();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /*
    * �ͷ��������Դ,�����������ر�
    * ������activity�������ڰ�
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

