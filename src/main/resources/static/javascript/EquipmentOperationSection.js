


class EquipmentOperationSection{
    constructor(){
        this.imageId = "operationImageSection";
        this.editId = "operationEditSection";
        this.areaId = "operationAreaSection";
        this.addId = "operationAddSection";
        // this.self = null;
    }
    setActive( Id ){
        document.getElementById(Id).className="nav-link text-muted active";
        //console.log( Id.slice(9) );
        // we also need active the body content
        document.getElementById(Id.slice(9)).className = "card-body";
    }
    setInactive( Id ){
        document.getElementById(Id).className="nav-link text-muted";
        //console.log( Id.slice(9) );
        document.getElementById(Id.slice(9)).className = "card-body d-none";
    }
    getIds(){
        return [this.imageId, this.editId, this.areaId,  this.addId ];
    }
    clearActive(){
        for( var i = 0 ; i< this.getIds().length ; i++ ){
            this.setInactive(  this.getIds()[i] );
        }
    }
    setIndexActive( index ){
        this.clearActive();
        this.setActive( this.getIds()[index] );
    }
    // ativeAllButton(){
    //
    //     document.getElementById(this.imageId).onclick =
    //
    // }
    getActiveFunctions(){
        return [ this.activeImage ,this.activeEdit , this.activeArea, this.activeAdd ]
    }
    activeImage(){
        this.clearActive();
        this.setActive( this.imageId );
    }
    activeAdd(){
        this.clearActive();
        this.setActive( this.addId );
    }
    activeArea(){
        this.clearActive();
        this.setActive( this.areaId );
    }
    activeEdit(){
        this.clearActive();
        this.setActive( this.editId );
    }
    activeAllButton( self ){
        //self = new EquipmentOperationSection();
        document.getElementById( self.imageId ).onclick = function () {
            self.activeImage();
        };
        document.getElementById( self.editId ).onclick = function () {
            self.activeEdit();
        };
        document.getElementById(self.areaId).onclick = function () {
            self.activeArea();
        };
        document.getElementById(self.addId).onclick = function () {
            self.activeAdd();
        };
    }
}



class ImageOp{
    constructor(){
        this.equipmentNameInputId = "equipmentNameInput";
        this.equipmentDescriptionId = "equipmentDescription";
        this.coverImageId = "coverImageId";
        this.coverImageUploadId = "myfile";
        this.self = null;


    }
    getNameValue(){
        return document.getElementById(this.equipmentNameInputId).value;
    }
    setNameValue( value ){
        document.getElementById(this.equipmentNameInputId).value = value;
    }
    getTextArea(){
        return document.getElementById(this.equipmentDescriptionId).value;
    }
    setTextArea( value ){
        document.getElementById( this.equipmentDescriptionId ).value = value;
    }

    setCoverImage( value ){

        //<img src='' class='grid-photo-center' style='background-color: aquamarine' alt=''>
        // <img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUA
        // AAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO
        // 9TXL0Y4OHwAAAABJRU5ErkJggg==" alt="Red dot" />

        document.getElementById( this.coverImageId ).innerHTML = "<img src='"+value+"' class='grid-photo-center' style='background-color: aquamarine' alt=''>";

    }

    handleNameInput( name ){

        sendProtocal["updateNameBoth"](name);


    }

    handleTextAreaInput( description ){

        sendProtocal["updateDescriptionBoth"](description);

    }

    handleImageInput( ImageData ){

        sendProtocal["uploadCoverImageBoth"](ImageData);

    }

    activeAll(){
        var imageOp = this.self;
        document.getElementById(imageOp.equipmentNameInputId).oninput = function () {

            imageOp.handleNameInput( imageOp.getNameValue() );
        };

        document.getElementById(imageOp.equipmentDescriptionId).oninput = function () {
            imageOp.handleTextAreaInput( imageOp.getTextArea() );
        };

        document.getElementById(imageOp.coverImageUploadId).oninput = async function (evt) {
            var file = evt.target.files[0];
            var ImageData = (await toBase64(file));
            imageOp.handleImageInput( ImageData );
        }
    }


}



class EditOp{
    constructor(){
        this.equipmentSize = "equipmentSize";
        this.equipmentAngle = "equipmentAngle";
        this.equipmentX = "equipmentX";
        this.equipmentY = "equipmentY";


        this.statusName = "statusName";
        this.statusImageUploadId = "myEquipmentStatusFile";
        this.imagesDropdown = "imagestatusDropDown";

        this.imageDataDelete = "imageDataDelete";

        this.self = null;

    }

    // so we need so many operation here


    setDropDown( imageDataIds, imageDataNames ){

        var len = imageDataIds.length;
        var total = "";
        for( var i = 0 ; i< len ; i++ ){
            var imageDataId = imageDataIds[i];
            var imageDataName = imageDataNames[i];

            total = total + this.getDropDownItem( imageDataId , imageDataName );
        }
        this.setDropDownHTML( total );
        this.activeImageDataButtons( imageDataIds );

    }

    getDropDownItem( imageDataId , imageDataName ){
        return "<div class='dropdown-item text-truncate' id='"+imageDataId+"' >"+imageDataName+"</div>";
    }

    setDropDownHTML( htmlContent ){
        document.getElementById(this.imagesDropdown).innerHTML = htmlContent;
    }

    activeImageDataButtons( imageDataIds ){

        for( var i = 0 ; i< imageDataIds.length ; i++ ){

            var imageDataId = imageDataIds[i];
            document.getElementById( imageDataId ).onclick = function (evt) {

                //var imageDataId = evt.id;

                //var makelocal = imageDataId;

               // console.log(evt);

                var makelocal = evt.path[0].id;

                // console.log(imageDataId);

                //console.log(makelocal);
                // yes it's sendprotocal
                sendProtocal["selectImageData"]( makelocal );



            }

        }

    }

    //handleSelectImageData()


    getStatusName(){
        return document.getElementById(this.statusName).value;
    }
    setStatusName( value ){
        document.getElementById(this.statusName).value = value;
    }
    getSizeValue(){
        return document.getElementById(this.equipmentSize).value;
    }
    setSizeValue( value ){
        document.getElementById(this.equipmentSize).value = value;
    }
    getAngleValue(){
        return document.getElementById(this.equipmentAngle).value;
    }
    setAngleValue( value ){
        document.getElementById(this.equipmentAngle).value = value;
    }
    // here we need set x position and set y position
    getXposition(){
        return document.getElementById(this.equipmentX).value;
    }
    setXposition( value ){
        document.getElementById(this.equipmentX).value = value;
    }
    getYPosition(){
        return document.getElementById(this.equipmentY).value;
    }
    setYPosition( value ){
        document.getElementById(this.equipmentY).value  =value;
    }

    handleStatusName( name ){

        // if( equipmentData.currentEquipmentData.currentImageIds != "" ){
        //
        //     console.log( equipmentData.currentEquipmentData.currentImageIds );
        //
        // }

        //console.log( equipmentData.currentEquipmentData.currentImageIds );

        var makeLocal = equipmentData.currentEquipmentData.currentImageIds;

        if( makeLocal != "" ){

           sendProtocal["updateStatusNameImage"]( makeLocal   , name  );

        }

        //sendProtocal["updateStatusNameImage"] = function ( imageDataId , sizeAngle ) {

    }

    handleEquipmentSize( size ){

        if( equipmentData.currentEquipmentData.currentImageIds != "" ){
            sendProtocal["updateSizeImage"]( equipmentData.currentEquipmentData.currentImageIds , size  );
        }

        //sendProtocal["updateSizeImage"] = function ( imageDataId , sizeValue ) {

    }

    handleEquipmentAngle( angle ){

        if( equipmentData.currentEquipmentData.currentImageIds != "" ){

            sendProtocal["updateAngleImage"]( equipmentData.currentEquipmentData.currentImageIds , angle );

        }
        //sendProtocal["updateAngleImage"] = function ( imageDataId , sizeAngle ) {

    }

    handleEquipmentXposition( xValue ){
        // so here we need check if it's

        if( equipmentData.currentEquipmentData.currentImageIds != "" ){

            sendProtocal["updateXposImage"]( equipmentData.currentEquipmentData.currentImageIds , xValue );

        }
       // sendProtocal["updateXposImage"] = function ( imageDataId , Xpos ) {

    }

    handleEquipmentYPosition( yvalue ){

        if( equipmentData.currentImageDataId != "" ){

            sendProtocal["updateYposImage"]( equipmentData.currentImageDataId , yvalue );

        }
        //sendProtocal["updateYposImage"] = function ( imageDataId , Ypos ) {

    }

    handleStatusImageUpload( imageData ){



        var img = new Image();
        img.onload = function() {

            sendProtocal["uploadStatusImageImage"]( img.src , this.width ,this.height  );

        };
        img.src = imageData;


    }

    handleImageDataDelete(){

        var makeLocal = equipmentData.currentEquipmentData.currentImageIds;

        sendProtocal["ImageDataDelete"]( makeLocal );
    }

    activeAll(){

        var editOp = this.self;

        document.getElementById(editOp.equipmentX).oninput = function () {
            editOp.handleEquipmentXposition( editOp.getXposition() );
        };

        document.getElementById(editOp.equipmentY).oninput = function () {
            editOp.handleEquipmentYPosition(editOp.getYPosition());
        };

        document.getElementById(editOp.equipmentAngle).onchange = function () {
            editOp.handleEquipmentAngle( editOp.getAngleValue() );
        };

        document.getElementById( editOp.equipmentSize ).onchange = function () {
            editOp.handleEquipmentSize( editOp.getSizeValue() );
        };

        document.getElementById(editOp.statusName).oninput = function () {
            editOp.handleStatusName( editOp.getStatusName() );
        };
        //this.statusImageUploadId
        document.getElementById(editOp.statusImageUploadId).onchange = async function (evt) {

            var file = evt.target.files[0];
            var ImageData = (await toBase64(file));
            editOp.handleStatusImageUpload( ImageData );

        }

        document.getElementById( editOp.imageDataDelete ).onclick= function () {

            editOp.handleImageDataDelete();

        }

    }



}


class SetOp{

    constructor(){

        // actually we don't have do right now

        // we need get id here

        // so work on chemistry then back to here

        this.likeEquipment = "likeEquipment";
        this.shareEquipment = "shareEquipment";
        this.createDuplicate = "createDuplicate";
        this.deleteEquipment = "deleteEquipment";

        this.self;
    }


    setLikeBaseBoolean( booleanValue ){
        if( booleanValue == true ){
            this.setDisLike();
        }else{
            this.setLike();
        }
    }

    setShareBaseBoolean( booleanValue ){
        if( booleanValue == true ){
            this.setNoShare();
        }else{
            this.setShare();
        }
    }

    setLike(){
        //document.getElementById(this.likeEquipment).innerHTML = "<i class='fas fa-heart'></i> Like Equip";
        document.getElementById(this.likeEquipment).className = "btn btn-lg h2 d-block btn-outline-danger text-capitalize";

    }

    setDisLike(){
        //document.getElementById(this.likeEquipment).innerHTML = "<i class='fas fa-heart'></i> DisLike Equip";
        document.getElementById(this.likeEquipment).className = "btn btn-lg h2 d-block btn-danger text-capitalize";
    }

    setShare(){
        document.getElementById(this.shareEquipment).className =   "btn btn-lg h2 d-block btn-outline-success text-capitalize";
    }

    setNoShare(){
        document.getElementById(this.shareEquipment).className =   "btn btn-lg h2 d-block btn-success text-capitalize";
    }

    isLike(){
        if( document.getElementById(this.likeEquipment).className == "btn btn-lg h2 d-block btn-outline-danger text-capitalize" ){
            return true;
        }else{
            return false;
        }
    }

    // true means have not share and want to share
    isShare(){
        if( document.getElementById(this.shareEquipment).className == "btn btn-lg h2 d-block btn-outline-success text-capitalize" ){
            return true;
        }else{
            return false;
        }
    }

    handleLike( boolean ){

        //
        //console.log(boolean);
        //equipmentData.currentEquipmentData.currentImageIds
        //var currentImageId = equipmentData.currentEquipmentData.currentImageIds;

        sendProtocal["likeEquipment"](   boolean );

    }

    handleShare( boolean ){

        //var currentImageId = equipmentData.currentEquipmentData.currentImageIds;
        //console.log(boolean);
        sendProtocal["shareEquipment"](   boolean);
    }

    handleDelete(){

        //var currentImageId = equipmentData.currentEquipmentData.currentImageIds;

        sendProtocal["deleteEquipment"]( );

    }

    handleDuplicate(){

        //var currentImageId = equipmentData.currentEquipmentData.currentImageIds;

        sendProtocal["duplicateEquipment"](  );

    }

    activeAll(){

        var setOp = new SetOp();
        document.getElementById( setOp.likeEquipment ).onclick = function () {
            setOp.handleLike( setOp.isLike() );
        };

        document.getElementById( setOp.shareEquipment ).onclick = function () {
            setOp.handleShare( setOp.isShare() );
        };

        document.getElementById( setOp.createDuplicate ).onclick =function () {
            setOp.handleDuplicate();
        };

        document.getElementById( setOp.deleteEquipment ).onclick =function () {
            setOp.handleDelete();
        };

    }




}

class AddOp{

    constructor(){



    }




}


