package com.greenlab.greenlab.labEquip.laboratory.labData;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.ImageData.ImageData;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.ImageData.ImageDataRepository;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.equipmentData.EquipmentData;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.equipmentData.EquipmentDataRepository;
import lombok.Getter;
import lombok.Setter;


import java.util.LinkedList;
import java.util.List;


@Getter
@Setter
public class LabEquipData{

    private int id;
    private String equipmentDataStr;
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