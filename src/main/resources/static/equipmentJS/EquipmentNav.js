class EquipmentNav{
    constructor(){
        this.folder = "equipmentFolderHideId";
        this.controlPane = "controlPaneHideId";
        this.centerBoard = "centerBoardId";
        this.dragClickBoard = "dragBoardId";
        this.sync = "syncId";
        this.isFolderShowing = true;
        this.isControlPaneShowing = true;
        this.isClickMode = true;
        this.self = null;
        this.theWindow = null;

    }
    setDragClickBoardToDrag(){
        document.getElementById( this.dragClickBoard ).innerHTML = "<span ><i class=\"fas fa-paw\"></i></span>";
    }
    setDragClickBoardToClick(){
        document.getElementById( this.dragClickBoard ).innerHTML = "<i class=\"fas fa-mouse-pointer\"></i>";
    }
    // fa-rotate-90
    //<i class="fas fa-sync-alt"></i>
    loadfromIsClickMode(){
        if( this.isClickMode == true ){
            this.setDragClickBoardToClick();
        }else{
            this.setDragClickBoardToDrag();
        }
    }
    setSynOneCircle(){
        //loadContent();
        //setTimeout(this.setSynOneCircleOne, 500);
        var pos=0;
        var id = setInterval(frame, 5);
        function frame() {
            if (pos == 360) {
                clearInterval(id);
            } else {
                pos++;
                // elem.style.top = pos + "px";
                // elem.style.left = pos + "px";
                //this.setSyncByAngle( pos*6 );
                //console.log("66");
                document.getElementById( "syncId" ).style.transform = "rotate("+pos+"deg)";
                //document.getElementById( "syncId" ).innerHTML = "<i class=\"fas fa-sync-alt fa-rotate-"+ pos*6+"\"></i>";
            }
        }
    }
    //controlPaneHideId
    setControlPaneHideIdToHide(){
        document.getElementById(this.controlPane).innerHTML = " control panel <i class=\"fas fa-eye-slash\"></i> ";
    }
    seControlPaneHideIdToDisplay(){
        document.getElementById(this.controlPane).innerHTML = " control panel <i class=\"far fa-eye\"></i> ";
    }
    setEquipmentFolderHideIdToHide(){
        document.getElementById(this.folder).innerHTML = " equipment panel <i class=\"fas fa-eye-slash\"></i> ";
    }
    setEquipmentFolderHideIdToDisplay(){
        document.getElementById(this.folder).innerHTML = " equipment panel <i class=\"far fa-eye\"></i> ";
    }

    handleSync(){
        var equipmentNav  =this.self;
        equipmentNav.setSynOneCircle();
    }

    handleDragClick(){
        var equipmentNav  =this.self;
        if( equipmentNav.isClickMode ==true ){
            equipmentNav.setDragClickBoardToDrag();
            equipmentNav.isClickMode = false;
        }else{
            equipmentNav.setDragClickBoardToClick();
            equipmentNav.isClickMode =true;
        }
    }

    handleControlPane(){
        var equipmentNav  =this.self;
        var theWindow = this.theWindow;
        if( equipmentNav.isControlPaneShowing == true ){
            // here we need change the data of window
            // if it's true
            // we need change to false and hide right side control panel
            // we need change the window data
            //theWindow = new TheWindow();
            equipmentNav.seControlPaneHideIdToDisplay();
            theWindow.equipmentOperationSectionWidth = 0;
            theWindow.update();
            document.getElementById("equipmentOperationSection").style.display = "none";
            // we need change the
            //
            equipmentNav.isControlPaneShowing = false;
        }else{
            theWindow.equipmentOperationSectionWidth = 380;
            theWindow.update();
            equipmentNav.setControlPaneHideIdToHide();
            document.getElementById("equipmentOperationSection").style.display = "";
            equipmentNav.isControlPaneShowing = true;
        }

    }

    handleFolder(){
        var equipmentNav  =this.self;

        if( equipmentNav.isFolderShowing == true ){
            // here we need change the data of window
            // if it's true
            // we need change to false and hide right side control panel
            // we need change the window data
            //theWindow = new TheWindow();
            equipmentNav.setEquipmentFolderHideIdToDisplay();
            // this.operationListWidth = 50;
            // this.equipmentListWidth = 200;
            //theWindow.equipmentOperationSectionWidth = 0;
            theWindow.operationListWidth =0;
            theWindow.equipmentListWidth =0;
            //document.getElementById("equipmentPanel").style.display = "none";
            document.getElementById("equipmentBody").style.display = "none";
            // we need change the
            //
            document.getElementById("equipmentPanel").style.marginLeft = "0px";
            //document.getElementById("equipmentBody").style.left = "0px";
            theWindow.update();
            equipmentNav.isFolderShowing = false;
        }else{
            //theWindow.equipmentOperationSectionWidth = 380;
            theWindow.operationListWidth =50;
            theWindow.equipmentListWidth =200;
            document.getElementById("equipmentPanel").style.marginLeft = "250px";
            equipmentNav.setEquipmentFolderHideIdToHide();
            //document.getElementById("equipmentPanel").style.display = "";
            document.getElementById("equipmentBody").style.display = "";
            equipmentNav.isFolderShowing = true;
            //document.getElementById("equipmentBody").style.position = "fixed";
            theWindow.update();
        }
    }

    handleCenterBoard(){

        var equipmentNav  =this.self;

        //

    }

    activeAllButtons(){
        var equipmentNav = this.self;
        //equipmentNav = new EquipmentNav();
        document.getElementById(equipmentNav.sync).onclick= function () {
            equipmentNav.handleSync();
        };
        document.getElementById( equipmentNav.dragClickBoard ).onclick = function () {
            equipmentNav.handleDragClick();
        };
        document.getElementById( equipmentNav.controlPane ).onclick = function () {
            equipmentNav.handleControlPane();
        };
        document.getElementById( equipmentNav.folder ).onclick = function () {
            equipmentNav.handleFolder();
        };
        document.getElementById( equipmentNav.centerBoard ).onclick =function () {
            equipmentNav.handleCenterBoard();
        }
    }

    activeShareFindModel(){
        var equipmentNav = this.self;
        document.getElementById(equipmentNav.sync).onclick= function () {
            equipmentNav.handleSync();
        };

        document.getElementById( equipmentNav.folder ).onclick = function () {
            equipmentNav.handleFolder();
        };

        equipmentNav.handleControlPane();

        // this.controlPane
        // this.isClickMode
        // this.dragClickBoard
        // this.centerBoard

        document.getElementById( equipmentNav.controlPane ).style.display = "none";
        document.getElementById( equipmentNav.dragClickBoard ).style.display = "none";
        document.getElementById( equipmentNav.centerBoard ).style.display = "none";


    }

    // setSynOneCircleOne( f ){
    //
    //     //this.setSyncByAngle( this.syncCount*30 );
    //     console.log("what");
    //
    //     this.syncCount = this.syncCount+1;
    //
    //     if(this.sync < 12) {
    //
    //         setTimeout(f  , 500);
    //     }
    // }
    //<i class="fas fa-eye-slash"></i>
    //<i class="far fa-eye"></i>
    // now we need
}

//_________________________________________________________

        // equipmentNav = new EquipmentNav();
        // equipmentNav.self = equipmentNav;
        // equipmentNav.activeAllButtons();      // create model
        // equipmentNav.activeShareFindModel();  // share and find model







