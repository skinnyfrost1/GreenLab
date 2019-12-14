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
    private String htmlid;
    private String nickname;

    private boolean material;
    private boolean blandable;
    private boolean blander;
    private boolean heatable;
    private boolean heater;

    // private double tempreature;
    private List<LabMaterials> materials;

    public LabEquipment() {

    }

    public LabEquipment(Equipment equipment, String htmlid, String nickname) {
        this.equipment_id = equipment.get_id();
        this.htmlid = htmlid;
        this.nickname = nickname;
        this.material = equipment.isMaterial();
        this.blandable = equipment.isBlandable();
        this.blander = equipment.isBlander();
        this.heatable = equipment.isHeatable();
        this.heater = equipment.isHeater();
        // this.tempreature = 37;
        this.materials = new ArrayList<>();
    }

}
