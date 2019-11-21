package com.greenlab.greenlab.labEquip.equipment.equipmentData.ImageData;

import com.greenlab.greenlab.labEquip.equipment.equipmentData.polygonData.Position;
import org.springframework.data.annotation.Id;

public class ImageData {
    @Id
    private String id;

    private String name;

    private Position position; // this save relative position to original dot

    // this save the angle
    private int angle;

    private int size;

    private String blobId;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setPosition(Position position) {
        this.position = position;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public int getAngle() {
        return angle;
    }

    public String getBlobId() {
        return blobId;
    }
    public void setBlobId(String blobId) {
        this.blobId = blobId;
    }
    public int getSize() {
        return size;
    }
    public Position getPosition() {
        return position;
    }
    public void setAngle(int angle) {
        this.angle = angle;
    }

    public void setSize(int size) {
        this.size = size;
    }







}
