package com.greenlab.greenlab.labEquip.equipment.equipmentData.polygonData;

public class Rectangle {

    private Position startDot;

    private Position endDot;

    public Rectangle(){

        startDot = new Position();
        endDot = new Position();

    }

    public Position getEndDot() {
        return endDot;
    }

    public Position getStartDot() {
        return startDot;
    }

    public void setEndDot(Position endDot) {
        this.endDot = endDot;
    }

    public void setStartDot(Position startDot) {
        this.startDot = startDot;
    }

}
