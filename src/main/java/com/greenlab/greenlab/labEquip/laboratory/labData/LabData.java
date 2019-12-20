package com.greenlab.greenlab.labEquip.laboratory.labData;


import com.greenlab.greenlab.labEquip.framework.userRoot.ItemData;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class LabData extends ItemData {

    private int currentLabStep;

    private List<String> labEquipDataList;
    private List<LabStep> labSteps;
    private List<LabEquipData> usedEquipList;

    public LabData(){

        usedEquipList = new LinkedList<>();
        labEquipDataList = new LinkedList<>();
        labSteps = new LinkedList<>();

        currentLabStep = 0;
        LabStep labStep = new LabStep();
        labSteps.add(labStep);

    }



}






//@Getter
//@Setter
//class LabEquipData {
//
//    // this we clone all the equipmentdata to here
//
//    // no like or dislike
//
//    private String imageBlobId;
//
//    private List<ImageData> imageDataList;
//
//    private String id;
//
//    private String name ="";
//
//    private String coverBlobId = "";
//
//    private String description  ="";
//
//
//    public LabEquipData(){
//
//
//    }
//
//}