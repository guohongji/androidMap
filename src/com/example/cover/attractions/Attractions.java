package com.example.cover.attractions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/9 0009.
 */

public class Attractions {
    private List<Attraction> attractionList = new ArrayList<Attraction>();
private static Attractions attractions = null;
    private Attractions(){
    }
    public static Attractions getAttractions(){
        if(attractions ==null){
            attractions = new Attractions();
        }
        return attractions;
    }


    public List<Attraction> getAttractionList() {
        return attractionList;
    }

    public void setAttractionList(List<Attraction> attractionList) {
        this.attractionList = attractionList;
    }
}
