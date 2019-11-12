package com.greenlab.greenlab.equipment.equipmentData.equipmentData;

import green.lab.equipment.equipmentData.polygonData.Position;
import org.springframework.data.annotation.Id;

import java.util.LinkedList;
import java.util.List;

public class EquipmentData {

    @Id
    private String id;

    private String equipmentName;

    private String description;

    private Position originalPos; // this dot use to controll the board

    private List<String> imageIds;

    private List<String> polygonIds;

    public EquipmentData(){

        imageIds = new LinkedList<>();
        polygonIds = new LinkedList<>();


    }

    public void setOriginalPos(Position originalPos) {
        this.originalPos = originalPos;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public Position getOriginalPos() {
        return originalPos;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public List<String> getImageIds() {
        return imageIds;
    }

    public List<String> getPolygonIds() {
        return polygonIds;
    }

    public void setImageIds(List<String> imageIds) {
        this.imageIds = imageIds;
    }

    public void setPolygonIds(List<String> polygonIds) {
        this.polygonIds = polygonIds;
    }

}

