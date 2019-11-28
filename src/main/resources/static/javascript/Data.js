







class EquipmentData{

    constructor(){

        // here will have all the data

        // it will contain some

        this.UserEquipment = "";
        this.UserEquipmentFolders = {};
        this.EquipmentData ={};
        this.ImageBlob = {};

        this.currentEquipmentData = "";
        this.ImageData = {};

        this.currentFolder = "all";
        //this.currentImageDataId = "5dd8558e403e78018e8b8cec";
            // this data will be saved

    }

    displayCurrentImageData(){

        // here we do all the display

        if( equipmentData.currentEquipmentData.currentImageIds != "" ){

            var imageData = this.ImageData[ this.currentEquipmentData.currentImageIds ];

            if( imageData != null ){

                // editOp.setAngleValue( imageData.angle );
                // editOp.setSizeValue( imageData.size );
                editOp.setStatusName( imageData.name );
                editOp.setXposition( imageData.position.x );
                editOp.setYPosition( imageData.position.y );
                editOp.setReceiveText( imageData.receiveText[0] );
                editOp.setSendtext( imageData.sendText[0] );

                // getReceiveText(){
                //     return document.getElementById(this.receiveText).value;
                // }
                //
                // setReceiveText( value ){
                //     document.getElementById(this.receiveText).value = value;
                // }
                //
                // getSendText(){
                //     return document.getElementById( this.sendText ).value;
                // }
                //
                // setSendtext( value ){
                //     document.getElementById( this.sendText ).value = value;
                // }
            }


            //setDropDown( imageDataIds, imageDataNames )

            //


        }else{
            editOp.setAngleValue( 0 );
            editOp.setSizeValue( 0 );
            editOp.setStatusName( "select or upload to start" );
            editOp.setXposition( 0 );
            editOp.setYPosition( 0 );

        }


        var imageDataIds = this.currentEquipmentData.imageIds;

        var imageDataNames = [];
        for( var i = 0 ; i< imageDataIds.length ; i++ ){
            var name = this.ImageData[ imageDataIds[i] ]["name"];
            imageDataNames.push(name);
        }

        if( imageDataNames.length != 0 ){


            editOp.setDropDown(  imageDataIds, imageDataNames );

        }else{

            editOp.setDropDownHTML("");

        }



        // editOp.setAngleValue()
        // editOp.setSizeValue()
        // editOp.setStatusName()
        // editOp.setXposition()
        // editOp.setYPosition()
        // imageData.angle
        // imageData.size
        // imageData.name
        // imageData.position.x



    }

    displayFolder(){
        if( this.currentFolder == "all" ){

            this.__displayAll();
        }else if( this.currentFolder == "share" ){
            this.__displayShare();
        }else if( this.currentFolder == "favourite" ){
            this.__displayfavourite();
        }else if( this.currentFolder == "recent" ){
            this.__displayRecent();
        }

    }


    __displayAll(  ){
        this.__displayFolderByType( "all" );
    }

    __displayShare(){
        this.__displayFolderByType( "share" );
    }

    __displayfavourite(){
        this.__displayFolderByType( "favourite" );
    }

    __displayRecent(){

        //console.log("thi is ercent");

        this.__displayFolderByType( "recent" );
    }

    displayEquipment(){

        var name = this.currentEquipmentData.name;
        var description = this.currentEquipmentData.description;
        var currentImageId = this.currentEquipmentData.coverBlobId;

        if( currentImageId != "" ){

            var imageblob = localStorage.getItem( currentImageId );
            var imageData = JSON.parse(imageblob).blob;

            //imageOP = new ImageOp();
            imageOP.setCoverImage( imageData );
        }

        imageOP.setNameValue( name );
        imageOP.setTextArea( description );


        //this.currentEquipmentData.favourite
        //console.log("waht happened?");
        //console.log( this.currentEquipmentData.shared );
        //console.log( this.currentEquipmentData.favourite );

        setOp.setShareBaseBoolean( this.currentEquipmentData.shared );
        setOp.setLikeBaseBoolean( this.currentEquipmentData.favourite );

    }

    __displayFolderByType( type ){


        var cursor;
        var keys = Object.keys(this.UserEquipmentFolders);
        for( var i = 0 ; i < keys.length ; i++ ){
            cursor = this.UserEquipmentFolders[keys[i]];
            if( cursor["type"] == type ){


                break;
            }
        }


        if( cursor["itemIdsInFolder"] != null ){
            var cards = new Cards();
            for( var i = 0  ; i <  cursor["itemIdsInFolder"].length  ; i++ ){
                var equipmentDataId =  cursor["itemIdsInFolder"][i];
                var equipmentData = this.EquipmentData[ equipmentDataId ];
                //localStorage.getItem(equipmentData.coverBlobId)
                var imageData;
                //console.log(equipmentData.coverBlobId);
                if( equipmentData.coverBlobId == "" ){
                    imageData = "data:image/png;base64,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";
                }else{
                    var imageblob = localStorage.getItem(equipmentData.coverBlobId);
                    imageData = JSON.parse(imageblob).blob;
                }
                cards.addCard( imageData, equipmentData.name , equipmentData.description , equipmentData.id );
            }
            cards.setHTML();

        }


    }

}


class LabData{

    constructor(){




    }


}

