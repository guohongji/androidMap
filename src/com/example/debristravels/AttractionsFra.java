package com.example.debristravels;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.cache.iamgecache.BitmapCache;
import com.example.cover.attractions.Attraction;
import com.example.cover.attractions.Attractions;
import com.example.cover.hotels.HotelDemo;
import com.example.sensor.MyOritationListener;
import com.example.sensor.MyOritationListener.OnOritationListener;
import com.example.volleydemo.MyApplication;
import com.example.volleydemo.initdata.AttractionData;

import android.app.Activity;
import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AttractionsFra extends Fragment{



	private static AttractionsFra attractionsFra;
	private boolean isFirstTemp = true;


	//初始化相关
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	Button resetLoc;
	private ImageView praiseImg;
	//定位相关
	private LocationClient mLocationClient;//位置监听线程,百度提供
	private MyLocationListener mLocationListener;//内部类
	/*当前的定位模式*/
	private LocationMode mCurrentMode = LocationMode.NORMAL;
	/*自定义导航图标*/
	private BitmapDescriptor mNaviIcon;
	/*方向改变*/
	private MyOritationListener mMyOritationListener;
	/*记录当前位置*/
	private float mCurrentX;
	// LocationData locData = null;
	private boolean isFirstLoc = true;
	private double mLatitude;
	private double mLongitude;
	//覆盖物相关
	private BitmapDescriptor mMarker;
	private RelativeLayout showHotelInfo;
	//初始景点单例
	Attractions attractions = Attractions.getAttractions();
	private  String dataUrl = "http://shylockcc.frpgz1.idcfengye.com/hello/json";
	private String imageResourceUrl = "http://shylockcc.frpgz1.idcfengye.com/hello/getImage/";

	public AttractionsFra() {
		// TODO Auto-generated constructor stub

	}
	public static AttractionsFra getInstance(){
		if(attractionsFra==null){
			attractionsFra=new AttractionsFra();

		}
		return attractionsFra;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		//在使用SDK各组件之前初始化context信息，传入ApplicationContext  
		//注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getActivity().getApplicationContext());
	    /*
		 * resource:Fragment需要加载的布局文件
		 * root:加载layout的父ViewGroup
		 * attrachToRoot:false,不返回ViewGroup
		 * */
		View view = inflater.inflate(R.layout.attractions_map, container, false);
		//初始化地图
		mMapView = (MapView) view.findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();

		//初始化百度地图的配置
		initLocation();

		//初始化覆盖物
		initMarker();

		//初始化布局监听
		initLayoutListener(view);

		//初始化覆盖物监听
		initOverlaysListener(view);

		//初始化地图的监听
		initMapListener(view);


		return view;
	}

	//初始化地图的监听
	private void initMapListener(View view) {
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				// TODO Auto-generated method stub
				showHotelInfo.setVisibility(View.GONE);
			}
		});
	}

	//初始化覆盖物的监听
	private void initOverlaysListener(View view) {
		showHotelInfo = (RelativeLayout) view.findViewById(R.id.show_hotel_info);
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				// TODO Auto-generated method stub
				//showHotel(marker);
				showAttration(marker);
				return true;
			}

			//显示具体的景点信息
			private void showAttration(Marker marker) {
				Bundle extraInfo = marker.getExtraInfo();
				Attraction attraction = (Attraction) extraInfo.getSerializable("attraction");
				ImageView iView = (ImageView) showHotelInfo.findViewById(R.id.hotel_image);
				TextView distance = (TextView) showHotelInfo.findViewById(R.id.hotel_distance);
				TextView name = (TextView) showHotelInfo.findViewById(R.id.hotel_name);
				TextView nPraise = (TextView) showHotelInfo.findViewById(R.id.num_hotel_praise);
				String imageUrl = imageResourceUrl+attraction.getId();
				int distan = (int) (111000*Math.sqrt(Math.pow(Math.abs(attraction.getLatitude()-mLatitude),2)+Math.pow(Math.abs(attraction.getLongitude()-mLongitude),2)));
				if(distan > 1000)
					distance.setText("距离大概:"+distan/1000+"千米");
				else
					distance.setText("距离大概:"+distan/1000.0+"米");


				/*  加载Volley队列用于显示图片    */
				RequestQueue queue = MyApplication.getHttpQueues();
				ImageLoader imageLoader = new ImageLoader(queue,new BitmapCache());
				//创建一个Listener用于监听回调
				ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(iView,R.drawable.a01,R.drawable.a01);
				imageLoader.get(imageUrl,imageListener);

				//	iView.setImageResource(attraction.getImageId());
				///	distance.setText(attraction.getDistance());
				name.setText(attraction.getName());
				nPraise.setText(attraction.getPraise()+"");
				showHotelInfo.setVisibility(View.VISIBLE);
			}

			private void showHotel(Marker marker) {
				Bundle extraInfo = marker.getExtraInfo();
				HotelDemo hotel = (HotelDemo) extraInfo.getSerializable("hotel");
				ImageView iView = (ImageView) showHotelInfo.findViewById(R.id.hotel_image);
				TextView distance = (TextView) showHotelInfo.findViewById(R.id.hotel_distance);
				TextView name = (TextView) showHotelInfo.findViewById(R.id.hotel_name);
				TextView nPraise = (TextView) showHotelInfo.findViewById(R.id.num_hotel_praise);

				iView.setImageResource(hotel.getImageId());
				distance.setText(hotel.getDistance());
				name.setText(hotel.getName());
				nPraise.setText(hotel.getPraise()+"");


				showHotelInfo.setVisibility(View.VISIBLE);
			}
		});
	}


	//初始化布局控件的监听
	private void initLayoutListener(View view) {
		//初始化控件,重定位button及监听器
		resetLoc = (Button) view.findViewById(R.id.resetLoc);
		resetLoc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				centerToMyLocation();

			}
		});





	}

	/*
	 * 初始化覆盖物图标
	 * 
	 * */
	private void initMarker() {
		// TODO Auto-generated method stub
		mMarker = BitmapDescriptorFactory.fromResource(R.drawable.maker);

	}
	/*
	 * 添加酒店覆盖物
	 * */
	public void addHotelOverlays(List<HotelDemo> hotels) {
		// TODO Auto-generated method stub
		mBaiduMap.clear();
		LatLng latLng = null;
		Marker marker = null;
		OverlayOptions options;
		for(HotelDemo hotel:hotels) {
			//获取酒店的经纬度
			latLng = new LatLng(hotel.getLatitude(), hotel.getLongitute());
			/*
			 * position:标志的位置
			 * icon:标志的图案
			 * zIndex:地图第几层之后开始显示
			 * */
			options = new MarkerOptions().position(latLng).icon(mMarker).zIndex(5);
			marker = (Marker) mBaiduMap.addOverlay(options);
			//点击marker有事件发生必须将hotel添加到marker的序列中
			Bundle arg0 = new Bundle();
			arg0.putSerializable("hotel", hotel);
			//需要的是一个bundle数据包
			marker.setExtraInfo(arg0);
		}
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.animateMapStatus(msu);
	}
	/*
	* 添加景点覆盖物
	* */
public void addAttractionOverlays(List<Attraction> attractionList) {
	mBaiduMap.clear();
	Toast.makeText(getActivity(),"长度是:"+ attractionList.size()+"", Toast.LENGTH_SHORT).show();
	LatLng latLng = null;
	Marker marker = null;
	OverlayOptions options;
	if (attractionList!=null) {
		for (Attraction attraction : attractionList) {
		//	Toast.makeText(getActivity(),a, Toast.LENGTH_LONG).show();
			latLng = new LatLng(attraction.getLatitude(), attraction.getLongitude());
			Log.i("getLatitude",attraction.getLatitude()+"");
			Log.i("getLongitute",attraction.getLongitude()+"");
			/*
			 * position:标志的位置
			 * icon:标志的图案
			 * zIndex:地图第几层之后开始显示
			 * */
			options = new MarkerOptions().position(latLng).icon(mMarker).zIndex(5);
			marker = (Marker) mBaiduMap.addOverlay(options);
			//点击marker有事件发生必须将hotel添加到marker的序列中
			Bundle arg0 = new Bundle();
			arg0.putSerializable("attraction", attraction);
			//需要的是一个bundle数据包
			marker.setExtraInfo(arg0);
		}
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.animateMapStatus(msu);
	}
}


	/*
	 * 定位到我的位置
	 * */
	private void centerToMyLocation() {
		LatLng  latLng = new LatLng(mLatitude, mLongitude);
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.animateMapStatus(msu);
	}

	/*初始化百度地图的配置*/
	private void initLocation() {

		mMapView.showZoomControls(false);
		// TODO Auto-generated method stub
		mLocationClient = new LocationClient(getActivity());
		mLocationListener = new MyLocationListener();
		// TODO 将监听器添加到Client
		mLocationClient.registerLocationListener(mLocationListener);

		//设置地图的选项
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setIsNeedAddress(true);
		option.setScanSpan(1000);
		mLocationClient.setLocOption(option);

		//初始化百度地图的导航图标
		mNaviIcon = BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked);
		//初始化方向监听---自定义
		mMyOritationListener = new MyOritationListener(this.getActivity());
		mMyOritationListener.setOnOritationListener(new OnOritationListener() {
			@Override
			public void onOritationChanged(float x) {
				// TODO Auto-generated method stub
				mCurrentX = x;
			}
		});

	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		//在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
	}
	@Override
	public void onResume() {
		super.onResume();
		//在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}
	@Override
	public void onPause() {
		super.onPause();
		//在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//开启定位允许
		mBaiduMap.setMyLocationEnabled(true);
		if(!mLocationClient.isStarted())
			mLocationClient.start();
		//开启方向传感器监听
		mMyOritationListener.start();
		//添加hotel
		//addHotelOverlays(HotelDemo.hotels);
		//添加attraction
		AttractionData attractionData = new AttractionData(getActivity());
		attractionData.getAttrationData(dataUrl);

		//将接口通过函数传入:
		attractionData.setVolleyCallBack(new AttractionData.VolleyCallBack() {
			@Override
			public void getAttractions(Attractions attractions) {
				addAttractionOverlays(attractions.getAttractionList());
			}
		});
	   // addAttractionOverlays();

	}
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		//停止定位
		mBaiduMap.setMyLocationEnabled(false);
		mLocationClient.stop();
		//停止方向传感器监听
		mMyOritationListener.stop();
	}
	/*
	 * 设置一个监听器监听位置的改变
	 * */
	private class MyLocationListener implements BDLocationListener{
		//定位成功后的回调
		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			MyLocationData myLocationData = new MyLocationData.Builder()
					.direction(mCurrentX)
					.accuracy(location.getRadius())
					.latitude(location.getLatitude())
					.longitude(location.getLongitude())
					.build();
			mBaiduMap.setMyLocationData(myLocationData);	
			/*启用改变图标*/
			MyLocationConfiguration configuration = new MyLocationConfiguration(mCurrentMode, true,mNaviIcon);
			mBaiduMap.setMyLocationConfiguration(configuration);

			//设置首次定位到自己的位置
			mLatitude = location.getLatitude();
			mLongitude = location.getLongitude();
			if(isFirstLoc) {
				//设置经纬度
				LatLng  latLng = new LatLng(mLatitude, mLongitude);
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
				mBaiduMap.animateMapStatus(msu);
				isFirstLoc = false;
			}
		}

	}
	//结束
}
