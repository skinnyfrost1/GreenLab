package com.greenlab.greenlab.equipment.equipmentData.ImageData;

import green.lab.equipment.equipmentData.polygonData.Position;
import org.springframework.data.annotation.Id;

public class ImageData {
    @Id
    private String id;

    private String name;

    private Position position; // this save relative position to original dot

    // this save the angle
    private int angle;

    private int size;

    private int originalWidth;

    private int OriginalHeight;

    private int index;

    private String imageUrl;
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    //____________________________________________________

    public String render(){


        return "";
    } // render function is to get the formation



    //____________________________________________________
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
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
    public int getOriginalHeight() {
        return OriginalHeight;
    }
    public int getOriginalWidth() {
        return originalWidth;
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
    public void setOriginalHeight(int originalHeight) {
        OriginalHeight = originalHeight;
    }
    public void setOriginalWidth(int originalWidth) {
        this.originalWidth = originalWidth;
    }
    public void setSize(int size) {
        this.size = size;
    }

}
