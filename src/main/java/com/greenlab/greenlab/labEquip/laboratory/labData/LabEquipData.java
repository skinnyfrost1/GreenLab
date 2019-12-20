package com.greenlab.greenlab.labEquip.laboratory.labData;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;


@Getter
@Setter
public class LabEquipData{

    private int id;                    // this is 0123 id
    private String equipmentDataStr;                         //  if you are looking for current name status name
    private List<String> imageDataStrArr;



    public LabEquipData(  ) throws JsonProcessingException {

        imageDataStrArr = new LinkedList<>();
//        String equipmentId , EquipmentDataRepository equipmentDataRepository , ImageDataRepository imageDataRepository
//        imageDataStrArr = new LinkedList<>();
//
//        ObjectMapper jsonMapper = new ObjectMapper();
//        EquipmentData equipmentData = equipmentDataRepository.getById(equipmentId);
//        equipmentDataStr =  jsonMapper.writeValueAsString(equipmentData);
//
//        List<String> list =  equipmentData.getImageIds();
//
//        for( int i = 0 ; i< list.size();i++ ){
//
//            String imageDataId = list.get(i);
//            ImageData imageData =  imageDataRepository.getById(imageDataId);
//            imageDataStrArr.add(jsonMapper.writeValueAsString( imageData ));
//
//
//        }


        //System.out.println("seems work 666");
    }



}