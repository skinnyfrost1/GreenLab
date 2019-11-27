function receiveMessage( message ){
    //console.log(message);
    //var data= JSON.parse(message);
    var evt= JSON.parse(message);
    functionHash[evt.type](evt);
}
function getUserEquipment() {
    var sendData = {};
    sendData["type"] = "UserEquipment";
    //sendData["userId"] = passport;
    sendFolderMessage( JSON.stringify(sendData) );
}
function getUserEquipmentFolder() {
    var sendData = {};
    sendData["type"] = "UserEquipmentFolder";
    sendFolderMessage( JSON.stringify(sendData) );
}
function getAllFolder() {
    var sendData = {};
    sendData["type"] = "AllFolder";
    // this is send unique
    // because only the current need to receive this
    sendImageMessage( JSON.stringify(sendData) );
    //
}
function getEquipment() {
    // for this one we will load with details
    // because the previous getString function
    // cut out some thing
    // so we will
}
function loadContent() {
    //getUserEquipment();
    getAllFolder();
    getLoadEquipment();
    // var sendData = {};
    // sendData["type"] = "loadImagesThenloadWindow";
    // sendData["imageIds"] = ["5dd20dc0cfb4771772408b6a"
    //     , "5dd20eb672ab133f93483daa", "5dd20ebb72ab133f93483dab"];
    // sendImageMessage( JSON.stringify(sendData) );
}





var functionHash = {};
functionHash["UserEquipment"] = function (evt) {
    //    console.log(evt);
    // {
    //     "data":{
    //     "equipmentFolderIds":[
    //         "5dd19437ed724845d9dd082d",
    //         "5dd19437ed724845d9dd082e",
    //         "5dd19437ed724845d9dd082f"
    //     ],
    //         "userId":"weixin.tang@stonybrook.edu"
    // },
    //     "type":"UserEquipment"
    // }
    data.UserEquipment = evt["data"];
    // this should evoke
    //sendFolderMessage()
    // for( var i = 0 ; i <  data.UserEquipment["equipmentFolderIds"].length ; i++){
    //
    //     console.log( data.UserEquipment["equipmentFolderIds"][i] );
    //
    //     var sendData = {};
    //     sendData["type"] = "UserEquipmentFolder";
    //
    //     sendData[]
    //
    // }
};
functionHash["UserEquipmentFolder"] = function (evt) {
};
function loadfolder() {
    // we only need refresh
    for( var i = 0 ; i< (Object.keys(data.UserEquipmentFolders)).length  ; i++){
        if( data.UserEquipmentFolders[ (Object.keys(data.UserEquipmentFolders))[i] ]["FolderName"] == data.currentFolderId ){
            var equipIds =  data.UserEquipmentFolders[ (Object.keys(data.UserEquipmentFolders))[i] ]["equipmentIdsInFolder"];
            cards.clearCards();
            for( var b = 0 ; b < equipIds.length ; b++ ){
                var equipmentData =  data.EquipmentData[equipIds[b]];
                // we need get image blob from blob id
                // so we need
                var imageBlob =  localStorage.getItem( equipmentData.coverBlobId );
                imageBlob = JSON.parse(imageBlob);
                cards.addCard( imageBlob.data.blob , equipmentData.equipmentName , equipmentData.description  , equipmentData.id )
            }
            cards.setHTML();
        }
    }
}
functionHash["loadImagesThenloadWindow"] = function (evt){
    //console.log(evt);
    for( var i = 0 ; i< evt.data.length ; i++ ){
        // so here we
        var imageBlob =  JSON.parse( evt.data[i] )
        localStorage.setItem( imageBlob.data.id ,  evt.data[i] );
    }
    loadfolder();
};
// functionHash["loadEquipment"] = function(evt){
//
//
//
// };
functionHash["AllFolder"] = function (evt) {
    // so we put all folder data together
    // here we will recieve everything
    // they it will display everything
    // what happened if the image data is not in there
    // so now we need create
    //console.log(JSON.stringify(evt.data.length));
    // now we can get those data
    // do we can
    // class Data {
    //
    //     constructor( ){
    //
    //         // everything inside will be get and set
    //         //this.
    //
    //         // I am thinking about
    //         // this is class we can get can type cast
    //         this.UserEquipment = new UserEquipment();
    //         this.UserEquipmentFolders = {"": new UserEquipmentFolder()};
    //         this.ImageBlob = {"": new ImageBlob()};
    //
    //     }
    for( var i = 0 ; i< evt.data.length ; i++ ){
        //console.log( evt.data[i] );
        var item =  JSON.parse( evt.data[i]);
        if(  item["type"] == "UserEquipment" ){
            data.UserEquipment = item.data;
        }else if( item["type"] == "UserEquipmentFolder" ){
            data.UserEquipmentFolders[ item.data["userEquipmentFolderId"] ] = item.data;
        }else if( item["type"] == "EquipmentData" ){
            data.EquipmentData[ item.data["id"]   ] =    item.data;
        }
    }
    // first formost
    //console.log( JSON.stringify( data ) );
    var coverImageIds = [];

    for( var i = 0 ; i< Object.keys(data.EquipmentData).length ; i++ ){

        var blobKey = data.EquipmentData[ Object.keys(data.EquipmentData)[i] ]["coverBlobId"];
        var blobData = localStorage.getItem( blobKey );

        if( blobData == null ){
            coverImageIds.push( blobKey );
        }
    }
    if( coverImageIds.length == 0 ){
        // we will directly go to the showing section
        loadfolder();
    }else{
        // here we will send image
        var sendData = {};
        sendData["type"] = "loadImagesThenloadWindow";
        sendData["imageIds"] = coverImageIds;
        sendImageMessage( JSON.stringify(sendData) );
    }
}

functionHash["refreshfolder"] = function () {

    getAllFolder();

};

functionHash["refreshEquipment"] = function () {


    getLoadEquipment();
    //getAllFolder();

};

functionHash["loadImageAndLoadEquipment"] = function (evt){

    var blob = JSON.parse(evt.data);

    //console.log(evt.data);

    localStorage.setItem(blob.data.id, evt.data  );

    loadEquipment();
};

functionHash["loadEquipment"] = function (evt) {

    //  JSON.parse(evt.data);
    //
    data.currentEquipmentData = JSON.parse(evt.data);

    // here we only check the cover image

    var coverImage = localStorage.getItem(  data.currentEquipmentData.coverBlobId );

    if( coverImage == null ){

        var sendData = {}
        sendData["type"] = "loadImageAndLoadEquipment";
        sendData["blobId"] = data.currentEquipmentData.coverBlobId;
        sendImageMessage( JSON.stringify(sendData) );

    }
    else{

        loadEquipment();

    }

    //getLoadEquipment();
    //getAllFolder();

};




function getLoadEquipment() {
    // here we will load all the equipment details
    var sendData ={};
    sendData["type"] = "loadEquipment";
    sendPadMessage(JSON.stringify(sendData));
}

function loadEquipment(){

    // so should have write many api before

    imageOP.setNameValue( data.currentEquipmentData.equipmentName );
    imageOP.setTextArea(data.currentEquipmentData.description);

    // var imageBlob =  localStorage.getItem( equipmentData.coverBlobId );
    // imageBlob = JSON.parse(imageBlob);
    // cards.addCard( imageBlob.data.blob

    var imageBlob = localStorage.getItem( data.currentEquipmentData.coverBlobId );
    imageBlob = JSON.parse(imageBlob);
    imageOP.setCoverImage(imageBlob.data.blob);
    //console.log(imageBlob);
    //editOp.setSizeValue( data.currentEquipmentData. )

}

//var loadWait = false;
function allFolderOnclick() {
    data.currentFolderId = "all";
    loadfolder();
}
function favouriteOnClick() {
    data.currentFolderId = "share";
    loadfolder();
}

class Protocal{
    constructor(){
        //this.socket;
        //this.stompClient;
    }
    messagePrepare(  objectId ,  type, value ){
        var data = {};
        data["objectId"] = objectId;
        data["type"] = type;
        data["value"] = value;
        return JSON.stringify( data );
    }
    activeAllButtons( protocal ){
        document.getElementById("myfile").onchange= async function (evt) {
            var file = evt.target.files[0];
            // we need upload the file through websocket
            //var reader = new FileReader();
            //console.log(reader.readAsBinaryString(file));
            //console.log(file);
            //document.getElementById("666test").innerHTML="<img src='"+(await toBase64(file))+"'/>";
            //console.log(await toBase64(file));
            // so it just upload some images
            // we will complete this part later
            var ImageData = (await toBase64(file));
            var sendData = {};
            sendData["type"] = "uploadCoverImage";
            sendData["data"] = ImageData;
            sendPadMessage( JSON.stringify(sendData) );
        };
        // object id well be equipment id
        //   image id
        //   polygon id   // this could be dot group,  line , polygon line, polygon itself
        // we need define the send information and handle the
        //  fileSelectedHandler
        // the new problem is what is image doesnot exist
        // so should request new image
        // the object Id is equipment Id
        // so the id will also be image id
        // the polygon id object id
        // so the dot group
        // so the line
        //
        // we need list all the possible operations
        //

        // for(var i = 0 ; i< (Object.keys(localStorage)).length ; i++){
        //     console.log(localStorage[Object.keys(localStorage)[i]]);
        // }

        document.getElementById("equipmentNameInput").oninput = function () {
            var equipmentName =  document.getElementById("equipmentNameInput").value;
            var data = {};
            data.type = "equipmentName";
            data.value = equipmentName;
            //sendFolderMessage( JSON.stringify(data) );
            sendPadMessage( JSON.stringify(data) )
        };
        //equipmentDescription
        document.getElementById("equipmentDescription").oninput = function () {

            var equipmentDescription =  document.getElementById("equipmentDescription").value;
            var data = {};
            data.type = "equipmentDescription";
            data.value = equipmentDescription;
            //sendFolderMessage( JSON.stringify(data) );
            sendPadMessage( JSON.stringify(data) )

        };
        //equipmentSize equipmentAngle
        document.getElementById("equipmentSize").oninput = function () {
            var equipmentSize = document.getElementById("equipmentSize").value;
            var data = {};
            data.type = "equipmentSize";
            data.value = equipmentSize;
            sendPadMessage( JSON.stringify(data) )
        };
        document.getElementById("equipmentAngle").oninput = function () {
            var equipmentAngle = document.getElementById("equipmentAngle").value;
            var data = {};
            data.type = "equipmentAngle";
            data.value = equipmentAngle;
            sendPadMessage( JSON.stringify(data) )
        };
    }
}
// we load images
//

class UserEquipment{
    constructor(){
        this.currentEquipmentId ="";
        this.equipmentFolderIds = [];
        this.userId = "";
    }
}
class UserEquipmentFolder{
    constructor(){
        this.userEquipmentFolderId = "";
        this.equipmentIdsInFolder = [];
        this.FolderName = "";
    }
}
class ImageBlob{
    constructor(){
        this.imageWidth = "190px";
        this.imageHeight = "250px";
        this.id = "id";
        this.blob ="blob";
    }
    //__build
    // <img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUA
    // AAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO
    // 9TXL0Y4OHwAAAABJRU5ErkJggg==" alt="Red dot" />
}
class Position{
    constructor(){
        this.x = "";
        this.y = "";
    }
}
class ImageData{
    constructor(){
        this.id = "";
        this.name = "";
        this.position = new Position();
        this.angle = "";
        this.size = "";
        this.blobId = "";
    }
}
//class

//
// class Data {
//     constructor( ){
//         this.UserEquipment = "";
//         this.UserEquipmentFolders = {};
//         this.EquipmentData ={};
//         this.ImageBlob = {};
//         this.currentFolderId = "all";
//
//         this.currentEquipmentData = null;
//
//
//         //this.currentEquipmentId = "5dd212af81475723231cd44e"; // we load this equipment base on this
//         // everything inside will be get and set
//         //this.
//         // I am thinking about
//         // this is class we can get can type cast
//         // this.UserEquipment = new UserEquipment();
//         // this.UserEquipmentFolders = {"": new UserEquipmentFolder()};
//         // this.EquipmentData ={};
//         // this.ImageBlob = {"": new ImageBlob()};
//     }
//     // now we need
//     createFakeData(){
//         this.UserEquipment.equipmentFolderIds.push("1");
//         this.UserEquipment.equipmentFolderIds.push("2");
//         this.UserEquipment.equipmentFolderIds.push("3");
//         this.UserEquipmentFolders["1"] = new UserEquipmentFolder();
//         this.UserEquipmentFolders["2"] = new UserEquipmentFolder();
//         this.UserEquipmentFolders["3"] = new UserEquipmentFolder();
//         this.UserEquipmentFolders["1"].FolderName = "666";
//         this.UserEquipmentFolders["2"].FolderName = "111";
//         this.UserEquipmentFolders["3"].FolderName = "888";
//     }
//     getFoldersMin(){
//     }
//     setFoldersMin(  ){  //UserEquipment <-- we not put inside because we will do it later
//         // here is recieve the userEquipment
//         // all the operation should be locate operation api
//         // and update to
//     }
// }
//
//data.createFakeData();


//var folderMin = new FolderMin();
//folderMin.setSelf(folderMin);
//folderMin.__refresh();
//folderMin.setSelf(folderMin);
//folderMin.setHTML();
//folderMin.activeButton();

class AreaOp{
    constructor(){
    }
}
class AddOp{
    constructor(){
    }
}
//class Operation
class Magic{
    constructor(){
    }
}
//data.UserEquipmentFolders[""].
// so here we will do a experiment to do type cast
//
var StringTest = "{\"data\":{\"currentEquipmentId\":\"设备id\",\"equipmentFolderIds\":[\"1hao\",\"2hao\",\"3hao\"],\"userId\":\"xiaoming\"},\"type\":\"loadFoldersIds\"}";
var ObjectJSON = JSON.parse(StringTest);
console.log(  ObjectJSON.data );
// new Sortable( document.getElementById("sortable2") , {
//     animation: 150,
//     ghostClass: 'blue-background-class'
// });

// new Sortable( document.getElementById("folderContent") , {
//     animation: 150,
//     ghostClass: 'blue-background-class'
// });
