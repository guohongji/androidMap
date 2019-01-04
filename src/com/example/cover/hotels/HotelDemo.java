package com.example.cover.hotels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.example.debristravels.R;

import android.media.Image;

public class HotelDemo implements Serializable{
	/**
	 * 设置唯一的序列ID
	 *
	 */
	private static final long serialVersionUID = 7924085716634691495L;

	private double latitude;
	private double longitute;
	private int imageId;
	private String name;
	private String distance;
	private int praise;
	public static List<HotelDemo> hotels= new ArrayList<HotelDemo>();

	/*
	* latitude:纬度
	* longitude:经度
	* */
	public HotelDemo(double latitude, double longitute, int imageId, String name, String distance, int praise) {
		super();
		this.latitude = latitude;
		this.longitute = longitute;
		this.imageId = imageId;
		this.name = name;
		this.distance = distance;
		this.praise = praise;
	}
	static {
		//hotels.add(new HotelDemo(118.22385, 25.265138, R.drawable.a01, "英伦贵族小旅馆","距离209米", 1456));

		hotels.add(new HotelDemo(25.265224,118.224955, R.drawable.a01, "Android开发小队",
				"距离209米", 666));
		hotels.add(new HotelDemo(25.264113, 118.220894,R.drawable.a01, "Android开发小队",
				"距离209米", 666));
		hotels.add(new HotelDemo(25.264195,118.22323, R.drawable.a01, "Android开发小队",
				"距离209米", 666));
		hotels.add(new HotelDemo(25.268492, 118.225404,R.drawable.a01, "Android开发小队",
				"距离209米", 666));
	}



	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitute() {
		return longitute;
	}
	public void setLongitute(double longitute) {
		this.longitute = longitute;
	}
	public int getImageId() {
		return imageId;
	}
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public int getPraise() {
		return praise;
	}
	public void setPraise(int praise) {
		this.praise = praise;
	}

}
