package com.greenlab.greenlab.lab;

import java.util.ArrayList;
import java.util.List;

import com.greenlab.greenlab.model.Equipment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabEquipment {
    private String equipment_id;
    private String equipmentName;
    private String nameInWorkspace;
    private boolean material;
    private boolean blandable;
    private boolean blander;
    private boolean heatable;
    private boolean heater;

    private double tempreature;
    private List<LabMaterials> materials;

    public LabEquipment() {

    }

    public LabEquipment(Equipment equipment, String nameInWorkspace) {
        this.equipment_id = equipment.get_id();
        this.equipmentName = equipment.getEquipmentName();
        this.nameInWorkspace = nameInWorkspace;
        this.material = equipment.isMaterial();
        this.blandable = equipment.isBlandable();
        this.blander = equipment.isBlander();
        this.heatable = equipment.isHeatable();
        this.heater = equipment.isHeater();
        this.materials = new ArrayList<>();
    }

}
