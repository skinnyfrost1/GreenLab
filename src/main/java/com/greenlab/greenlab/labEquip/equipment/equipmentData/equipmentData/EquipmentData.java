package com.greenlab.greenlab.labEquip.equipment.equipmentData.equipmentData;



import com.greenlab.greenlab.labEquip.equipment.equipmentData.polygonData.Dot;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.polygonData.PolygonData;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.polygonData.Position;
import com.greenlab.greenlab.labEquip.framework.userRoot.ItemData;

import java.util.LinkedList;


public class EquipmentData extends ItemData {


    private Position originalPos; // this dot use to controll the board

    private LinkedList<String> imageIds;

    private LinkedList<PolygonData> polygonData;

    private LinkedList<Dot>  reactDot;

    public EquipmentData(){
        imageIds = new LinkedList<>();
        polygonData = new LinkedList<>();
        reactDot = new LinkedList<>();
    }

    public LinkedList<Dot> getReactDot() {
        return reactDot;
    }

    public void setPolygonData(LinkedList<PolygonData> polygonData) {
        this.polygonData = polygonData;
    }

    public LinkedList<PolygonData> getPolygonData() {
        return polygonData;
    }

    public void setReactDot(LinkedList<Dot> reactDot) {
        this.reactDot = reactDot;
    }

    public void setOriginalPos(Position originalPos) {
        this.originalPos = originalPos;
    }

    public Position getOriginalPos() {
        return originalPos;
    }

    public LinkedList<String> getImageIds() {
        return imageIds;
    }

    public void setImageIds(LinkedList<String> imageIds) {
        this.imageIds = imageIds;
    }

}

