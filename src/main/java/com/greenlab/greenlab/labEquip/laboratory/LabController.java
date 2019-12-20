package com.greenlab.greenlab.labEquip.laboratory;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenlab.greenlab.labEquip.laboratory.labData.DoLab;
import com.greenlab.greenlab.labEquip.laboratory.labData.DoLabRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LabController {


    @Autowired
    private DoLabRepository doLabRepository;


    @RequestMapping( value = "/dolab/{dolabId}", method = RequestMethod.GET )
    public String getDoLabBoard(Model model, @PathVariable("dolabId") String dolabId, HttpServletRequest request  ) throws JsonProcessingException {

        //5df2f0becb6f801c28bbed84
        //DoLab doLab =  doLabRepository.getById( "5df2f0becb6f801c28bbed84" );
        //model.addAttribute( "labData", jsonMapper.writeValueAsString( doLab ) );


        return "lab/dolab";

    }

    @RequestMapping(value="/ajax/dolabData" , method = RequestMethod.POST)
    @ResponseBody
    public Object UploadImage(@Valid @RequestBody String reqBody, HttpServletRequest request) throws JSONException, JsonProcessingException {
        System.out.println( reqBody );

        JSONObject jsonObject =  new JSONObject( reqBody );
        String doLoadId = jsonObject.getString( "dolabId" );

        ObjectMapper jsonMapper = new ObjectMapper();
        DoLab doLab =  doLabRepository.getById( doLoadId );
        Map<String,Object> sendData = new HashMap<>();
        //sendData.put("success",true);
        sendData.put("doLabData", jsonMapper.writeValueAsString( doLab ) );

        return sendData;
    }

}
