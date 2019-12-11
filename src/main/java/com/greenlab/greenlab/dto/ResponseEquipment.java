package com.greenlab.greenlab.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseEquipment {
    private String _id;
    private String equipmentName;
    private String description;
    private String creator;
    private boolean isMaterial;
    private boolean blandable;
    private boolean blander;
    private boolean heatable;
    private boolean heater;
    private String image;

    public ResponseEquipment(){
        
    }
    public ResponseEquipment(String equipmentName, String description, String creator, boolean isMaterial,
            boolean blandable, boolean blander, boolean heatable, boolean heater, String image) {

        this.equipmentName = equipmentName;
        this.description = description;
        this.creator = creator;
        this.isMaterial = isMaterial;
        this.blandable = blandable;
        this.blander = blander;
        this.heatable = heatable;
        this.heater = heater;
        this.image = image;
    }
}
