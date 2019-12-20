package com.greenlab.greenlab.labEquip.equipment.equipmentData.polygonData;

public class Dot {

    private Position position; // this is relative position to origianl dot



    public Dot(){

        this.position = new Position();
    }

    public String getMouseRender( Position originalDot ){

        // we need save a simple to here
        //<circle cx="500" cy="500" r="4" stroke="black" stroke-width="1" fill="red"  />
        //return "";

        Position realPos = originalDot.getRelativePosition(this.position);

        return "<circle cx=\""+realPos.getX()+"\" cy=\""+realPos.getY()+"\" r=\"4\" stroke=\"black\" stroke-width=\"1\" fill=\"red\"  />";

    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }




}
