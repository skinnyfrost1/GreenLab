

class Box{
    constructor( elementId ){
        this.elementId = elementId;
        this.width = 0;
        this.height = 0;
    }
    setWidth( width ){
        this.width = width;
        document.getElementById(this.elementId).style.width = this.width+"px";
    }
    setHeight( height ){
        this.height = height;
        document.getElementById(this.elementId).style.height = this.height+"px";
    }
    setLeft( left ){
        document.getElementById(this.elementId).style.left = left+"px";
    }
    setRight( right ){
        document.getElementById(this.elementId).style.right=right+"px";
    }
    getWidth(){
        return this.width;
    }
    getHeight(){
        return this.height;
    }
}
class TheWindow{
    constructor(){
        this.navbarHeight = 81;
        this.operationListWidth = 50;
        this.equipmentListWidth = 200;
        this.equipmentOperationSectionWidth = 380;
        this.operationList = new Box( "operationList" );
        this.operationList.setWidth( this.operationListWidth );
        this.equipmentList = new Box( "equipmentList" );
        this.equipmentList.setWidth(this.equipmentListWidth);
        this.equipmentList.setLeft(this.operationListWidth);
        this.equipmentPanel = new Box( "equipmentPanel" );
        this.equipmentPanel.setLeft(this.operationListWidth+ this.equipmentListWidth );
        this.equipmentOperationSection = new Box( "equipmentOperationSection" );
        this.equipmentOperationSection.setWidth(this.equipmentOperationSectionWidth);
        this.equipmentOperationSection.setRight( 0 );
         this.update();
        //operationList
        //equipmentList
        //equipmentPanel
        //equipmentOperationSection
    }
    collectBox(){
        return [
            this.operationList,
            this.equipmentList,
            this.equipmentPanel,
            this.equipmentOperationSection
        ];
    }
    update(){
        this.setAllHeight();
        this.setEquipmentPanelWidth();
    }
    setAllHeight(){
        for( var i = 0 ;  i< this.collectBox().length ; i++ ){
            this.collectBox()[i].setHeight( this.getRestHeight() );
        }
        // here we set svg board
        //document.getElementById("SVGBoard").style.height = (this.getRestHeight()-10)+"px";
    }
    setEquipmentPanelWidth(){
        this.equipmentPanel.setWidth( this.getRestWidth() );
        //document.getElementById("SVGBoard").style.width = (this.getRestWidth()-10)+"px";
    }
    getWindowWidth(){
        return window.innerWidth;
    }
    getWindowHeight(){
        return window.innerHeight;
    }
    getRestHeight(){
        return this.getWindowHeight()-this.navbarHeight;
    }
    getRestWidth(){
        return this.getWindowWidth()-(this.operationListWidth+ this.equipmentListWidth+this.equipmentOperationSectionWidth);
    }



    // one function to set model
    // so we will first set

}

//------------------- how to use this ---------------------

        //theWindow = new TheWindow();


