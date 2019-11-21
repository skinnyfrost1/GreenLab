package com.greenlab.greenlab.labEquip.equipment.equipmentData.polygonData;

import java.util.ArrayList;
import java.util.List;

public class PolygonData {


    private List<Dot> dots; //

    private String type;

    public PolygonData(){

        dots = new ArrayList<Dot>();
    }

    public List<Dot> getDots() {
        return dots;
    }

    public void setDots(List<Dot> dots) {
        this.dots = dots;
    }

    public int getNumberofDots(){

        return this.dots.size();

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLineRender(Position originalDot ){
        return "";
    }

    public String getPolygonRender(Position originalDot ){
        return "";
    }



}
