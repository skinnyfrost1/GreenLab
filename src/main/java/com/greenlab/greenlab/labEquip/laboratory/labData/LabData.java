package com.greenlab.greenlab.labEquip.laboratory.labData;


import com.greenlab.greenlab.labEquip.equipment.equipmentData.ImageData.ImageData;
import com.greenlab.greenlab.labEquip.framework.userRoot.ItemData;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class LabData extends ItemData {


    // the structure here need to design

    // we add some structure here

    private List<LabEquipData> labEquipDataList;
    private List<Integer> labEquipDataListOrder;

    // there should have something named
        // step


    public LabData(){

        labEquipDataList = new LinkedList<>();
        labEquipDataListOrder = new LinkedList<>();


    }



}

@Getter
@Setter
class LabEquipData {

    // this we clone all the equipmentdata to here

    // no like or dislike

    private String imageBlobId;

    private List<ImageData> imageDataList;

    private String id;

    private String name ="";

    private String coverBlobId = "";

    private String description  ="";


    public LabEquipData(){


    }

}