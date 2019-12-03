package com.greenlab.greenlab.labEquip.equipment;


//import green.lab.equipment.equipmentData.notUse.EquipmentData;
//import green.lab.equipment.equipmentData.notUse.UserEquipment;
//import green.lab.equipment.equipmentData.notUse.UsersEquipment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.json.JsonMapper;

import com.greenlab.greenlab.dto.SingleStringRequestBody;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.ImageData.ImageData;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.ImageData.ImageDataRepository;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.equipmentData.EquipmentData;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.equipmentData.EquipmentDataRepository;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.polygonData.Position;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.userEquipment.UserEquipment;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.userEquipment.UserEquipmentFolder;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.userEquipment.UserEquipmentFolderRepository;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.userEquipment.UserEquipmentRepository;
import com.greenlab.greenlab.labEquip.framework.imageBlob.ImageBlob;
import com.greenlab.greenlab.labEquip.framework.imageBlob.ImageBlobRepository;
import com.greenlab.greenlab.labEquip.laboratory.labData.LabData;
import com.greenlab.greenlab.labEquip.laboratory.labData.LabDataRepository;
import com.greenlab.greenlab.model.Lab;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.websocket.Session;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
public class EquipmentController {




    @Autowired
    private UserEquipmentRepository userEquipmentRepository;

    @Autowired
    private UserEquipmentFolderRepository userEquipmentFolderRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private EquipmentDataRepository equipmentDataRepository;

    @Autowired
    private ImageBlobRepository imageBlobRepository;

    @Autowired
    private MongoTemplate mongoTemplate;



    @RequestMapping( value="/equipment/{equipmentId}", method = RequestMethod.GET )
    public String getEquipmentPage(Model model, @PathVariable("equipmentId") String equipmentId, HttpServletRequest request ){

        //model.addAttribute("jumpType", new JumpType());
//        if( request.getSession().getAttribute("email")!=null){
//
//            model.addAttribute( "isLogin", "false" );
//            model.addAttribute( "email", request.getSession().getAttribute("email") );
//        }else{
//
//
//            model.addAttribute( "isLogin", "true" );
//        }

        EquipmentData equipmentData = equipmentDataRepository.getById(equipmentId);

        if( equipmentData == null ){
            return "";
        }else{

            UserEquipmentFolder userEquipmentFolder = userEquipmentFolderRepository.findByOwnerAndType( "weixin.tang@stonybrook.edu" , "recent" );

            LinkedList<String> linkedList =  userEquipmentFolder.getItemIdsInFolder();

            if( linkedList.contains( equipmentData.getId() ) == true ){
                linkedList.remove( equipmentData.getId() );
            }

            linkedList.add( 0, equipmentData.getId() );
            userEquipmentFolder.setItemIdsInFolder( linkedList );
            userEquipmentFolderRepository.save( userEquipmentFolder );
            model.addAttribute( "email", "weixin.tang@stonybrook.edu" );
            model.addAttribute( "iframeAdress", "/equipmentBoard/"+equipmentId );
            return "lab/equip";

        }

    }

    @RequestMapping( value = "/equipmentBoard/{equipmentId}", method = RequestMethod.GET )
    public String getEquipmentBoard(Model model, @PathVariable("equipmentId") String equipmentId, HttpServletRequest request  ){

        EquipmentData equipmentData = equipmentDataRepository.getById(equipmentId);

        if( equipmentData == null ){
            return "welcome";
        }else{
            return "lab/equipboard";
        }

    }

    @RequestMapping(value="/ajax/requestId" , method = RequestMethod.POST)
    @ResponseBody
    public Object requestId(HttpServletRequest request) throws JSONException {
        String dataStr = request.getParameter("data");
        //System.out.println( dataStr );
        Map<String,Object> sendData = new HashMap<>();
        sendData.put("success",true);
        sendData.put("data", "weixin.tang@stonybrook.edu" );
        return sendData;
    }

    @RequestMapping(value="/ajax/requestNewEquipment" , method = RequestMethod.POST)
    @ResponseBody
    public Object requestNewEquipment(HttpServletRequest request) throws JSONException, JsonProcessingException {
        String dataStr = request.getParameter("data");
        //System.out.println( dataStr );

        EquipmentData equipmentData = new EquipmentData();
        equipmentData.setOwnerId( "weixin.tang@stonybrook.edu" );
        String equipmentId =  equipmentDataRepository.save(equipmentData).getId();
        Query query = new Query();
        query.addCriteria(Criteria.where("type").is("all").andOperator( Criteria.where("owner").is("weixin.tang@stonybrook.edu") ));

        UserEquipmentFolder userEquipmentFolder =  mongoTemplate.findOne( query , UserEquipmentFolder.class );
        userEquipmentFolder.getItemIdsInFolder().add(0,equipmentId);
        userEquipmentFolderRepository.save( userEquipmentFolder );

        sendEquipFolder( "weixin.tang@stonybrook.edu" ,   prepareUpdateAll() );

        Map<String,Object> sendData = new HashMap<>();
        sendData.put("success",true);
        sendData.put("data", equipmentId );
        return sendData;
    }

    public JSONArray prepareAllImages(JSONArray str ) throws JSONException, JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();
            JSONArray jsonArray = new JSONArray();
            for( int i = 0 ; i< str.length() ; i++ ){
                ImageBlob imageBlob = imageBlobRepository.getById( str.get(i).toString() );
                jsonArray.put(  objectMapper.writeValueAsString( imageBlob ) );
            }
            return jsonArray;
    }

    @MessageMapping("/equipment/front/{userId}/{sessionId}")
    public void handleEquipmentFront(@DestinationVariable String userId , @DestinationVariable String sessionId , String message) throws JSONException, JsonProcessingException {





        JSONObject jsonObject = new JSONObject(message);

        try {
            String __equipmentId__ = jsonObject.get("itemId").toString();

            if( __equipmentId__ != null ){

                EquipmentData equipmentData =  equipmentDataRepository.getById( __equipmentId__ );

                if( equipmentData ==  null){

                    // if should
                    return;
                }
            }

        }catch (Exception e){


        }





        if( jsonObject.get("type").toString().equals("connectSuccess")  ){
            messagingTemplate.convertAndSend("/topic/image/" + userId + "/" + sessionId, jsonObject.toString() );
        }else if( jsonObject.get("type").toString().equals("loadFolder") ){

            jsonObject.put("data", prepareAllFolder( "weixin.tang@stonybrook.edu" ) );

            //websocket.sendEquipFolder( userId , jsonObject.toString() );
            sendUnique( userId , sessionId , jsonObject.toString() );
            //messagingTemplate.convertAndSend("/topic/equipment/folder/" + userId , jsonObject.toString() );
        }else if( jsonObject.get("type").toString().equals("loadEquipment") ){

            //String equipmentId = jsonObject.get("itemId").toString();
            //UpdateTheEquipment( equipmentId ,  userId );

            String equipmentId = jsonObject.get("itemId").toString();
            jsonObject.put("data", prepareAllEquipment( equipmentId ) );
            //jsonObject.put("data",  );
            //System.out.println(jsonObject.toString());
            //messagingTemplate.convertAndSend("/topic/equipment/pad/" + equipmentId , jsonObject.toString() );
            //messagingTemplate.convertAndSend("/topic/image/" + userId + "/" + sessionId, jsonObject.toString() );
            sendUnique( userId , sessionId , jsonObject.toString() );
        }else if( jsonObject.get("type").toString().equals("updateNameBoth") ){
            String name =  jsonObject.get("data").toString();
            String equipmentId = jsonObject.get("itemId").toString();
            EquipmentData equipmentData = equipmentDataRepository.getById(equipmentId);
            equipmentData.setName(name);
            equipmentDataRepository.save(equipmentData);

            //messagingTemplate.convertAndSend("/topic/image/" + userId + "/" + sessionId, prepareUpdateAll() );

            sendEquipFolder( userId , prepareUpdateAll() );
            //messagingTemplate.convertAndSend("/topic/equipment/folder/" + userId , prepareUpdateAll() );
        }else if( jsonObject.get("type").toString().equals("updateDescriptionBoth") ){
            String description =  jsonObject.get("data").toString();
            String equipmentId = jsonObject.get("itemId").toString();
            EquipmentData equipmentData = equipmentDataRepository.getById(equipmentId);
            equipmentData.setDescription(description );
            equipmentDataRepository.save(equipmentData);
            //messagingTemplate.convertAndSend("/topic/equipment/folder/" + userId , prepareUpdateAll() );
            sendEquipFolder( userId , prepareUpdateAll() );
            //messagingTemplate.convertAndSend("/topic/image/" + userId + "/" + sessionId, prepareUpdateAll() );
        }else if( jsonObject.get("type").toString().equals("uploadCoverImageBoth") ){
            String imageDataStr =  jsonObject.get("data").toString();
            String equipmentId = jsonObject.get("itemId").toString();
            EquipmentData equipmentData = equipmentDataRepository.getById(equipmentId);
            ImageBlob imageBlob = new ImageBlob();
            imageBlob.setBlob(imageDataStr);
            imageBlob.setCounter(1);
            String imageBlobId =  imageBlobRepository.save(imageBlob).getId();
            if( equipmentData.getCoverBlobId().equals("")  ){
            }else{
                ImageBlob imageBlob1 = imageBlobRepository.getById( equipmentData.getCoverBlobId() );
                int counter = imageBlob1.getCounter() -1;
                if( counter <= 0 ){
                    imageBlobRepository.deleteById( equipmentData.getCoverBlobId() );
                }else{
                    imageBlob1.setCounter( counter );
                    imageBlobRepository.save( imageBlob1 );
                }
            }
            equipmentData.setCoverBlobId(imageBlobId);
            equipmentDataRepository.save(equipmentData);
            //messagingTemplate.convertAndSend("/topic/equipment/folder/" + userId , prepareUpdateAll() );
            //messagingTemplate.convertAndSend("/topic/image/" + userId + "/" + sessionId, prepareUpdateAll() );
            sendEquipFolder( userId , prepareUpdateAll() );

        }else if( jsonObject.get("type").toString().equals("loadImage") ){
//            String imageDataStr =  jsonObject.get("data").toString();
//
//            JSONObject jsonObject1 = new JSONObject(imageDataStr);

            JSONObject jsonObject1 = (JSONObject) jsonObject.get("data");
            JSONArray jsonArray = jsonObject1.getJSONArray("imageIdArr");
            String refreshType = jsonObject1.get("refreshType").toString();

            JSONArray imageBlobArr =  prepareAllImages(  jsonArray  );
            jsonObject1.put("data", imageBlobArr );

            jsonObject.put( "data", jsonObject1 );
            //System.out.println(imageDataStr);
            //{"imageIdArr":["5dd813916e04ab4b078b9d73",""],"refreshType":"displayFolder"}

            //messagingTemplate.convertAndSend("/topic/image/" + userId + "/" + sessionId, jsonObject.toString() );
            sendUnique( userId, sessionId , jsonObject.toString()  );
        }else if( jsonObject.get("type").toString().equals("uploadStatusImageImage") ){


            JSONObject jsonObject1 = (JSONObject) jsonObject.get("data");
            String imageDataStr =  jsonObject1.get("imageData").toString();
            String width = jsonObject1.get("width").toString();
            String height = jsonObject1.get("height").toString();
            ImageBlob imageBlob = new ImageBlob();
            imageBlob.setBlob(imageDataStr);
            imageBlob.setOriginalHeight( Integer.parseInt(height) );
            imageBlob.setOriginalWidth( Integer.parseInt(width) );
            imageBlob.setCounter(1);
            String imageBlobId =  imageBlobRepository.save(imageBlob).getId();
            ImageData imageData = new ImageData();
            imageData.setBlobId( imageBlobId );
            String imageDataId =  imageDataRepository.save( imageData ).getId();
            String equipmentId = jsonObject.get("itemId").toString();
            EquipmentData equipmentData = equipmentDataRepository.getById(equipmentId);
            equipmentData.getImageIds().add(imageDataId);
            equipmentData.setCurrentImageIds( imageDataId );
            equipmentDataRepository.save( equipmentData );


            UpdateTheEquipment(  equipmentId ,  userId );
            updateEquipmentBoard(  equipmentId );
            //updateEquipmentBoard(  equipmentId );

        }else if( jsonObject.get("type").toString().equals("updateStatusNameImage") ){

            //System.out.println( message );

            JSONObject jsonObject1 = (JSONObject) jsonObject.get("data");
            //System.out.println( jsonObject1.toString() );
            String imageDataId = jsonObject1.get("imageDataId").toString();
            String statusName = jsonObject1.get("statusName").toString();

            ImageData imageData = imageDataRepository.getById( imageDataId );
            imageData.setName( statusName );
            imageDataRepository.save( imageData );
            String equipmentId = jsonObject.get("itemId").toString();
            UpdateTheEquipment(  equipmentId ,  userId );


        }else if( jsonObject.get("type").toString().equals("updateSizeImage") ){

//            JSONObject jsonObject1 = (JSONObject) jsonObject.get("data");
//            //System.out.println( jsonObject1.toString() );
//            String imageDataId = jsonObject1.get("imageDataId").toString();
//            String sizeValue = jsonObject1.get("sizeValue").toString();
//
//            ImageData imageData = imageDataRepository.getById( imageDataId );
//            imageData.setSize(Integer.parseInt(sizeValue));
//            imageDataRepository.save( imageData );
//            String equipmentId = jsonObject.get("itemId").toString();
//            UpdateTheEquipment(  equipmentId ,  userId );
//
//            updateEquipmentBoard(  equipmentId );
//            //websocket.sendLabBoard();


        }else if( jsonObject.get("type").toString().equals("updateAngleImage") ){

//            JSONObject jsonObject1 = (JSONObject) jsonObject.get("data");
//            //System.out.println( jsonObject1.toString() );
//            String imageDataId = jsonObject1.get("imageDataId").toString();
//            String sizeAngle = jsonObject1.get("sizeAngle").toString();
//
//            ImageData imageData = imageDataRepository.getById( imageDataId );
//            imageData.setAngle(Integer.parseInt(sizeAngle));
//            imageDataRepository.save( imageData );
//            String equipmentId = jsonObject.get("itemId").toString();
//            UpdateTheEquipment(  equipmentId ,  userId );
//
//            updateEquipmentBoard(  equipmentId );
//            //websocket.sendLabBoard();

        }else if( jsonObject.get("type").toString().equals("updateXposImage") ){

            JSONObject jsonObject1 = (JSONObject) jsonObject.get("data");
            //System.out.println( jsonObject1.toString() );
            String imageDataId = jsonObject1.get("imageDataId").toString();
            String Xpos = jsonObject1.get("Xpos").toString();
            ImageData imageData = imageDataRepository.getById( imageDataId );
            Position position = imageData.getPosition();
            position.setX(Integer.parseInt(Xpos));
            imageData.setPosition( position );
            imageDataRepository.save( imageData );
            String equipmentId = jsonObject.get("itemId").toString();
            UpdateTheEquipment(  equipmentId ,  userId );

            updateEquipmentBoard(  equipmentId );
            //websocket.sendLabBoard();

        }else if( jsonObject.get("type").toString().equals("updateYposImage") ){

            JSONObject jsonObject1 = (JSONObject) jsonObject.get("data");
            //System.out.println( jsonObject1.toString() );
            String imageDataId = jsonObject1.get("imageDataId").toString();
            String Ypos = jsonObject1.get("Ypos").toString();
            ImageData imageData = imageDataRepository.getById( imageDataId );
            Position position = imageData.getPosition();
            position.setY(Integer.parseInt(Ypos));
            imageData.setPosition( position );
            imageDataRepository.save( imageData );
            String equipmentId = jsonObject.get("itemId").toString();
            UpdateTheEquipment(  equipmentId ,  userId );

            updateEquipmentBoard(  equipmentId );
            //websocket.sendLabBoard();

        }else if( jsonObject.get("type").toString().equals("selectImageData") ){

            String equipmentId = jsonObject.get("itemId").toString();
            String selectImageDataId = jsonObject.get("data").toString();
            EquipmentData equipmentData = equipmentDataRepository.getById( equipmentId );
            equipmentData.setCurrentImageIds( selectImageDataId );
            equipmentDataRepository.save(equipmentData);
            UpdateTheEquipment(  equipmentId ,  userId );

            updateEquipmentBoard(  equipmentId );
            updateEquipmentBoard(   equipmentId );
            //websocket.sendLabBoard();

        }else if( jsonObject.get("type").toString().equals("ImageDataDelete") ){

            String equipmentId = jsonObject.get("itemId").toString();
            String selectImageDataId = jsonObject.get("data").toString();
            EquipmentData equipmentData = equipmentDataRepository.getById( equipmentId );
            if( equipmentData.getCurrentImageIds().equals( selectImageDataId ) ){
                equipmentData.setCurrentImageIds("");

                //websocket.sendLabBoard();

                //websocket.

            }
            LinkedList<String> linkedList =  equipmentData.getImageIds();
            linkedList.remove( selectImageDataId );
            equipmentData.setImageIds( linkedList );
            equipmentDataRepository.save( equipmentData );

            ImageData imageData = imageDataRepository.getById( selectImageDataId );
            String imageBlobId = imageData.getBlobId();
            decreamentImageBlobCount(  imageBlobId  );
            imageDataRepository.deleteById(selectImageDataId);
            UpdateTheEquipment(  equipmentId ,  userId );

            updateEquipmentBoard(  equipmentId );

        }else if( jsonObject.get("type").toString().equals("ImageDataRecieveText") ){
            String equipmentId = jsonObject.get("itemId").toString();
            JSONObject jsonObject1 =  (JSONObject)jsonObject.get("data");
            String imageDataId = jsonObject1.get("imageData").toString();
            String receiveText = jsonObject1.get("ReceiveText").toString();
            ImageData imageData = imageDataRepository.getById( imageDataId );
            LinkedList<String> linkedList =  imageData.getReceiveText();
            linkedList.clear();
            linkedList.add( receiveText );
            imageData.setReceiveText(linkedList);
            imageDataRepository.save( imageData );

            UpdateTheEquipment(  equipmentId ,  userId );

            //System.out.println(  );

        }else if( jsonObject.get("type").toString().equals("ImageDataSendText") ) {


            String equipmentId = jsonObject.get("itemId").toString();
            JSONObject jsonObject1 =  (JSONObject)jsonObject.get("data");
            String imageDataId = jsonObject1.get("imageData").toString();
            String receiveText = jsonObject1.get("SendText").toString();
            ImageData imageData = imageDataRepository.getById( imageDataId );
            LinkedList<String> linkedList =  imageData.getSendText();
            linkedList.clear();
            linkedList.add( receiveText );
            imageData.setSendText( linkedList );
            imageDataRepository.save( imageData );

            UpdateTheEquipment(  equipmentId ,  userId );

        }else if( jsonObject.get("type").toString().equals("likeEquipment") ){

            //System.out.println(  (Boolean)jsonObject.get("data") );

            String equipmentId = jsonObject.get("itemId").toString();
            Boolean like = (Boolean)jsonObject.get("data");
            EquipmentData equipmentData = equipmentDataRepository.getById(equipmentId);
            equipmentData.setFavourite(like);
            equipmentDataRepository.save( equipmentData );
            UserEquipmentFolder userEquipmentFolder = userEquipmentFolderRepository.findByOwnerAndType( userId , "favourite" );
            LinkedList<String> linkedList =  userEquipmentFolder.getItemIdsInFolder();

            if( like == true ){ // like it
                // so we will insert
                if( linkedList.contains( equipmentId ) == false ){
                    linkedList.add(0, equipmentId );
                }
            }else{
                if( linkedList.contains(equipmentId) == true ){
                    linkedList.remove( equipmentId );
                }
            }
            userEquipmentFolder.setItemIdsInFolder(linkedList);
            userEquipmentFolderRepository.save(userEquipmentFolder);
            sendEquipPad( equipmentId , prepareUpdateAll() );

        }else if( jsonObject.get("type").toString().equals("shareEquipment") ){

            //System.out.println(  (Boolean)jsonObject.get("data") );

            String equipmentId = jsonObject.get("itemId").toString();
            Boolean share = (Boolean)jsonObject.get("data");
            EquipmentData equipmentData = equipmentDataRepository.getById(equipmentId);
            equipmentData.setShared(share );
            equipmentDataRepository.save( equipmentData );
            UserEquipmentFolder userEquipmentFolder = userEquipmentFolderRepository.findByOwnerAndType( userId , "share" );
            LinkedList<String> linkedList =  userEquipmentFolder.getItemIdsInFolder();
            if( share == true ){ // like it
                // so we will insert
                if( linkedList.contains( equipmentId ) == false ){
                    linkedList.add(0, equipmentId );
                }
            }else{
                if( linkedList.contains(equipmentId) == true ){
                    linkedList.remove( equipmentId );
                }
            }
            userEquipmentFolder.setItemIdsInFolder(linkedList);
            userEquipmentFolderRepository.save(userEquipmentFolder);
            sendEquipPad( equipmentId , prepareUpdateAll() );

        }else if( jsonObject.get("type").toString().equals("deleteEquipment") ){


            String equipmentId = jsonObject.get("itemId").toString();

            // we will look at all the folders
            // if it contain the equipment
                // just delete it

            List<UserEquipmentFolder> userEquipmentFolders = userEquipmentFolderRepository.findAllByOwner( userId );

            for( int i = 0 ; i < userEquipmentFolders.size() ; i++ ){

                UserEquipmentFolder userEquipmentFolder = userEquipmentFolders.get(i);

                LinkedList<String> linkedList = userEquipmentFolder.getItemIdsInFolder();
                if( linkedList.contains(equipmentId) == true ){
                    linkedList.remove( equipmentId );
                    userEquipmentFolder.setItemIdsInFolder( linkedList );
                    userEquipmentFolderRepository.save( userEquipmentFolder );
                }
            }

            EquipmentData equipmentData = equipmentDataRepository.getById( equipmentId );



            LinkedList<String> linkedList = equipmentData.getImageIds();

            if( linkedList != null ){

                for( int i = 0 ; i< linkedList.size() ;i++ ){

                    String imageDataId = linkedList.get(i);
                    ImageData imageData = imageDataRepository.getById(imageDataId);

                    if( imageData != null ){

                        String imageBlobId = imageData.getBlobId();
                        decreamentImageBlobCount(  imageBlobId  );
                        imageDataRepository.deleteById(imageDataId);
                    }

                }
            }

            equipmentDataRepository.deleteById( equipmentId );

            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put( "type", "deleteEquipment" );
            jsonObject.put( "data", equipmentId );

            sendEquipFolder(  userId , jsonObject1.toString()   );


            //sendEquipPad( equipmentId , prepareUpdateAll() );

        }else if( jsonObject.get("type").toString().equals("duplicateEquipment") ){
            String equipmentId = jsonObject.get("itemId").toString();

            //System.out.println( "print something to start 1124" );
            UserEquipmentFolder userEquipmentFolder = userEquipmentFolderRepository.findByOwnerAndType( userId , "all" );

            String newEquipmentId =   duplicateEquipment( equipmentId );

            LinkedList<String> linkedList = userEquipmentFolder.getItemIdsInFolder();
            linkedList.add( 0, newEquipmentId );
            userEquipmentFolder.setItemIdsInFolder(  linkedList );

            userEquipmentFolderRepository.save( userEquipmentFolder );

            sendEquipPad( equipmentId , prepareUpdateAll() );

        }

    //ImageDataDelete
        //updateNameBoth
        //System.out.println( message+"__"+ userId +"__"+ sessionId);


    }

    public void updateEquipmentBoard(  String equipmentId ) throws JSONException, JsonProcessingException {

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("type", "updateBoard" );

        // here collect all the equiment information send to back side?
            // I think only need send the currect Image Data

        EquipmentData equipmentData = equipmentDataRepository.getById(equipmentId);
        String currentImageId =  equipmentData.getCurrentImageIds();

        if( currentImageId.equals("")==false ){

            ObjectMapper jsonMapper = new ObjectMapper();
            ImageData imageData = imageDataRepository.getById( currentImageId );
            String imageDataStr =  jsonMapper.writeValueAsString( imageData );
            //JSONObject jsonObject = new JSONObject();
            //jsonObject.put( "ImageData", imageDataStr );

            jsonObject2.put( "data", imageDataStr );
            sendEquipBoard( equipmentId , jsonObject2.toString() );

        }else{
            sendEquipBoard( equipmentId , jsonObject2.toString() );
        }


    }

    public String duplicateEquipment( String equipmentId ){

        EquipmentData equipmentData = equipmentDataRepository.getById( equipmentId );
        equipmentData.setId(null);
        //equipmentData.setCurrentImageIds(null);
        LinkedList<String>  linkedList = equipmentData.getImageIds();
        LinkedList<String>  putlinkedList = new LinkedList<>();
        for( int i = 0 ; i< linkedList.size() ;i++ ){

            ImageData imageData = imageDataRepository.getById( linkedList.get(i) );
            String imageBlobId = imageData.getBlobId();
            incrementImageBlobCount( imageBlobId );
            imageData.setId(null);
            String newImageDataId = imageDataRepository.save( imageData ).getId();
            putlinkedList.add( newImageDataId );

        }
        equipmentData.setImageIds( putlinkedList );
        String newEquipmentDataIs =  equipmentDataRepository.save(equipmentData).getId();
        return newEquipmentDataIs;

    }

    public void incrementImageBlobCount( String imageBlobId ){
        ImageBlob imageBlob =  imageBlobRepository.getById(imageBlobId);
        imageBlob.setCounter( imageBlob.getCounter()+1 );
        imageBlobRepository.save(imageBlob);
    }

    public void decreamentImageBlobCount(  String imageBlobId  ){

        ImageBlob imageBlob =  imageBlobRepository.getById(imageBlobId);
        int counter = imageBlob.getCounter() -1;
        if( counter <= 0 ){
            imageBlobRepository.deleteById(imageBlobId);
        }else{
            imageBlob.setCounter(counter);
            imageBlobRepository.save(imageBlob);
        }

    }

    public void UpdateTheEquipment( String equipmentId , String userId ) throws JSONException, JsonProcessingException {

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("type", "loadEquipment");
        jsonObject2.put( "itemId" , equipmentId );
        jsonObject2.put("data", prepareAllEquipment( equipmentId ) );
        //messagingTemplate.convertAndSend("/topic/equipment/pad/" + equipmentId, jsonObject2.toString() );
        sendEquipPad( equipmentId , jsonObject2.toString() );

    }

    //System.out.println( imageBlobId );
//            System.out.println( imageBlob.getCounter() );
//            System.out.println( imageBlob.getOriginalHeight() );
//            System.out.println( imageBlob.getOriginalWidth() );
//            System.out.println( imageBlob.getBlob() );

//            String equipmentId = jsonObject.get("itemId").toString();
//            EquipmentData equipmentData = equipmentDataRepository.getById(equipmentId);

    //System.out.println(imageBlobId);


    // System.out.println(equipmentData.getId());

//            equipmentData.getImageIds().add(imageDataId);
//            equipmentDataRepository.save( equipmentData );
//

    //

    @MessageMapping("/equipment/back/{userId}/{sessionId}")
    public void handleEquipmentBack(@DestinationVariable String userId , @DestinationVariable String sessionId , String message) throws JSONException, JsonProcessingException {


        // start working on back side right now

//        System.out.println(message);

        JSONObject jsonObject = new JSONObject(message);
        String type = jsonObject.get("type").toString();
        if( type.equals("connectSuccess") ){
            //messagingTemplate.convertAndSend("/topic/image/" + userId + "/" + sessionId, jsonObject.toString() );
            sendUnique( userId , sessionId , jsonObject.toString() );



            //updateEquipmentBoard(   equipmentId );

            return;
        }


        String equipmentId = jsonObject.get("itemId").toString();
        //JSONObject dataObject = (JSONObject) jsonObject.get("data");

        if( type.equals("mousemove") ){

            sendEquipBoard(equipmentId , message );
            //messagingTemplate.convertAndSend("/topic/equipment/board/" + equipmentId, message );

        }else if( type.equals("mouseleave") ){


            sendEquipBoard(equipmentId , message );
            //messagingTemplate.convertAndSend("/topic/equipment/board/" + equipmentId, message );
        }else if( type.equals("requestImage") ){
            String imageBlobId =  jsonObject.get("data").toString();
            ObjectMapper jsonMapper = new ObjectMapper();
            ImageBlob imageBlob = imageBlobRepository.getById( imageBlobId );
            String imageBlobStr =  jsonMapper.writeValueAsString( imageBlob );
            jsonObject.put( "data", imageBlobStr );
            sendUnique( userId , sessionId , jsonObject.toString() );
        }else if (type.equals("ImageDataMove") ){
             JSONObject jsonObject1 =  (JSONObject) jsonObject.get("data");
             String imageDataId =   jsonObject1.get("imageData").toString();
             ImageData imageData =  imageDataRepository.getById(imageDataId);
             Position position = new Position();
             position.setX((int) Double.parseDouble(jsonObject1.get("x").toString()) );
             position.setY( (int) Double.parseDouble(jsonObject1.get("y").toString()) );
             imageData.setPosition( position );

             imageDataRepository.save(imageData);



             updateEquipmentBoard(   equipmentId );

//            if( jsonObject1 == null ){
//
//                System.out.println("this is null");
//
//            }else{
//                System.out.println(jsonObject1.toString());
//
//            }



            UpdateTheEquipment(  equipmentId ,  userId );

        }else if( type.equals("updateBoard") ){

            updateEquipmentBoard(   equipmentId );
        }


//
    }


//_______________________________________________________________________________________________________________________________________________________
//_______________________________________________________________________________________________________________________________________________________
//_______________________________________________________________________________________________________________________________________________________
//_______________________________________________________________________________________________________________________________________________________


    @MessageMapping("/lab/front/{userId}/{sessionId}")
    public void handleLabFront(@DestinationVariable String userId , @DestinationVariable String sessionId , String message ) throws JSONException, JsonProcessingException {

//        {"type":"connectSuccess"}
//        {"type":"likeLab","itemId":"5de1e3d65c9835c587c68dc9","data":true}
//        {"type":"shareLab","itemId":"5de1e3d65c9835c587c68dc9","data":true}
//        {"type":"duplicateLab","itemId":"5de1e3d65c9835c587c68dc9"}
//        {"type":"labName","itemId":"5de1e3d65c9835c587c68dc9","data":"1"}
//        {"type":"labDescription","itemId":"5de1e3d65c9835c587c68dc9","data":"2"}

        JSONObject data = new JSONObject(message);
        String type = data.get("type").toString();
        if( type.equals("connectSuccess") ){

            sendUnique( userId , sessionId , message );

        }else{
            String labId = data.get("itemId").toString();
            LabData labData = labDataRepository.getById(labId);
            ObjectMapper jsonMapper = new ObjectMapper();
            if(labData == null){
                return;
            }

            if( type.equals("likeLab") ){

                Boolean like = (Boolean) data.get("data");
                //System.out.println(like);
                labData.setFavourite(like);
                labDataRepository.save(labData);
                SendRefreshLab(  labId , userId );

            }else if ( type.equals("shareLab") ){

                Boolean share = (Boolean) data.get("data");
                //System.out.println(share);

                labData.setShared(share);
                labDataRepository.save(labData);
                SendRefreshLab(  labId , userId );
            }else if( type.equals("labName") ){

               String name = data.get("data").toString();
               labData.setName( name );
               labDataRepository.save(labData);
                SendRefreshLab(  labId , userId );
            }else if( type.equals("labDescription") ){

                String description = data.get("data").toString();
                labData.setDescription( description );
                labDataRepository.save( labData );
                SendRefreshLab(  labId , userId );
            }else if( type.equals("duplicateLab") ){



            }else if( type.equals("deleteLab") ){



            }else if( type.equals("refreshLab") ){

                // here we need get all the data

                String labDataStr =  jsonMapper.writeValueAsString( labData );
                JSONObject jsonObject = new JSONObject(labDataStr);
                data.put("data", jsonObject );
                //sendLabPad( labId , data.toString() );
                sendUnique( userId , sessionId , data.toString() );


            }else if( type.equals("refreshAllEquipment") ){


                JSONArray jsonArray = new JSONArray();
                List<EquipmentData> equipmentDataList = equipmentDataRepository.findAllByOwnerId(userId);
                for( int i = 0 ; i< equipmentDataList.size() ; i++ ){

                   String equipStr =  jsonMapper.writeValueAsString(equipmentDataList.get(i));

                   jsonArray.put(new JSONObject(equipStr));
                }

                data.put("data", jsonArray );

                sendUnique( userId , sessionId , data.toString() );

//                 List<EquipmentData> equipmentData =  equipmentDataRepository.findAllByOwnerId( userId );
//                 JSONArray jsonArray = new JSONArray();
//
//                 for( int i = 0 ; i < equipmentData.size() ;i++ ){
//
//                     jsonArray.put( new JSONObject(jsonMapper.writeValueAsString( equipmentData.get(i) )) );
//
//                 }
//
//                 data.put("data", jsonArray );
//
//                 sendLabFolder( "", "" );
            }




        }




    }

    public void SendRefreshLab( String labId , String userId ) throws JSONException, JsonProcessingException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "refreshLab" );
        LabData labData = labDataRepository.getById(labId);
        ObjectMapper jsonMapper = new ObjectMapper();
        String labDataStr =  jsonMapper.writeValueAsString( labData );
        JSONObject jsonlabData = new JSONObject(labDataStr);
        jsonObject.put("data", jsonlabData );
        jsonObject.put("itemId", labId );


        //System.out.println("what YY happened??");

        sendLabFolder( userId , jsonObject.toString() );

    }


    @RequestMapping( value="/lab/{labId}", method = RequestMethod.GET )
    public String getLabPage(Model model, @PathVariable("labId") String labId ,  HttpServletRequest request ){

        LabData labData = labDataRepository.getById(labId);

        if(labData!= null){
            return "/lab/createlab";
        }else{
            return "lab/test";
        }

    }



     @RequestMapping(value="/ajax/uploadImage" , method = RequestMethod.POST)
     @ResponseBody
     public Object UploadImage(@Valid @RequestBody String reqBody, HttpServletRequest request) throws JSONException, JsonProcessingException {

        // String dataStr = request.getParameter("data");

         //System.out.println( reqBody );

         JSONObject jsonObject = new JSONObject(reqBody);
         String blob = jsonObject.get("coverImageData").toString();
         String labId = jsonObject.get( "labId" ).toString();
         //System.out.println( imageBlob );
         LabData labData = labDataRepository.getById(labId);
         ImageBlob imageBlob = new ImageBlob();
         imageBlob.setBlob(blob);
         String imageBlobId =  imageBlobRepository.save(imageBlob).getId();
         labData.setCoverBlobId( imageBlobId );

         labDataRepository.save(labData);

         SendRefreshLab( labId , "weixin.tang@stonybrook.edu" );

         Map<String,Object> sendData = new HashMap<>();
         sendData.put("success",true);
         sendData.put("data", "weixin.tang@stonybrook.edu" );
         return sendData;
     }

     @Autowired
     private LabDataRepository labDataRepository;

    @RequestMapping(value="/ajax/createNewLab" , method = RequestMethod.POST)
    @ResponseBody
    public Object CreateNewLab(@Valid @RequestBody String reqBody, HttpServletRequest request) throws JSONException {


        LabData labData = new LabData();
        labData.setOwnerId("weixin.tang@stonybrook.edu");
        String labId =  labDataRepository.save(labData).getId();

        Map<String,Object> sendData = new HashMap<>();
        sendData.put("success",true);
        sendData.put("labId", labId );
        return sendData;
    }

//    @RequestMapping(value="/ajax/refreshPage" , method = RequestMethod.POST)
//    @ResponseBody
//    public Object refreshPage(@Valid @RequestBody String reqBody, HttpServletRequest request) throws JSONException {
//
//
//
//        // just refresh everything make it as simple as possible
//
//
//        Map<String,Object> sendData = new HashMap<>();
//        sendData.put("success",true);
//        sendData.put("data", "weixin.tang@stonybrook.edu" );
//        return sendData;
//    }

        @RequestMapping(value="/ajax/downloadImages" , method = RequestMethod.POST)
    @ResponseBody
    public Object DownloadImages(@Valid @RequestBody String reqBody, HttpServletRequest request) throws JSONException, JsonProcessingException {

        //System.out.println(reqBody);



        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject jsonObject = new JSONObject(reqBody);
        JSONArray blobIdArr = (JSONArray) jsonObject.get("blobIds");

        JSONArray blobDataArr = new JSONArray();

        for( int i = 0 ; i<  blobIdArr.length() ; i++ ){

           String blobId = blobIdArr.get(i).toString();
            ImageBlob imageBlob = imageBlobRepository.getById(blobId);
            String imagBlobStr = objectMapper.writeValueAsString(imageBlob);
            JSONObject jsonObject1 =new JSONObject(imagBlobStr);
            blobDataArr.put( imagBlobStr );

        }



        Map<String,Object> sendData = new HashMap<>();
        sendData.put("success",true);
        sendData.put("data", blobDataArr.toString() );



        return sendData;
    }

//_______________________________________________________________________________________________________________________________________________________
//_______________________________________________________________________________________________________________________________________________________
//_______________________________________________________________________________________________________________________________________________________





    @MessageMapping("/lab/back/{userId}/{sessionId}")
    public void handleLabBack(@DestinationVariable String userId , @DestinationVariable String sessionId , String message) throws JSONException {


        //


    }

    @Autowired
    private ImageDataRepository imageDataRepository;



    public String prepareUpdateAll() throws JSONException, JsonProcessingException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "loadAll");

        return jsonObject.toString();
    }

    public JSONArray prepareAllEquipment( String equipmentId ) throws JSONException, JsonProcessingException {
        JSONArray jsonArray = new JSONArray();
        ObjectMapper objectMapper = new ObjectMapper();

        EquipmentData equipmentData = equipmentDataRepository.getById( equipmentId );

        if( equipmentData == null ){
            return jsonArray;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put( "EquipmentData" , objectMapper.writeValueAsString(equipmentData) );
        jsonArray.put( jsonObject );

        LinkedList<String> linkedList = equipmentData.getImageIds();

        if( linkedList != null ){

            for( int i = 0 ; i < linkedList.size() ; i++ ){
                ImageData imageData = imageDataRepository.getById( linkedList.get(i) );
                jsonObject = new JSONObject();
                jsonObject.put( "ImageData" , objectMapper.writeValueAsString( imageData ) );
                jsonArray.put(jsonObject);
            }


        }



        return jsonArray;
    }


    public JSONArray prepareAllFolder( String userId ) throws JSONException, JsonProcessingException {
        // here we will prepare all information except images
        ObjectMapper objectMapper = new ObjectMapper();
        JSONArray jsonArray = new JSONArray();
        UserEquipment userEquipment =  userEquipmentRepository.findByUserId(userId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "UserEquipment");
        jsonObject.put("data", objectMapper.writeValueAsString(userEquipment));
        jsonArray.put( jsonObject );

        List<UserEquipmentFolder> userEquipmentFolders = userEquipmentFolderRepository.findAllByOwner(userId);

        for( int  i = 0 ; i < userEquipmentFolders.size() ; i++ ){
            jsonObject = new JSONObject();
            jsonObject.put("type", "UserEquipmentFolder");
            jsonObject.put( "data", objectMapper.writeValueAsString( userEquipmentFolders.get(i) ) );
            jsonArray.put( jsonObject );
        }
        // here we will create all share and favourite

        List<EquipmentData> equipmentDatas = equipmentDataRepository.findAllByOwnerId( userId );

        for( int i = 0 ; i < equipmentDatas.size() ; i++ ){
            jsonObject = new JSONObject();
            jsonObject.put("type", "EquipmentData");
            jsonObject.put( "data", objectMapper.writeValueAsString( equipmentDatas.get(i) ) );
            jsonArray.put( jsonObject );
        }

        return jsonArray;
    }


    public void sendUnique(String userId , String sessionId , String message ){
        messagingTemplate.convertAndSend("/topic/image/" + userId + "/" + sessionId, message );
    }

    public void sendEquipFolder( String userId , String message ){
        messagingTemplate.convertAndSend("/topic/equipment/folder/" + userId , message );
    }

    public void sendEquipPad( String itemId ,  String message ){
        messagingTemplate.convertAndSend("/topic/equipment/pad/" + itemId , message );
    }

    public void sendEquipBoard( String itemId ,  String message ){
        messagingTemplate.convertAndSend("/topic/equipment/board/" + itemId , message );
    }

    public void sendEquipFind( String message ){
        messagingTemplate.convertAndSend("/topic/equipment/find"  , message );
    }

    public void sendLabFolder( String userId , String message ){

        messagingTemplate.convertAndSend("/topic/lab/folder/" + userId , message );
    }

    public void sendLabPad( String itemId ,  String message ){
        messagingTemplate.convertAndSend("/topic/lab/pad/" + itemId , message );
    }

    public void sendLabBoard( String itemId ,  String message ){
        messagingTemplate.convertAndSend("/topic/lab/board/" + itemId , message );
    }

    public void sendLabFind( String message ){
        messagingTemplate.convertAndSend("/topic/lab/find"  , message );
    }


}


//@SubscribeMapping("/image/{userId}/{sessionId}")
//    public void initialReply( @DestinationVariable String userId ,  @DestinationVariable String sessionId  ) throws Exception {
//
//        //return "Welcome to the chat room.";
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("type", "connect_success");
//
//        System.out.println("test123");
//        messagingTemplate.convertAndSend("/topic/image/" + userId + "/" + sessionId, jsonObject.toString() );
//        //return jsonObject.toString();
//    }

//    @RequestMapping(value = "/equipment", method = RequestMethod.GET)
//    public String jumpEquipment(Model model, HttpServletRequest request) {
//
////        if( request.getSession().getAttribute("email")!=null){
////
////            model.addAttribute( "email", request.getSession().getAttribute("email") );
////
////            return "equipment";
////
////        }else{
////
////            return "welcome";
////
////        }
//
//        model.addAttribute("email", "weixin.tang@stonybrook.edu");
//
//        return "equipment";
//
//    }

//    @RequestMapping(value = "/equipmentPane2.html", method = RequestMethod.GET)
//    public String getEquipmentPad(Model model, HttpServletRequest request) {
//
//
//        return "equipmentPane2";
//    }
 /*
    // here is ajax to get username
    @RequestMapping(value="/ajax/equipmentRequestUserId" , method = RequestMethod.POST)
    @ResponseBody
    public Object SignupSumbit(HttpServletRequest request) throws JSONException {

        //HttpServletRequest 为javax.servlet.http包下
        //String emailStr = request.getParameter("email");
        //String schoolStr = request.getParameter("school");
        //System.out.println(
        //        emailStr+"__"+passwordStr
        //);
        //User user = new User();
        //user.setSchool( schoolStr );
        //user.setEmail( emailStr );
        //userRepository.save( user );

        //request.getSession().setAttribute("email", emailStr );

        UserEquipment userEquipment =  userEquipmentRepository.findByUserId("weixin.tang@stonybrook.edu");
        if( userEquipment == null ){

            // here we need create one

            UserEquipmentFolder allFolder = new UserEquipmentFolder();
            allFolder.setFolderName("all");

            String allFolderId  = userEquipmentFolderRepository.save(allFolder).getUserEquipmentFolderId();

            UserEquipmentFolder favouriteFolder = new UserEquipmentFolder();
            favouriteFolder.setFolderName("favourite");

            String favouriteFolderId  = userEquipmentFolderRepository.save(favouriteFolder).getUserEquipmentFolderId();

            UserEquipmentFolder shareFolder = new UserEquipmentFolder();
            shareFolder.setFolderName("share");

            String shareFolderId  = userEquipmentFolderRepository.save(shareFolder).getUserEquipmentFolderId();

            userEquipment = new UserEquipment();
            userEquipment.setUserId("weixin.tang@stonybrook.edu");


            LinkedList<String> equipmentFolderIds = new LinkedList<>();
            equipmentFolderIds.add(allFolderId);
            equipmentFolderIds.add(favouriteFolderId);
            equipmentFolderIds.add(shareFolderId);

            userEquipment.setEquipmentFolderIds( equipmentFolderIds );

            userEquipmentRepository.save(userEquipment);

        }


        Map<String,Object> sendData = new HashMap<>();
        sendData.put("success",true);
        sendData.put("passport", "weixin.tang@stonybrook.edu" );
        return sendData;



    }
*/


/*
    @MessageMapping("/equipment/board/{cookieId}/{equipmentId}")
    public void handleEquipmentBoard( @DestinationVariable String cookieId, @DestinationVariable String equipmentId, String message) throws JSONException {

        //System.out.println("the cookieId is "+cookieId);
        //System.out.println("the Mssage is "+message);
//        UsersEquipment usersEquipment = UsersEquipment.getUsersEquipment();
//        UserEquipment userEquipment = usersEquipment.getUserEquipment(cookieId);
//        EquipmentData equipmentData = userEquipment.getEquipmentData(equipmentId);
        //equipmentData.

        JSONObject jsonObject = new JSONObject(message);

        // we what this be hash
        // not for loop check
        //




        if( jsonObject.get( "type" ).toString().equals("mousemove") ){
            messagingTemplate.convertAndSend("/topic/equipment/board/" + cookieId + "/" + equipmentId, message);
            return;
        }
        else if( jsonObject.get( "type" ).toString().equals("hideMouseCursor") ){
            // some other command here
            messagingTemplate.convertAndSend("/topic/equipment/board/" + cookieId + "/" + equipmentId, message);
            return;
        }
        else if( jsonObject.get( "type" ).toString().equals("requestImage") ){
            // some other command here
            //messagingTemplate.convertAndSend("/topic/equipment/board/" + cookieId + "/" + equipmentId, message);

            return;
        }

        //messagingTemplate.convertAndSend("/topic/equipment/board/" + cookieId + "/" + equipmentId, message);


    }
*/


/*
    @MessageMapping("/equipment/pad/{cookieId}/{equipmentId}")
    public void handleEquipmentPad( @DestinationVariable String cookieId  ,@DestinationVariable String equipmentId, String message) throws JSONException, JsonProcessingException {


        // here need to read
        // read the scroll pad and name input
//        UsersEquipment usersEquipment = UsersEquipment.getUsersEquipment();
//        UserEquipment userEquipment = usersEquipment.getUserEquipment(cookieId);
//        EquipmentData equipmentData = userEquipment.getEquipmentData(equipmentId);

//        var sendData = {};
//        sendData["type"] = "uploadCoverImage";
//        sendData["data"] = ImageData;
//
//        sendPadMessage( JSON.stringify(sendData) );

        JSONObject jsonObject = new JSONObject(message);

        if( jsonObject.get( "type" ).toString().equals("uploadCoverImage") ){

            ImageBlob imageBlob = new ImageBlob();
            imageBlob.setBlob(jsonObject.get( "data" ).toString());
            String imagblobId = imageBlobRepository.save( imageBlob ).getId();

            EquipmentData equipmentData = equipmentDataRepository.getById(equipmentId);
            equipmentData.setCoverBlobId(imagblobId);
            equipmentDataRepository.save(equipmentData);

            jsonObject = new JSONObject();
            jsonObject.put("type", "refreshfolder");
            messagingTemplate.convertAndSend("/topic/equipment/pad/" + cookieId + "/" + equipmentId, jsonObject.toString());

            jsonObject = new JSONObject();
            jsonObject.put("type", "refreshEquipment");
            messagingTemplate.convertAndSend("/topic/equipment/pad/" + cookieId + "/" + equipmentId, jsonObject.toString());


            return;

        }else if( jsonObject.get( "type" ).toString().equals("loadEquipment") ){

            EquipmentData equipmentData = equipmentDataRepository.getById(equipmentId);



            ObjectMapper objectMapper = new ObjectMapper();
            String equipStr = objectMapper.writeValueAsString( equipmentData );
            jsonObject.put("data", equipStr);
            messagingTemplate.convertAndSend("/topic/equipment/pad/" + cookieId + "/" + equipmentId, jsonObject.toString());

        }else if( jsonObject.get( "type" ).toString().equals("equipmentName") ){

            String equipmentName =  jsonObject.get("value").toString();

            EquipmentData equipmentData = equipmentDataRepository.getById(equipmentId);
            equipmentData.setEquipmentName(equipmentName);
            equipmentDataRepository.save(equipmentData);

            jsonObject = new JSONObject();
            jsonObject.put("type", "refreshfolder");
            messagingTemplate.convertAndSend("/topic/equipment/pad/" + cookieId + "/" + equipmentId, jsonObject.toString());

            jsonObject = new JSONObject();
            jsonObject.put("type", "refreshEquipment");
            messagingTemplate.convertAndSend("/topic/equipment/pad/" + cookieId + "/" + equipmentId, jsonObject.toString());

        }else if( jsonObject.get( "type" ).toString().equals("equipmentDescription") ){

            String equipmentDescription =  jsonObject.get("value").toString();

            EquipmentData equipmentData = equipmentDataRepository.getById(equipmentId);
            equipmentData.setDescription(equipmentDescription);
            equipmentDataRepository.save(equipmentData);

            jsonObject = new JSONObject();
            jsonObject.put("type", "refreshfolder");
            messagingTemplate.convertAndSend("/topic/equipment/pad/" + cookieId + "/" + equipmentId, jsonObject.toString());

            jsonObject = new JSONObject();
            jsonObject.put("type", "refreshEquipment");
            messagingTemplate.convertAndSend("/topic/equipment/pad/" + cookieId + "/" + equipmentId, jsonObject.toString());

        }

        //    equipmentName

        //

        //loadImageAndLoadEquipment

        //System.out.println( message );


        //messagingTemplate.convertAndSend("/topic/equipment/pad/"+cookieId + "/" + equipmentId, message);

    }
*/
//messagingTemplate.convertAndSend("/topic/equipment", message );
// we call this super broadcast
//

    /*
    @MessageMapping("/equipment/image/{cookieId}/{sessionId}")
    public void handleEquipmentImage( @DestinationVariable String cookieId  ,@DestinationVariable String sessionId, String message) throws JSONException {


        // here need to read
        // read the scroll pad and name input
//        UsersEquipment usersEquipment = UsersEquipment.getUsersEquipment();
//        UserEquipment userEquipment = usersEquipment.getUserEquipment(cookieId);
//        EquipmentData equipmentData = userEquipment.getEquipmentData(equipmentId);

        JSONObject jsonObject = new JSONObject(message);


        if( jsonObject.get( "type" ).toString().equals("AllFolder") ){


            jsonObject.put("data",  prepareAllFolder( cookieId ) ) ;

            messagingTemplate.convertAndSend("/topic/equipment/image/"+cookieId + "/" + sessionId, jsonObject.toString());
            return;

        }else if( jsonObject.get( "type" ).toString().equals("loadImagesThenloadWindow") ){


            jsonObject.put("data",   prepareAllImages((JSONArray)jsonObject.get("imageIds")  ) )  ;

            messagingTemplate.convertAndSend("/topic/equipment/image/"+cookieId + "/" + sessionId, jsonObject.toString());
        }else if( jsonObject.get( "type" ).toString().equals("loadImageAndLoadEquipment") ){


            ImageBlob imageBlob = imageBlobRepository.getById( jsonObject.get("blobId").toString() );

            jsonObject.put("data", imageBlob.toString());

            messagingTemplate.convertAndSend("/topic/equipment/image/"+cookieId + "/" + sessionId, jsonObject.toString());

        }


//loadImageAndLoadEquipment
//loadImagesThenloadWindow

            //System.out.println( message );


        //messagingTemplate.convertAndSend("/topic/equipment/image/"+cookieId + "/" + sessionId, message);

    }
*/