package com.greenlab.greenlab.labEquip.merge;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.equipmentData.EquipmentData;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.equipmentData.EquipmentDataRepository;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.userEquipment.UserEquipmentFolderRepository;
import com.greenlab.greenlab.labEquip.laboratory.labData.LabData;
import com.greenlab.greenlab.labEquip.laboratory.labData.LabDataRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
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

        List<EquipmentData> equipmentDataList =  equipmentDataRepository.findAllByOwnerId("666");


        JSONArray jsonArray = new JSONArray();
        jsonArray.put( jsonMapper.writeValueAsString( equipmentDataList )  );






        Map<String,Object> sendData = new HashMap<>();
        //sendData.put("allEquipmentData",  );
        sendData.put( "data", jsonArray.toString() );
        return sendData;

    }

}





