package com.greenlab.greenlab.equipment.equipmentData.polygonData;

public class Position {

    private int x;
    private int y;

    public Position(){

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setXY( int x, int y ){
        this.x = x;
        this.y = y;
    }

    public void setXY( int[] IntArr ){
        this.x = IntArr[0];
        this.y = IntArr[1];
    }

    public int[] getXY(){
        int IntArr[] = new int[2];
        IntArr[0] = this.x;
        IntArr[1]= this.y;
        return IntArr;
    }

//    @Override
//    public String toString() {
//        return "x : "+this.x+" y : "+this.y;
//    }

    public boolean compareWith( Position position ){
        if( this.x == position.getX()  ){
            if( this.y == position.getY() ){
                return true;
            }
        }
        return false;
    }

    public Position getRelativePosition( Position position ){
        // we assume orginal samaller
        // compare is larger
        Position newPosition = new Position();
        newPosition.setX( position.getX()-this.x );
        newPosition.setY( position.getY()-this.y );
        return newPosition;
    }

}
