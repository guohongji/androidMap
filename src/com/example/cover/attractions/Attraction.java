package com.example.cover.attractions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Attraction implements Serializable{

    private static final long serialVersionUID = 6664085716634691496L;
    private double latitude;
    private double longitude;
    private String imagePath;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String name;
    private String distance;
    private int praise;
  //  public static List<Attraction> attractions= new ArrayList<Attraction>();




//    public Attraction(double latitude,double longitute,String imagePath,String name,String distance,int praise){
//        super();
//        this.latitude = latitude;
//        this.longitute = longitute;
//        this.imagePath = imagePath;
//        this.name = name;
//        this.distance = distance;
//        this.praise = praise;
//    }



    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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
