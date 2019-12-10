package com.greenlab.greenlab.lab;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Steps{

    private int stepNumber;
    private LabEquipment equipmentA;
    private LabEquipment equipmentB;
    private LabEquipment solution;
    private String info;

}