package com.greenlab.greenlab.labEquip.laboratory.labData;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;


@Getter
@Setter
public class LabStep {

    private String info;
    private List<LabEquipStatus> before;
    private List<LabEquipStatus> current;
    private List<LabEquipStatus> after;

    public LabStep(){

        before = new LinkedList<LabEquipStatus>();
        current = new LinkedList<LabEquipStatus>();
        after  =new LinkedList<LabEquipStatus>();

    }

}
