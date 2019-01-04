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


	//��ʼ�����
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	Button resetLoc;
	private ImageView praiseImg;
	//��λ���
	private LocationClient mLocationClient;//λ�ü����߳�,�ٶ��ṩ
	private MyLocationListener mLocationListener;//�ڲ���
	/*��ǰ�Ķ�λģʽ*/
	private LocationMode mCurrentMode = LocationMode.NORMAL;
	/*�Զ��嵼��ͼ��*/
	private BitmapDescriptor mNaviIcon;
	/*����ı�*/
	private MyOritationListener mMyOritationListener;
	/*��¼��ǰλ��*/
	private float mCurrentX;
	// LocationData locData = null;
	private boolean isFirstLoc = true;
	private double mLatitude;
	private double mLongitude;
	//���������
	private BitmapDescriptor mMarker;
	private RelativeLayout showHotelInfo;
	//��ʼ���㵥��
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

		//��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext  
		//ע��÷���Ҫ��setContentView����֮ǰʵ��
		SDKInitializer.initialize(getActivity().getApplicationContext());
	    /*
		 * resource:Fragment��Ҫ���صĲ����ļ�
		 * root:����layout�ĸ�ViewGroup
		 * attrachToRoot:false,������ViewGroup
		 * */
		View view = inflater.inflate(R.layout.attractions_map, container, false);
		//��ʼ����ͼ
		mMapView = (MapView) view.findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();

		//��ʼ���ٶȵ�ͼ������
		initLocation();

		//��ʼ��������
		initMarker();

		//��ʼ�����ּ���
		initLayoutListener(view);

		//��ʼ�����������
		initOverlaysListener(view);

		//��ʼ����ͼ�ļ���
		initMapListener(view);


		return view;
	}

	//��ʼ����ͼ�ļ���
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

	//��ʼ��������ļ���
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

			//��ʾ����ľ�����Ϣ
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
					distance.setText("������:"+distan/1000+"ǧ��");
				else
					distance.setText("������:"+distan/1000.0+"��");


				/*  ����Volley����������ʾͼƬ    */
				RequestQueue queue = MyApplication.getHttpQueues();
				ImageLoader imageLoader = new ImageLoader(queue,new BitmapCache());
				//����һ��Listener���ڼ����ص�
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


	//��ʼ�����ֿؼ��ļ���
	private void initLayoutListener(View view) {
		//��ʼ���ؼ�,�ض�λbutton��������
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
	 * ��ʼ��������ͼ��
	 * 
	 * */
	private void initMarker() {
		// TODO Auto-generated method stub
		mMarker = BitmapDescriptorFactory.fromResource(R.drawable.maker);

	}
	/*
	 * ��ӾƵ긲����
	 * */
	public void addHotelOverlays(List<HotelDemo> hotels) {
		// TODO Auto-generated method stub
		mBaiduMap.clear();
		LatLng latLng = null;
		Marker marker = null;
		OverlayOptions options;
		for(HotelDemo hotel:hotels) {
			//��ȡ�Ƶ�ľ�γ��
			latLng = new LatLng(hotel.getLatitude(), hotel.getLongitute());
			/*
			 * position:��־��λ��
			 * icon:��־��ͼ��
			 * zIndex:��ͼ�ڼ���֮��ʼ��ʾ
			 * */
			options = new MarkerOptions().position(latLng).icon(mMarker).zIndex(5);
			marker = (Marker) mBaiduMap.addOverlay(options);
			//���marker���¼��������뽫hotel��ӵ�marker��������
			Bundle arg0 = new Bundle();
			arg0.putSerializable("hotel", hotel);
			//��Ҫ����һ��bundle���ݰ�
			marker.setExtraInfo(arg0);
		}
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.animateMapStatus(msu);
	}
	/*
	* ��Ӿ��㸲����
	* */
public void addAttractionOverlays(List<Attraction> attractionList) {
	mBaiduMap.clear();
	Toast.makeText(getActivity(),"������:"+ attractionList.size()+"", Toast.LENGTH_SHORT).show();
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
			 * position:��־��λ��
			 * icon:��־��ͼ��
			 * zIndex:��ͼ�ڼ���֮��ʼ��ʾ
			 * */
			options = new MarkerOptions().position(latLng).icon(mMarker).zIndex(5);
			marker = (Marker) mBaiduMap.addOverlay(options);
			//���marker���¼��������뽫hotel��ӵ�marker��������
			Bundle arg0 = new Bundle();
			arg0.putSerializable("attraction", attraction);
			//��Ҫ����һ��bundle���ݰ�
			marker.setExtraInfo(arg0);
		}
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.animateMapStatus(msu);
	}
}


	/*
	 * ��λ���ҵ�λ��
	 * */
	private void centerToMyLocation() {
		LatLng  latLng = new LatLng(mLatitude, mLongitude);
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.animateMapStatus(msu);
	}

	/*��ʼ���ٶȵ�ͼ������*/
	private void initLocation() {

		mMapView.showZoomControls(false);
		// TODO Auto-generated method stub
		mLocationClient = new LocationClient(getActivity());
		mLocationListener = new MyLocationListener();
		// TODO ����������ӵ�Client
		mLocationClient.registerLocationListener(mLocationListener);

		//���õ�ͼ��ѡ��
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// ��gps
		option.setCoorType("bd09ll"); // ������������
		option.setIsNeedAddress(true);
		option.setScanSpan(1000);
		mLocationClient.setLocOption(option);

		//��ʼ���ٶȵ�ͼ�ĵ���ͼ��
		mNaviIcon = BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked);
		//��ʼ���������---�Զ���
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
		//��activityִ��onDestroyʱִ��mMapView.onDestroy()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onDestroy();
	}
	@Override
	public void onResume() {
		super.onResume();
		//��activityִ��onResumeʱִ��mMapView. onResume ()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onResume();
	}
	@Override
	public void onPause() {
		super.onPause();
		//��activityִ��onPauseʱִ��mMapView. onPause ()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onPause();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//������λ����
		mBaiduMap.setMyLocationEnabled(true);
		if(!mLocationClient.isStarted())
			mLocationClient.start();
		//�������򴫸�������
		mMyOritationListener.start();
		//���hotel
		//addHotelOverlays(HotelDemo.hotels);
		//���attraction
		AttractionData attractionData = new AttractionData(getActivity());
		attractionData.getAttrationData(dataUrl);

		//���ӿ�ͨ����������:
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
		//ֹͣ��λ
		mBaiduMap.setMyLocationEnabled(false);
		mLocationClient.stop();
		//ֹͣ���򴫸�������
		mMyOritationListener.stop();
	}
	/*
	 * ����һ������������λ�õĸı�
	 * */
	private class MyLocationListener implements BDLocationListener{
		//��λ�ɹ���Ļص�
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
			/*���øı�ͼ��*/
			MyLocationConfiguration configuration = new MyLocationConfiguration(mCurrentMode, true,mNaviIcon);
			mBaiduMap.setMyLocationConfiguration(configuration);

			//�����״ζ�λ���Լ���λ��
			mLatitude = location.getLatitude();
			mLongitude = location.getLongitude();
			if(isFirstLoc) {
				//���þ�γ��
				LatLng  latLng = new LatLng(mLatitude, mLongitude);
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
				mBaiduMap.animateMapStatus(msu);
				isFirstLoc = false;
			}
		}

	}
	//����
}
