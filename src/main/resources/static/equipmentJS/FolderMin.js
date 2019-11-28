
/*!
  * Folder v4.1.1 (https://getbootstrap.com/)
  * Copyright 2011-2018 The Bootstrap Authors (https://github.com/twbs/bootstrap/graphs/contributors)
  * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
  */

class FolderMin{
    constructor(){
        // we don't need save value here
        this.folderMin = "minFolder";
        //this.manageFolderJumpAddress = "#123";
        this.folderNames = ["1", "2", "3"];
        this.folderIds = ["id1","id2", "id3"];
        this.self = null;
    }
    // we need create a folder by name
    // we need
    //sortable3
    setFolderNames( folderNames , folderIds ){
        this.folderNames = folderNames;
        this.folderIds = folderIds;
        this.__refresh();
    }
    setSelf( self ){
        this.self = self;
    }
    // those three function we need change the herf
    // so it can jump to some where
    // __getDivideLine(){
    //     //<div class='dropdown-divider'></div>
    //     //<div class="dropdown-divider"></div>
    //     return "<div class=\"dropdown-divider\"></div>";
    // }
    //
    // __getMangaeFolder(){
    //     //<a class="dropdown-item" href="#"><i class="fas fa-folder"></i>&nbsp;&nbsp;Mangae Folders</a>
    //     //<a class='dropdown-item' href='#'><i class='fas fa-folder'></i>&nbsp;&nbsp;Mangae Folders</a>
    //     return "<a class=\"dropdown-item\" href=\""+this.manageFolderJumpAddress+"\"><i class=\"fas fa-folder\"></i>&nbsp;&nbsp;Mangae Folders</a>";
    // }
    //
    //
    //
    // __getButtomHTML(){
    //     return this.getDivideLine()+this.getMangaeFolder();
    // }
    // getDynamicItem(){
    //     // <div class='input-group bg-light dropdown-item' style='border: 0px'>
    //     //         <input type='text' style='border: 0px' class='form-control bg-light text-dark text-truncate font-weight-normal' value='folder 1'>
    //     //         <div class='input-group-append bg-light' style='border: 0px'>
    //     //         <span class='input-group-text bg-light' style='border: 0px'><i class='fas fa-minus'></i></span>
    //     //     </div>
    //     //     </div>
    //
    //
    //     var ReturnData = " <div class='input-group bg-light dropdown-item' style='border: 0px'>\n" +
    //         "                <input type='text' style='border: 0px' class='form-control bg-light text-dark text-truncate font-weight-normal' value='folder 1'>\n" +
    //         "                <div class='input-group-append bg-light' style='border: 0px'>\n" +
    //         "                <span class='input-group-text bg-light' style='border: 0px'><i class='fas fa-minus'></i></span>\n" +
    //         "            </div>\n" +
    //         "            </div>";
    //
    //
    //
    //     return
    // }
    // we need register on click function
    // so this just call the same function
    __getStaticItem( folderName , folderId ){
        //<div class="dropdown-item"><p class="d-inline">Folder 2</div>
        //<div class='dropdown-item'><p class='d-inline'>Folder 2</div>
        var ClassName = "dropdown-item text-truncate";
        // var OnClick = "function () {   }"
        return "<div class='"+ClassName+"' id='"+folderId+"' >"+folderName+"</div>";
    }
    __getAllHTML(  ){
        var total = "";
        for( var i = 0 ; i < this.folderNames.length ; i++ ){
            //folderNames[i]
            total = total+this.__getStaticItem( this.folderNames[i] , this.folderIds[i] );
        }
        //total = total + this.getButtomHTML();
        return total;
    }
    __setHTML(){
        document.getElementById(this.folderMin).innerHTML = this.__getAllHTML();
    }
    __activeButton(){
        var self = this.self;
        document.getElementById(this.folderMin).onchange = function () {
            //console.log("print something first");
            // good it dose work
            //
            // the function don't change it self
            // instead it read all the data and send to back side
            // document.getElementById(folderMin.folderMin).childNodes.forEach(
            //
            //     function(item){
            //
            //         //console.log(item);
            //
            //         console.log(item);
            //
            //
            //     }
            //
            //
            // )
            //console.log(  document.getElementById(folderMin.folderMin).children[0].id );
            //console.log( document.getElementById(folderMin.folderMin).childNodes[0] );
            // so we need writ  a function get the id
            // so it will start the id with id=" or id='
            var folderNode =  document.getElementById(folderMin.folderMin).children;
            var folderIds = [];
            for( var i = 0 ; i < folderNode.length ; i++ ){
                folderIds.push( folderNode[i].id );
            }
            // we will send to message prepare
            console.log(folderIds);
        }
        for( var i = 0 ; i < this.folderIds.length ; i ++ ){
            document.getElementById(this.folderIds[i]).onclick = function () {
                console.log("666");
                //console.log(self.getDivideLine());
            }
        }
        //document.getElementById(this.folderMin).
    }
    __refresh(){
        this.__setHTML();
        this.__activeButton()
    }
}

//___________________________________________

    // the description below is how to use
            // this javascript class

/*

            folderMin = new FolderMin();
            folderMin.__refresh();

            there is a problem
                // it lack of icon
                // so we could now the icon
             we don't focus on this
                this time

            // so we will do this later

 */









