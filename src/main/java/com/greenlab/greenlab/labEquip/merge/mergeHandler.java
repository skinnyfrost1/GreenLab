package com.greenlab.greenlab.labEquip.merge;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.ImageData.ImageData;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.ImageData.ImageDataRepository;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.equipmentData.EquipmentData;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.equipmentData.EquipmentDataRepository;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.userEquipment.UserEquipmentFolder;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.userEquipment.UserEquipmentFolderRepository;
import com.greenlab.greenlab.labEquip.framework.imageBlob.ImageBlob;
import com.greenlab.greenlab.labEquip.framework.imageBlob.ImageBlobRepository;
import com.greenlab.greenlab.labEquip.laboratory.labData.LabData;
import com.greenlab.greenlab.labEquip.laboratory.labData.LabDataRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
public class mergeHandler {



    // this is the easy version

//    @RequestMapping(value="/ajax/requestId" , method = RequestMethod.POST)
//    @ResponseBody
//    public Object requestId(HttpServletRequest request) throws JSONException {
//        String dataStr = request.getParameter("data");
//        //System.out.println( dataStr );
//        Map<String,Object> sendData = new HashMap<>();
//        sendData.put("success",true);
//        sendData.put("data", "weixin.tang@stonybrook.edu" );
//        return sendData;
//    }

//    @RequestMapping(value="/ajax/dolabData" , method = RequestMethod.POST)
//    @ResponseBody
//    public Object UploadImage(@Valid @RequestBody String reqBody, HttpServletRequest request) throws JSONException, JsonProcessingException {
//        //System.out.println( reqBody );
//        ObjectMapper jsonMapper = new ObjectMapper();
//        DoLab doLab =  doLabRepository.getById( "5df2fdafcb6f801e7431a523" );
//        Map<String,Object> sendData = new HashMap<>();
//        //sendData.put("success",true);
//        sendData.put("doLabData", jsonMapper.writeValueAsString( doLab ) );
//
//        return sendData;
//    }


    @Autowired
    private UserEquipmentFolderRepository userEquipmentFolderRepository;

    @Autowired
    public EquipmentDataRepository equipmentDataRepository;


    @RequestMapping(value="/ajax/listEquipments" , method = RequestMethod.POST)
    @ResponseBody
    public Object listEquipments(@Valid @RequestBody String reqBody, HttpServletRequest request) throws JSONException, JsonProcessingException {


        System.out.println( reqBody );

        ObjectMapper jsonMapper = new ObjectMapper();

        String emailId = (String) request.getSession().getAttribute("email");

//        UserEquipmentFolder userEquipmentFolder =  userEquipmentFolderRepository.findByOwnerAndType( emailId , "all" );
//
//        List<String> list =  userEquipmentFolder.getItemIdsInFolder()
//
//        for( int i = 0 ; i < list.size() ; i++ ){
//}

        System.out.println( "__emailId is :"+emailId );

        List<EquipmentData> equipmentDataList =  equipmentDataRepository.findAllByOwnerId( emailId );


        JSONArray jsonArray = new JSONArray();
        jsonArray.put( jsonMapper.writeValueAsString( equipmentDataList )  );

        Map<String,Object> sendData = new HashMap<>();
        //sendData.put("allEquipmentData",  );
        sendData.put( "data", jsonArray.toString() );
        return sendData;

    }

//downloadEquipments


    ///ajax/listLabs

    @Autowired
    private LabDataRepository labDataRepository;

    @RequestMapping(value="/ajax/listLabs" , method = RequestMethod.POST)
    @ResponseBody
    public Object listLabs(@Valid @RequestBody String reqBody, HttpServletRequest request) throws JSONException, JsonProcessingException {

        System.out.println( "reqBody  listLabs" );
        System.out.println( reqBody );

        ObjectMapper jsonMapper = new ObjectMapper();

        String emailId = (String) request.getSession().getAttribute("email");

//        UserEquipmentFolder userEquipmentFolder =  userEquipmentFolderRepository.findByOwnerAndType( emailId , "all" );
//
//        List<String> list =  userEquipmentFolder.getItemIdsInFolder()
//
//        for( int i = 0 ; i < list.size() ; i++ ){
//}

        List<LabData> equipmentDataList =  labDataRepository.findAllByOwnerId( emailId );


        JSONArray jsonArray = new JSONArray();
        jsonArray.put( jsonMapper.writeValueAsString( equipmentDataList )  );



        Map<String,Object> sendData = new HashMap<>();
        //sendData.put("allEquipmentData",  );
        sendData.put( "data", jsonArray.toString() );
        return sendData;

    }

//  /ajax/selectedLabIds

    //   /ajax/searchEquipments
    //  /ajax/searchEquipments

    @RequestMapping(value="/ajax/searchEquipments" , method = RequestMethod.POST)
    @ResponseBody
    public Object listSearchEquipments(@Valid @RequestBody String reqBody, HttpServletRequest request) throws JSONException, JsonProcessingException {


        System.out.println( reqBody );

        ObjectMapper jsonMapper = new ObjectMapper();

        String emailId = (String) request.getSession().getAttribute("email");

//        UserEquipmentFolder userEquipmentFolder =  userEquipmentFolderRepository.findByOwnerAndType( emailId , "all" );
//        List<String> list =  userEquipmentFolder.getItemIdsInFolder()
//        for( int i = 0 ; i < list.size() ; i++ ){
//}

        System.out.println( "__emailId is :"+emailId );

        List<EquipmentData> equipmentDataList =  equipmentDataRepository.findAllByShared( true );


        JSONArray jsonArray = new JSONArray();
        jsonArray.put( jsonMapper.writeValueAsString( equipmentDataList )  );






        Map<String,Object> sendData = new HashMap<>();
        //sendData.put("allEquipmentData",  );
        sendData.put( "data", jsonArray.toString() );
        return sendData;

    }


    @RequestMapping(value="/ajax/selectedLabIds" , method = RequestMethod.POST)
    @ResponseBody
    public Object addSelectedLabShare(@Valid @RequestBody String reqBody, HttpServletRequest request) throws JSONException, JsonProcessingException {


        System.out.println( reqBody );

        JSONObject receiveData = new JSONObject(reqBody);
        JSONArray jsonArray = (JSONArray)receiveData.get("blobIds");

        String emailId = (String) request.getSession().getAttribute("email");

        for( int i = 0 ; i < jsonArray.length() ; i++ ){

            String labId  = jsonArray.getString( i );

            LabData labData =  labDataRepository.getById( labId );
            labData.setShared( false );
            labData.setOwnerId( emailId );

            labDataRepository.save(labData);

        }



        Map<String,Object> sendData = new HashMap<>();

        return sendData;
    }


    ///ajax/selectedEquipmentIds
    @RequestMapping(value="/ajax/selectedEquipmentIds" , method = RequestMethod.POST)
    @ResponseBody
    public Object addSelectedEquipmentShare(@Valid @RequestBody String reqBody, HttpServletRequest request) throws JSONException, JsonProcessingException {


        System.out.println( reqBody );

        JSONObject receiveData = new JSONObject(reqBody);
        JSONArray jsonArray = (JSONArray)receiveData.get("blobIds");

        String emailId = (String) request.getSession().getAttribute("email");

        for( int i = 0 ; i < jsonArray.length() ; i++ ){

            String equipmentId  = jsonArray.getString( i );

            DuplicateAndSave( equipmentId , emailId );

        }



        Map<String,Object> sendData = new HashMap<>();

        return sendData;
    }

    private ImageDataRepository imageDataRepository;

    private ImageBlobRepository imageBlobRepository;

    public void DuplicateAndSave( String equipmentId , String userId ){

        UserEquipmentFolder userEquipmentFolder = userEquipmentFolderRepository.findByOwnerAndType( userId , "all" );

        String newEquipmentId =   shareEquipmentCopy(  equipmentId , userId );

        LinkedList<String> linkedList = userEquipmentFolder.getItemIdsInFolder();
        linkedList.add( 0, newEquipmentId );
        userEquipmentFolder.setItemIdsInFolder(  linkedList );

        userEquipmentFolderRepository.save( userEquipmentFolder );

    }

    public String shareEquipmentCopy( String equipmentId , String ownerId ){

        EquipmentData equipmentData = equipmentDataRepository.getById( equipmentId );
        equipmentData.setId(null);
        equipmentData.setShared( false );
        //equipmentData.setCurrentImageIds(null);
        LinkedList<String> linkedList = equipmentData.getImageIds();
        LinkedList<String>  putlinkedList = new LinkedList<>();
        for( int i = 0 ; i< linkedList.size() ;i++ ){

            ImageData imageData = imageDataRepository.getById( linkedList.get(i) );
            String imageBlobId = imageData.getBlobId();

            ImageBlob imageBlob =  imageBlobRepository.getById(imageBlobId);
            imageBlob.setCounter( imageBlob.getCounter()+1 );
            imageBlobRepository.save(imageBlob);

            imageData.setId(null);
            String newImageDataId = imageDataRepository.save( imageData ).getId();
            putlinkedList.add( newImageDataId );

        }
        equipmentData.setImageIds( putlinkedList );
        equipmentData.setOwnerId( ownerId );
        String newEquipmentDataIs =  equipmentDataRepository.save(equipmentData).getId();
        return  newEquipmentDataIs;
    }

    @RequestMapping(value="/ajax/searchLabs" , method = RequestMethod.POST)
    @ResponseBody
    public Object listSearchLabs(@Valid @RequestBody String reqBody, HttpServletRequest request) throws JSONException, JsonProcessingException {


        System.out.println( reqBody );

        ObjectMapper jsonMapper = new ObjectMapper();

        String emailId = (String) request.getSession().getAttribute("email");

//        UserEquipmentFolder userEquipmentFolder =  userEquipmentFolderRepository.findByOwnerAndType( emailId , "all" );
//
//        List<String> list =  userEquipmentFolder.getItemIdsInFolder()
//
//        for( int i = 0 ; i < list.size() ; i++ ){
//}

        List<LabData> equipmentDataList =  labDataRepository.findAllByShared( true );


        JSONArray jsonArray = new JSONArray();
        jsonArray.put( jsonMapper.writeValueAsString( equipmentDataList )  );



        Map<String,Object> sendData = new HashMap<>();
        //sendData.put("allEquipmentData",  );
        sendData.put( "data", jsonArray.toString() );
        return sendData;

    }


}





