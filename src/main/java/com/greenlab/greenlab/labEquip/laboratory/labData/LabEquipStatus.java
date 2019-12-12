package com.greenlab.greenlab.labEquip.laboratory.labData;


import com.greenlab.greenlab.labEquip.equipment.equipmentData.polygonData.Position;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabEquipStatus {

    private int labEquipDataId;
    private String currentStatus;
    private int x;
    private int y;

}