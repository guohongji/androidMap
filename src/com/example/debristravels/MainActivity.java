package com.example.debristravels;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.example.cover.hotels.HotelDemo;

import com.example.volleydemo.MyApplication;
import com.example.volleydemo.initdata.AttractionData;
import com.gyf.barlibrary.ImmersionBar;

import android.R.bool;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements OnCheckedChangeListener{



	private RadioButton rButton;
	private RadioGroup group;
	
	private AttractionsFra attractions;
	private MeetFra meet;
	private TakePicFra picture; 
	
	private MyselfFra myself;
	
	private boolean isExit = false;

	public ImmersionBar mImmersionBar;

	private  String url = "http://shylockcc.frpgz1.idcfengye.com/hello/json";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(featureId);设置主题
		setContentView(R.layout.activity_main);
		mImmersionBar=super.mImmersionBar;

		//初始化底部导航栏
		group=(RadioGroup) findViewById(R.id.radiogroup);
		group.setOnCheckedChangeListener(this);
		group.check(R.id.fourth);

		PackageManager pm = getPackageManager();  
        boolean permission = (PackageManager.PERMISSION_GRANTED ==   
                pm.checkPermission("android.permission.ACCESS_COARSE_LOCATION", getPackageName()));  
        if (permission) {  
          
        }else {  
        	 Toast.makeText(this, "请开启定位权限"+getPackageName(), Toast.LENGTH_SHORT).show();
        }
        //加载景点数据
		//new AttractionData(this).getAttrationData(url);




	}

	public ImmersionBar getmImmersionBar(){
		return this.mImmersionBar;

	}



	/*
	 * 隐藏Fragment
	 * */
	public void hideAllFragment(FragmentTransaction transaction){
        if(attractions!=null){
            transaction.hide(attractions);
        }
        if(picture!=null){
            transaction.hide(picture);
        }
        if(meet!=null){
            transaction.hide(meet);
        }
        if(myself!=null){
            transaction.hide(myself);
        }
    }

	
	
/*
 * 底部四个复选框
 * */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		hideAllFragment(transaction);
		switch(checkedId) {
		case R.id.first:{
			if(attractions==null) {
				attractions = AttractionsFra.getInstance();
				transaction.add(R.id.frame, attractions);
			}else{
                transaction.show(attractions);
            }
			//attractions.addOverlays(HotelDemo.hotels);
			break;
		}
	case R.id.second:{
		if(meet==null) {
			meet = new MeetFra();
			transaction.add(R.id.frame, meet);
		}else{
            transaction.show(meet);
        }
			break;
		}
		
	case R.id.thrid:{
		if(picture==null) {
			picture = new TakePicFra();
			transaction.add(R.id.frame, picture);
		}else{
            transaction.show(picture);
        }
		
		break;
	}
	
	case R.id.fourth:{
		if(myself==null) {
			myself = new MyselfFra();
			transaction.add(R.id.frame, myself);
		}else{
            transaction.show(myself);
        }
		
		break;
	}
		
		
		}
		  transaction.commit();
	}




	
	
	//改写手机物理按键返回的逻辑
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK) {	
				exit();
				return false;
		}
			return super.onKeyDown(keyCode, event);
	}


	private void exit() {
		// TODO Auto-generated method stub
		if(!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(),"再按一次退出程序",Toast.LENGTH_SHORT).show();
			
			mHandler.sendEmptyMessageDelayed(0, 2000);
		}else {
			finish();
			System.exit(0);
		}
	}
	//消息传递
	Handler mHandler = new Handler() {
		@SuppressLint("HandlerLeak")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};
}
