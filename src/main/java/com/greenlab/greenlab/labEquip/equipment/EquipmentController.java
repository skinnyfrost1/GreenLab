package com.greenlab.greenlab.labEquip.equipment;


//import green.lab.equipment.equipmentData.notUse.EquipmentData;
//import green.lab.equipment.equipmentData.notUse.UserEquipment;
//import green.lab.equipment.equipmentData.notUse.UsersEquipment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.json.JsonMapper;

import com.greenlab.greenlab.dto.SingleStringRequestBody;
import com.greenlab.greenlab.lab.Steps;
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
import com.greenlab.greenlab.labEquip.laboratory.labData.*;
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
            LinkedList<String> linkedList =  equipmentData.getImageIds();

            linkedList.remove( selectImageDataId );
            if( equipmentData.getCurrentImageIds().equals( selectImageDataId ) ){
                if( linkedList.size()>0 ){
                    equipmentData.setCurrentImageIds( linkedList.get(0) );
                }else{
                    equipmentData.setCurrentImageIds("");
                }
                //websocket.sendLabBoard();
                //websocket.
            }

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
            String[] receiveArr  = receiveText.split(" ");


            ImageData imageData = imageDataRepository.getById( imageDataId );
            LinkedList<String> linkedList =  imageData.getReceiveText();
            linkedList.clear();

            for (String str : receiveArr){
                if(str.equals("")==false){
                    linkedList.add( str );
                }

            }

            imageData.setReceiveText(linkedList);
            imageDataRepository.save( imageData );

            UpdateTheEquipment(  equipmentId ,  userId );

            //System.out.println(  );

        }else if( jsonObject.get("type").toString().equals("ImageDataSendText") ) {


            String equipmentId = jsonObject.get("itemId").toString();
            JSONObject jsonObject1 =  (JSONObject)jsonObject.get("data");
            String imageDataId = jsonObject1.get("imageData").toString();
            String receiveText = jsonObject1.get("SendText").toString();
            String[] receiveArr  = receiveText.split(" ");

            ImageData imageData = imageDataRepository.getById( imageDataId );
            LinkedList<String> linkedList =  imageData.getSendText();
            linkedList.clear();
            for (String str : receiveArr){
                if(str.equals("")==false){
                    linkedList.add( str );
                }
            }
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


    @RequestMapping(value="/ajax/uploadEquipStatusImage" , method = RequestMethod.POST)
    @ResponseBody
    public Object uploadEquipStatusImage(@Valid @RequestBody String reqBody, HttpServletRequest request) throws JSONException, JsonProcessingException {

        // String dataStr = request.getParameter("data");

        //System.out.println( reqBody );

        JSONObject jsonObject = new JSONObject(reqBody);
        String userId = jsonObject.get("userId").toString();
        String equipmentId = jsonObject.get( "equipId" ).toString();
        String imageDataStr = jsonObject.get( "imageData" ).toString();
        Integer width = Integer.parseInt(jsonObject.get("width").toString());
        Integer height = Integer.parseInt(jsonObject.get("height").toString());

        ImageBlob imageBlob = new ImageBlob();
        imageBlob.setBlob(imageDataStr);
        imageBlob.setOriginalHeight( height );
        imageBlob.setOriginalWidth( width );
        imageBlob.setCounter(1);
        String imageBlobId =  imageBlobRepository.save(imageBlob).getId();
        ImageData imageData = new ImageData();
        imageData.setBlobId( imageBlobId );
        String imageDataId =  imageDataRepository.save( imageData ).getId();
        EquipmentData equipmentData = equipmentDataRepository.getById(equipmentId);
        equipmentData.getImageIds().add(imageDataId);
        equipmentData.setCurrentImageIds( imageDataId );
        equipmentDataRepository.save( equipmentData );
        UpdateTheEquipment(  equipmentId ,  userId );
        updateEquipmentBoard(  equipmentId );


        Map<String,Object> sendData = new HashMap<>();
        sendData.put("success",true);

        //sendData.put("data", "weixin.tang@stonybrook.edu" );
        return sendData;
    }

    @RequestMapping(value="/ajax/uploadEquipCoverImage" , method = RequestMethod.POST)
    @ResponseBody
    public Object uploadEquipCoverImage(@Valid @RequestBody String reqBody, HttpServletRequest request) throws JSONException, JsonProcessingException {

        // String dataStr = request.getParameter("data");

        //System.out.println( reqBody );

        JSONObject jsonObject = new JSONObject(reqBody);
        String userId = jsonObject.get("userId").toString();
        String equipmentId = jsonObject.get( "equipId" ).toString();
        String imageDataStr = jsonObject.get( "imageData" ).toString();

        //System.out.println(equipmentId);

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
        sendEquipFolder( userId , prepareUpdateAll() );

        //System.out.println("6666");
        //System.out.println( imageBlob );
        //        LabData labData = labDataRepository.getById(labId);
        //        ImageBlob imageBlob = new ImageBlob();
        //        imageBlob.setBlob(blob);
        //        String imageBlobId =  imageBlobRepository.save(imageBlob).getId();
        //        labData.setCoverBlobId( imageBlobId );
        //
        //        labDataRepository.save(labData);
        //
        //        SendRefreshLab( labId , "weixin.tang@stonybrook.edu" );

        Map<String,Object> sendData = new HashMap<>();
        sendData.put("success",true);

        //sendData.put("data", "weixin.tang@stonybrook.edu" );
        return sendData;
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
//_______________________________________________________________________________________________________________________________________________________ below lab front
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

            }
            else if ( type.equals("shareLab") ){

                Boolean share = (Boolean) data.get("data");
                //System.out.println(share);

                labData.setShared(share);
                labDataRepository.save(labData);
                SendRefreshLab(  labId , userId );
            }
            else if( type.equals("labName") ){

               String name = data.get("data").toString();
               labData.setName( name );
               labDataRepository.save(labData);
                SendRefreshLab(  labId , userId );
            }
            else if( type.equals("labDescription") ){

                String description = data.get("data").toString();
                labData.setDescription( description );
                labDataRepository.save( labData );
                SendRefreshLab(  labId , userId );
            }
            else if( type.equals("duplicateLab") ){

            }
            else if( type.equals("deleteLab") ){

            }
            else if( type.equals("refreshLab") ){

                // here we need get all the data
                String labDataStr =  jsonMapper.writeValueAsString( labData );
                JSONObject jsonObject = new JSONObject(labDataStr);
                data.put("data", jsonObject );
                //sendLabPad( labId , data.toString() );
                sendUnique( userId , sessionId , data.toString() );


            }
            else if( type.equals("refreshAllEquipment") ){


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
            else if( type.equals("labEquipList") ){

                try{

                    JSONArray jsonArray = (JSONArray)data.get("data");



                    List<String> list = new LinkedList<>();
                    for( int i = 0 ; i< jsonArray.length(); i++ ){

                        list.add( jsonArray.getString(i) );

                    }

                    labData.setLabEquipDataList( list );
                    labDataRepository.save( labData );


                    JSONArray equipArr = new JSONArray();

                    for( int i = 0 ; i< jsonArray.length(); i++ ){

                        String equipId = jsonArray.getString(i);

                        EquipmentData equipmentData = equipmentDataRepository.getById(equipId);
                        equipArr.put( jsonMapper.writeValueAsString( equipmentData ) );

                    }



                    data.put( "data", equipArr );
                    sendLabPad( labId , data.toString() );
                    //System.out.println("666");

                }catch (Exception e){

                    //System.out.println("686");

                    List<String> list = labData.getLabEquipDataList();

                    JSONArray equipArr = new JSONArray();

                    for( int i = 0 ; i< list.size(); i++ ){

                        String equipId = list.get(i);
                        EquipmentData equipmentData = equipmentDataRepository.getById(equipId);
                        equipArr.put( jsonMapper.writeValueAsString( equipmentData ) );

                    }

                    data.put( "data", equipArr );
                    sendLabPad( labId , data.toString() );
                }




            }
            else if( type.equals("addEquipToBoard")  ){

                List< LabStep> labSteps = labData.getLabSteps();
                if( labSteps.size() == 0 ){
                    labSteps.add( new LabStep() );
                }
                labData.setLabSteps( labSteps );
                labData.setCurrentLabStep(0);
                //private int currentLabStep;
                //
                //private List<String> labEquipDataList;
                //private List<LabStep> labSteps;
                //private List<LabEquipData> usedEquipList;

                //private int id;
                //private String equipmentDataStr;
                //private List<String> imageDataStrArr;

                //_______________________________________________________________________ the code to create labEquipData
                String equipId = data.get("data").toString();
                EquipmentData equipmentData =  equipmentDataRepository.getById(equipId);
                String equipmentDataStr = jsonMapper.writeValueAsString(equipmentData);
                LinkedList<String> imageIds = equipmentData.getImageIds();
                LabEquipData labEquipData = new LabEquipData();
                labEquipData.setEquipmentDataStr(equipmentDataStr);
                List<String> imageDataStrArr = new LinkedList<>();
                String fristStatusName = null;
                for( int i = 0 ; i < imageIds.size() ;i++ ){

                    String imageDataId = imageIds.get(i);
                    ImageData imageData =  imageDataRepository.getById(imageDataId);
                    if( i == 0 ){

                        fristStatusName = imageData.getName();


                    }
                    String imageDataStr =  jsonMapper.writeValueAsString(imageData);
                    imageDataStrArr.add(imageDataStr);

                }

                if( fristStatusName == null ){
                    return;
                }

                labEquipData.setImageDataStrArr( imageDataStrArr );
                //_______________________________________________________________________
                //System.out.println( equipId );
                int currentLabStep = labData.getCurrentLabStep();

                //private List<LabEquipStatus> before;
                //private List<LabEquipStatus> current;
                //private List<LabEquipStatus> after;

                List<LabEquipData> usedEquipList = labData.getUsedEquipList();
                int usedEquipListSize = usedEquipList.size();
                labEquipData.setId(usedEquipListSize);  // so if empty should be 0
                usedEquipList.add(labEquipData);
                labData.setUsedEquipList( usedEquipList );  // <--------------- set equip list

                //List<LabStep> labSteps =  labData.getLabSteps();
                LabStep labStep =  labSteps.get( currentLabStep );
                List<LabEquipStatus> current = labStep.getCurrent( );

                LabEquipStatus labEquipStatus = new LabEquipStatus();
                labEquipStatus.setLabEquipDataId(usedEquipListSize);
                labEquipStatus.setCurrentStatus( fristStatusName );
                labEquipStatus.setX(20);
                labEquipStatus.setY(20);

                current.add(labEquipStatus);
                labStep.setCurrent( current );
                labSteps.set(currentLabStep , labStep  );
                labData.setLabSteps( labSteps );  // <--------------- set the first step

                labData = labDataRepository.save( labData );

                //jsonMapper.writeValueAsString(labData);

                // now we need send to the front side

                data.put("type", "refreshBoard" );
                data.put( "data" , jsonMapper.writeValueAsString( labData ) );

                sendLabBoard( labId , data.toString() );

                //labEquipStatus.

                //current.add( )

                //private int labEquipDataId;
                //private String currentStatus;
                //private int x;
                //private int y;

                //LabEquipData labEquipData = new LabEquipData();
                //labEquipData.setEquipmentDataStr();




            }
            //____________________________________________________________ the code below from step
            else if( type.equals("newStep")  ){




                    System.out.println( "newStep received" );


//removeLastStep
            }
            else if( type.equals("removeLastStep")  ){

                    System.out.println( "removeLastStep received" );
//
//                data.put("type", "refreshBoard" );
//                data.put( "data" , jsonMapper.writeValueAsString( labData ) );
//                sendLabBoard( labId , data.toString() );

            }
            else if( type.equals("stepInfo")  ){

                //Integer index = data.getInt("data");
                JSONObject receiveData = (JSONObject) data.get("data");
                //System.out.println( "stepInfo received" );
                Integer stepId =  receiveData.getInt("id");
                String info = receiveData.getString("info");

                List<LabStep> labSteps = labData.getLabSteps();
                LabStep labStep =  labSteps.get( stepId );
                labStep.setInfo( info );
                labSteps.set( stepId , labStep );
                labData.setLabSteps( labSteps );
                labDataRepository.save( labData );
                //data.put("type", "refreshBoard" );
                //data.put( "data" , jsonMapper.writeValueAsString( labData ) );
                //sendLabBoard( labId , data.toString() );
                //SendRefreshLab(  labId , userId );

                // this will be different again

                //data = new JSONObject();
                //data.put("type","stepInfo" );

                //System.out.println(data.toString()  );
                //System.out.println("6666");

                sendLabPad( labId , data.toString() );


                //sendLabPad( labId , data.toString() );
            }
            else if( type.equals("saveBefore")  ){

                Integer index = data.getInt("data");

                System.out.println( "saveBefore received" );







//                data.put("type", "refreshBoard" );
//                data.put( "data" , jsonMapper.writeValueAsString( labData ) );
//                sendLabBoard( labId , data.toString() );

            }
            else if( type.equals("saveEnd")  ){

                Integer index = data.getInt("data");
                System.out.println( "saveEnd received" );






//                data.put("type", "refreshBoard" );
//                data.put( "data" , jsonMapper.writeValueAsString( labData ) );
//                sendLabBoard( labId , data.toString() );

            }
            else if( type.equals("displayBefore")  ){

                Integer index = data.getInt("data");
                System.out.println( "displayBefore received" );







//                data.put("type", "refreshBoard" );
//                data.put( "data" , jsonMapper.writeValueAsString( labData ) );
//                sendLabBoard( labId , data.toString() );

            }
            else if( type.equals("displayEnd")  ){
                Integer index = data.getInt("data");

                System.out.println( "displayEnd received" );

            }
            else if( type.equals("insertStepBelow")  ){
                Integer index = data.getInt("data");


                System.out.println( "insertStepBelow received" );
            }
            else if( type.equals("removeThisStep")  ){

                Integer index = data.getInt("data");

                System.out.println( "removeThisStep received" );
            }
            else if( type.equals("expandStep")  ){

                Integer index = data.getInt("data");

                System.out.println( "expandStep received" );
            }


            //expandStep

            //stepBefore
            //stepInfo
//addEquipToBoard


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


    @RequestMapping( value="/editmylab/{labId}", method = RequestMethod.GET )
    public String getLabPage(Model model, @PathVariable("labId") String labId ,  HttpServletRequest request ){

        LabData labData = labDataRepository.getById(labId);
        //System.out.println("1_dfghjfgh");
        //System.out.println("2_dfghjfgh");
        //System.out.println("3_dfghjfgh");
        //System.out.println("4_dfghjfgh");

        if(labData!= null){
            return "lab/createlab";
        }else{
            return "lab/test";
        }

    }

    @RequestMapping( value="/editlabboard/{labId}", method = RequestMethod.GET )
    public String getLabBoardPage(Model model, @PathVariable("labId") String labId ,  HttpServletRequest request ){

        LabData labData = labDataRepository.getById(labId);



        if(labData!= null){
            return "lab/labboard";
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

    @RequestMapping(value="/ajax/downloadEquipments" , method = RequestMethod.POST)
    @ResponseBody
    public Object DownloadEquipments(@Valid @RequestBody String reqBody, HttpServletRequest request) throws JSONException, JsonProcessingException {

        //System.out.println(reqBody);



        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject jsonObject = new JSONObject(reqBody);


//        JSONArray blobIdArr = (JSONArray) jsonObject.get("blobIds");
//
//        JSONArray EquipDataArr = new JSONArray();
//
//        for( int i = 0 ; i<  blobIdArr.length() ; i++ ){
//
//            String blobId = blobIdArr.get(i).toString();
//            ImageBlob imageBlob = imageBlobRepository.getById(blobId);
//            String imagBlobStr = objectMapper.writeValueAsString(imageBlob);
//            JSONObject jsonObject1 =new JSONObject(imagBlobStr);
//            EquipDataArr.put( imagBlobStr );
//
//        }



        Map<String,Object> sendData = new HashMap<>();
        sendData.put("success",true);
        //sendData.put("data", blobDataArr.toString() );



        return sendData;
    }

//_______________________________________________________________________________________________________________________________________________________
//_______________________________________________________________________________________________________________________________________________________
//_______________________________________________________________________________________________________________________________________________________





    @MessageMapping("/lab/back/{userId}/{sessionId}")
    public void handleLabBack(@DestinationVariable String userId , @DestinationVariable String sessionId , String message) throws JSONException, JsonProcessingException {


        JSONObject data = new JSONObject(message);
        String type = data.get("type").toString();
        if( type.equals("connectSuccess") ){

            sendUnique( userId , sessionId , message );

        }else {
            String labId = data.get("itemId").toString();
            LabData labData = labDataRepository.getById(labId);
            ObjectMapper jsonMapper = new ObjectMapper();
            if (labData == null) {
                return;
            }
            if( type.equals("refreshBoard") ){

                refreshLabBoard(  data , labData ,  labId  ,  jsonMapper );

            }
            else if(type.equals("onMouseDown") ){
                sendLabBoard( labId , data.toString() );
            }
            else if(type.equals("onDrag") ){


                //System.out.println( "ondrag detected" );

                JSONObject receiveData = (JSONObject) data.get("data");

                Integer x  = Integer.parseInt(receiveData.getString( "x" ));
                Integer y  = Integer.parseInt( receiveData.getString( "y" ));
                Integer equipId =  Integer.parseInt(receiveData.getString("equipId"));
                String timeStamp = receiveData.getString("timeStamp");
                // now we need set the value in

                List<LabStep> labSteps =  labData.getLabSteps();

                LabStep labStep =  labSteps.get( labData.getCurrentLabStep() );
                List<LabEquipStatus> current =  labStep.getCurrent();
                for( int i = 0 ; i< current.size() ; i++ ){
                    LabEquipStatus labEquipStatus = current.get( i );
                    if( labEquipStatus.getLabEquipDataId() == equipId ){
                        current.remove( i );
                        labEquipStatus.setX( x  );
                        labEquipStatus.setY( y );
                        current.add( i , labEquipStatus );
                        break;
                    }
                }
                labSteps.set(labData.getCurrentLabStep() , labStep  );
                labData.setLabSteps( labSteps );


                //System.out.println(jsonMapper.writeValueAsString( labData ));

                labDataRepository.save( labData );
                JSONObject sendData = new JSONObject();
                sendData.put( "labData" , jsonMapper.writeValueAsString( labData ) );
                sendData.put( "websocketId" , sessionId );
                data.put("type", "refreshBoardExceptSelf" );
                data.put( "data" , sendData );
                sendData.put( "timeStamp", timeStamp );
                sendLabBoard( labId , data.toString() );
                data.put("type", "refreshBoardExceptSelf" );

            }
            else if(type.equals("onDrop") ){

                JSONObject receiveData = (JSONObject) data.get("data");
                Integer dragId =  receiveData.getInt("dragId");
                Integer dropId =  receiveData.getInt(  "dropId" );
                String dragName =  receiveData.getString("dragName");
                String dropName =  receiveData.getString("dropName");
//                System.out.println("dragId"+dragId.toString());
//                System.out.println("dropId"+dropId.toString());
//                System.out.println("dragName"+dragName);
//                System.out.println("dropName"+dropName);

                List<LabStep> labSteps =  labData.getLabSteps();
                LabStep labStep =  labSteps.get( labData.getCurrentLabStep() );
                List<LabEquipStatus> current =  labStep.getCurrent();
                for( int i = 0 ; i< current.size() ; i++ ){
                    LabEquipStatus labEquipStatus = current.get( i );
                    if( labEquipStatus.getLabEquipDataId() == dragId ){

                        if( dragName.equals("") == false ){
                            current.remove( i );
                            labEquipStatus.setCurrentStatus( dragName );
                            current.add( i , labEquipStatus );
                        }

                    }else if( labEquipStatus.getLabEquipDataId() == dropId ){

                        if( dropName.equals("") == false ){
                            current.remove( i );
                            labEquipStatus.setCurrentStatus( dropName );
                            current.add( i , labEquipStatus );
                        }
                    }
                }

                labStep.setCurrent( current );
                labSteps.set( labData.getCurrentLabStep() , labStep );
                labData.setLabSteps( labSteps );
                labDataRepository.save( labData );
                data.put("type", "refreshBoard" );
                data.put( "data" , jsonMapper.writeValueAsString( labData ) );
                sendLabBoard( labId , data.toString() );




//                // now we need get sendText from both equipment
//                List<LabEquipData> labEquipDataList =   labData.getUsedEquipList();
//
//
//
//                List<LabStep> labSteps =  labData.getLabSteps();
//                LabStep labStep =  labSteps.get( labData.getCurrentLabStep() );
//                List<LabEquipStatus> current =  labStep.getCurrent();
//
//                String dragName ;
//                String dropName ;
//
//                for( int i = 0 ; i< current.size() ; i++ ){
//                    LabEquipStatus labEquipStatus = current.get( i );
//                    if( labEquipStatus.getLabEquipDataId() == dragId ){
//                        dragName = labEquipStatus.getCurrentStatus();
//                    }else if( labEquipStatus.getLabEquipDataId() == dropId ){
//                        dropName  = labEquipStatus.getCurrentStatus();
//                    }
//                }
//
//                LabEquipData dragEquipData = labEquipDataList.get(dragId);
//                LabEquipData dropEquipData = labEquipDataList.get(dropId);
//
//                dragEquipData.get


//                for( int i = 0 ; i< current.size() ; i++ ){
//                    LabEquipStatus labEquipStatus = current.get( i );
//                    if( labEquipStatus.getLabEquipDataId() == dragId ){
//                        current.remove( i );
//
//                        // we will do some set here
//
//
//                        current.add( i , labEquipStatus );
//                        break;
//                    }
//                }

            }

            else if( type.equals("removeEquipFromBoard") ){

                //JSONObject receiveData = (JSONObject) data.get("data");

                Integer equipIndex =  data.getInt("data");

                // now we need get current index

                // if no longer index into
                // we will set the content inside to null
                    // ok

                List< LabStep > labSteps =  labData.getLabSteps();
                int currentStep = labData.getCurrentLabStep();

                LabStep labStep = labSteps.get( currentStep );

                List< LabEquipStatus > current = labStep.getCurrent();
                for( int i = 0 ; i< current.size() ; i++ ){
                    LabEquipStatus labEquipStatus =  current.get(i);
                    if( labEquipStatus.getLabEquipDataId() == equipIndex ){
                        current.remove(i);
                        break;
                    }
                }
                labStep.setCurrent( current );
                labSteps.set( currentStep , labStep );
                labData.setLabSteps( labSteps );

                Boolean checkExist = checkIfInsideLabSteps(  equipIndex , labSteps  );

                if( checkExist == false ){
                   labData = cleanLaEquipData( labData ,  equipIndex );
                }

                labDataRepository.save( labData );

                refreshLabBoard(  data , labData ,  labId  ,  jsonMapper );

                System.out.println( "removeEquipFromBoard" );

            }

            //removeEquipFromBoard

        }

        //onDrag
        //onMouseDown
        //sendLabBoard(  );

    }

    public void refreshLabBoard( JSONObject data , LabData labData , String labId  , ObjectMapper jsonMapper ) throws JsonProcessingException, JSONException {

        data.put("type", "refreshBoard" );
        data.put( "data" , jsonMapper.writeValueAsString( labData ) );
        sendLabBoard( labId , data.toString() );
    }
    public LabData cleanLaEquipData( LabData labData , int equipIndex ){
        List<LabEquipData> equipmentDataList =  labData.getUsedEquipList();
        LabEquipData labEquipData =  equipmentDataList.get(equipIndex);
        labEquipData.setEquipmentDataStr(null);
        labEquipData.setImageDataStrArr(null);
        equipmentDataList.set( equipIndex , labEquipData );
        labData.setUsedEquipList( equipmentDataList );
        return labData;
    }
    public Boolean checkIfInsideLabSteps( int equipIndex , List< LabStep > labSteps  ){
        for( int i = 0 ; i< labSteps.size() ; i++ ){
            LabStep labStep = labSteps.get(i);
            List< LabEquipStatus > current =  labStep.getCurrent();
            List< LabEquipStatus > before  = labStep.getBefore();
            List< LabEquipStatus > after =  labStep.getAfter();
            if( checkIfInsideLabEquipStatus(  equipIndex , current ) == true ){
                return true;
            }
            if( checkIfInsideLabEquipStatus(  equipIndex , before ) == true ){
                return true;
            }
            if( checkIfInsideLabEquipStatus(  equipIndex , after ) == true ){
                return true;
            }
        }
        return false;
    }
    public Boolean checkIfInsideLabEquipStatus( int equipIndex , List< LabEquipStatus > labEquipStatusList ){
        for( int i = 0 ; i< labEquipStatusList.size() ; i++ ){
            LabEquipStatus labEquipStatus =  labEquipStatusList.get(i);
            if( labEquipStatus.getLabEquipDataId() == equipIndex ){
                return true;
            }
        }
        return false;
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