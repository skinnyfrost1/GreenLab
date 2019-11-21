package com.greenlab.greenlab.labEquip.equipment;


//import green.lab.equipment.equipmentData.notUse.EquipmentData;
//import green.lab.equipment.equipmentData.notUse.UserEquipment;
//import green.lab.equipment.equipmentData.notUse.UsersEquipment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.equipmentData.EquipmentData;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.equipmentData.EquipmentDataRepository;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.userEquipment.UserEquipment;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.userEquipment.UserEquipmentFolder;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.userEquipment.UserEquipmentFolderRepository;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.userEquipment.UserEquipmentRepository;
import com.greenlab.greenlab.labEquip.framework.imageBlob.ImageBlob;
import com.greenlab.greenlab.labEquip.framework.imageBlob.ImageBlobRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static java.awt.Color.green;

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



    @RequestMapping(value="/ajax/requestId" , method = RequestMethod.POST)
    @ResponseBody
    public Object requestId(HttpServletRequest request) throws JSONException {
        String dataStr = request.getParameter("data");
        System.out.println( dataStr );
        Map<String,Object> sendData = new HashMap<>();
        sendData.put("success",true);
        sendData.put("data", "weixin.tang@stonybrook.edu" );
        return sendData;
    }

    //ajax/requestNewEquipment

    @RequestMapping(value="/ajax/requestNewEquipment" , method = RequestMethod.POST)
    @ResponseBody
    public Object requestNewEquipment(HttpServletRequest request) throws JSONException {
        String dataStr = request.getParameter("data");
        System.out.println( dataStr );
        // so we can get the user Id from session
        // then we create a new

        UserEquipment userEquipment = userEquipmentRepository.findByUserId("weixin.tang@stonybrook.edu");

        LinkedList<String> folderIds = userEquipment.getFolderIds();

        // now we need find  the type is all

        for( int i = 0 ; i< folderIds.size() ; i++ ){



        }


        Map<String,Object> sendData = new HashMap<>();
        sendData.put("success",true);
        sendData.put("data", "http://www.google.com" );
        return sendData;
    }


    //imageBlobRepository

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
    public void handleEquipmentFront(@DestinationVariable String userId , @DestinationVariable String sessionId , String message) throws JSONException {

        JSONObject jsonObject = new JSONObject(message);
        if( jsonObject.get("type").toString().equals("connectSuccess")  ){
            messagingTemplate.convertAndSend("/topic/image/" + userId + "/" + sessionId, jsonObject.toString() );
        }

        //System.out.println( message+"__"+ userId +"__"+ sessionId);


    }


    @MessageMapping("/equipment/back/{userId}/{sessionId}")
    public void handleEquipmentBack(@DestinationVariable String userId , @DestinationVariable String sessionId , String message) throws JSONException {





    }

    @MessageMapping("/lab/front/{userId}/{sessionId}")
    public void handleLabFront(@DestinationVariable String userId , @DestinationVariable String sessionId , String message) throws JSONException {





    }


    @MessageMapping("/lab/back/{userId}/{sessionId}")
    public void handleLabBack(@DestinationVariable String userId , @DestinationVariable String sessionId , String message) throws JSONException {





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
        for( int  i = 0 ; i < userEquipment.getFolderIds().size() ; i++ ){
            UserEquipmentFolder userEquipmentFolder = userEquipmentFolderRepository.findByUserFolderId( userEquipment.getFolderIds().get(i) );
            jsonObject = new JSONObject();
            jsonObject.put("type", "UserEquipmentFolder");
            jsonObject.put( "data", objectMapper.writeValueAsString( userEquipmentFolder ) );
            jsonArray.put( jsonObject );
            for( int y = 0 ; y< userEquipmentFolder.getItemIdsInFolder().size() ; y++  ){
                EquipmentData equipmentData = equipmentDataRepository.getById( userEquipmentFolder.getItemIdsInFolder().get(y) );
                jsonObject = new JSONObject();
                jsonObject.put("type", "EquipmentData");
                jsonObject.put( "data", objectMapper.writeValueAsString( equipmentData ) );
                jsonArray.put( jsonObject );
            }
        }
        return jsonArray;
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