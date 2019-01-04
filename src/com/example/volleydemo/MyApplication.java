package com.example.volleydemo;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

public class MyApplication extends Application {
	public static RequestQueue queues;
	public void onCreate() {
		super.onCreate();
		queues = Volley.newRequestQueue(getApplicationContext());
		//����ȫ�ֵ�Header������
		SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
			@NonNull
			@Override
			public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
				//ָ��Ϊ����Header��Ĭ���� �������״�Header
				return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);
			}
		});
		//����ȫ�ֵ�Footer������
		SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
			@NonNull
			@Override
			public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
				//ָ��Ϊ����Footer��Ĭ���� BallPulseFooter
				return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
			}
		});
	}

	public static RequestQueue getHttpQueues() {
		return queues;
	}



}
