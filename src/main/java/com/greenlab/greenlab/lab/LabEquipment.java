package com.greenlab.greenlab.lab;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabEquipment {
    private String _id;
    private String equipmentName;

    private boolean material;
    private boolean blandable;
    private boolean blander;
    private boolean heatable;
    private boolean heater;

    private double tempreature;
    private List<LabMaterials> materials;

    
}
