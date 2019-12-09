package com.greenlab.greenlab.model;

import java.util.List;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Equipment {

    @Id
    private String _id;
    private String equipmentName;
    private String description;
    private String creator;
    private boolean isMaterial;
    private boolean blandable;
    private boolean blander;
    private boolean heatable;
    private boolean heater;
    private Binary image;

    // private double tempreature;
    // private double volume;
    // private List<Material> materials;

    public Equipment() {

    }

    public Equipment(String equipmentName, String description, String creator, boolean isMaterial, boolean blandable,
            boolean blander, boolean heatable, boolean heater) {

        this.equipmentName = equipmentName;
        this.description = description;
        this.creator = creator;
        this.isMaterial = isMaterial;
        this.blandable = blandable;
        this.blander = blander;
        this.heatable = heatable;
        this.heater = heater;
    }

    @Override
    public String toString(){
        String str = "{\n"+
                    "\"equipmentName\":"+equipmentName+
                    "\n\"description\":"+description+
                    "\n\"creator\":"+creator+
                    "\n\"isMaterial\":"+isMaterial+
                    "\n\"blandable\":"+blandable+
                    "\n\"blander\":"+blander+
                    "\n\"heatable\":"+heatable+
                    "\n\"heater\":"+heater+
                    "\n}";
        return str;
    }
    

}