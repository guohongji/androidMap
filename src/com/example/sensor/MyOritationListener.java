package com.example.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.sax.StartElementListener;

public class MyOritationListener implements SensorEventListener {

	//��ȡ������������
	private SensorManager mSensorManager;
	//������
	private Context mContext;
	//������
	private  Sensor mSensor;
	
	//��¼X,Y����Ϣ
	private float xAxis;
	//private float yAxis;

	
	
	public MyOritationListener(Context mContext) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
	}
	
	//��ʼ����
	@SuppressWarnings("deprecation")
	public void start() {
		//��÷��򴫸���
		mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
		
		if(mSensorManager != null) {
			mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		}
		
		if(mSensor != null) {
			mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
		}
	}
	//��������
	public void stop() {
		mSensorManager.unregisterListener(this);
	}
	@SuppressWarnings("deprecation")
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if(event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
			float x = event.values[SensorManager.DATA_X];
			if(Math.abs(x-xAxis) > 1.0) {
				if(mOnOritationListener!=null) {
					mOnOritationListener.onOritationChanged(x);
				}
			}
			xAxis = x; 
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}
	private OnOritationListener mOnOritationListener;
	public void setOnOritationListener(OnOritationListener mOnOritationListener) {
		this.mOnOritationListener = mOnOritationListener;
	}
	public interface OnOritationListener{
		void onOritationChanged(float x);
	}
}
