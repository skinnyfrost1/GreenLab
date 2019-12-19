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
        if (material) {
            LabMaterials lm = new LabMaterials(equipment.getEquipmentName(), 999999, equipment.getUnit());
            materials.add(lm);
        }
    }

    public void copy(LabEquipment le) {
        this.equipment_id = le.getEquipment_id();
        this.htmlid = le.getHtmlid();
        this.nickname = le.getNickname();
        this.material = le.isMaterial();
        this.blandable = le.isBlandable();
        this.blander = le.isBlander();
        this.heatable = le.isHeatable();
        this.heater = le.isHeater();
        // private double tempreature;
        List<LabMaterials> lms = new ArrayList<>();
        if (le.materials != null) {
            for (LabMaterials lm : le.getMaterials()) {
                LabMaterials buffer = new LabMaterials(lm.getMaterial(), lm.getQuantity(), lm.getUnit());
                lms.add(buffer);
            }
        }
        this.materials = lms;

    }

    public LabEquipment deepClone(){
        LabEquipment clone = new LabEquipment();
        clone.setEquipment_id(this.equipment_id);
        clone.setHtmlid(this. htmlid);
        clone.setNickname(this.nickname);
        clone.setMaterial(this.material);
        clone.setBlandable(this.blandable);
        clone.setBlander(this.blander);
        clone.setHeatable(this.heatable);
        clone.setHeater(this.heater);
        List<LabMaterials> lst = new ArrayList<>();
        LabMaterials buffer;
        for(LabMaterials l : this.materials){
            buffer = l.deepClone();
            lst.add(buffer);
        }
        clone.setMaterials(lst);
        return clone;
    }

    @Override
    public String toString() {
        String str = "{equipment_id=" + this.equipment_id;
        str += "  htmlid=" + this.htmlid;
        str += "  nickname=" + this.nickname;
        for (LabMaterials lm : this.materials) {
            str += "\n" + lm.toString();
        }
        str += "}";
        return str;
    }

}
