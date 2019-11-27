

class Main{

    constructor(){
        this.theWindow = null;
        this.eOSection = null;
        this.equipmentNav = null;
        this.self = null

    }

    activeWindow(){

        var main = this.self;
        main.theWindow = new TheWindow();



    }

    activePad(){

        var main = this.self;
        main.eOSection = new EquipmentOperationSection();
        main.eOSection.activeAllButton( main.eOSection );
        main.eOSection.setIndexActive(0);

    }

    activeEquipmentNav(){
        var main = this.self;

        main.equipmentNav = new EquipmentNav();
        main.equipmentNav.theWindow = main.theWindow;
        main.equipmentNav.self = main.equipmentNav;
        main.equipmentNav.activeAllButtons();

    }

    activeAll(){
        var main = this.self;
        main.activeWindow();
        main.activePad();
        main.activeEquipmentNav();
        //main.equipmentNav.handleControlPane();
        //main.equipmentNav.handleControlPane();


    }

    activeFind(){
        var main = this.self;
        main.activeWindow();
        main.activePad();
        main.activeEquipmentNav();
        //main.equipmentNav.
        //main.equipmentNav.handleControlPane();
    }

}