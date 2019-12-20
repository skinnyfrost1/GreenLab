package com.greenlab.greenlab.labEquip.equipment.equipmentData.ImageData;

import com.greenlab.greenlab.labEquip.equipment.equipmentData.polygonData.Position;
import org.springframework.data.annotation.Id;

import java.util.LinkedList;

public class ImageData {
    @Id
    private String id;

    private String name = "new image";

    private Position position; // this save relative position to original dot

    // this save the angle
    //private int angle = 10;   // 180 / 10   so each section should be 18 degree

    //private int size = 10;     // each time add 5 pixe or decrease by 5 pixal

    private String blobId;

    private LinkedList<String> sendText;

    private LinkedList<String> receiveText;

   // private LinkedList<String>

    //private LinkedList<Dot>  dots;

    //private LinkedList<Rectangle> rectangles;

    public ImageData(){
        position  =new Position();
        sendText= new LinkedList<>();
        receiveText = new LinkedList<>();
        //dots = new LinkedList<>();
        //rectangles = new LinkedList<>();
    }

    public LinkedList<String> getReceiveText() {
        return receiveText;
    }

    public LinkedList<String> getSendText() {
        return sendText;
    }

    public void setReceiveText(LinkedList<String> receiveText) {
        this.receiveText = receiveText;
    }

    public void setSendText(LinkedList<String> sendText) {
        this.sendText = sendText;
    }

    //    public LinkedList<Dot> getDots() {
//        return dots;
//    }
//
//    public void setDots(LinkedList<Dot> dots) {
//        this.dots = dots;
//    }
//
//    public LinkedList<Rectangle> getRectangles() {
//        return rectangles;
//    }
//
//    public void setRectangles(LinkedList<Rectangle> rectangles) {
//        this.rectangles = rectangles;
//    }

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
//    public int getAngle() {
//        return angle;
//    }

    public String getBlobId() {
        return blobId;
    }
    public void setBlobId(String blobId) {
        this.blobId = blobId;
    }
//    public int getSize() {
//        return size;
//    }
    public Position getPosition() {
        return position;
    }
//    public void setAngle(int angle) {
//        this.angle = angle;
//    }
//
//    public void setSize(int size) {
//        this.size = size;
//    }



}
