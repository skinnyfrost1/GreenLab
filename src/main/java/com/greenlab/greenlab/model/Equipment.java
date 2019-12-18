package com.greenlab.greenlab.model;

import java.util.Base64;
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
    private boolean material;
    private String unit;
    private boolean blandable;
    private boolean blander;
    private boolean heatable;
    private boolean heater;
    private boolean solution;
    private Binary image;

    // private double tempreature;
    // private double volume;
    // private List<Material> materials;

    public Equipment() {

    }

    public Equipment(String equipmentName, String description, String creator, boolean material, String unit, boolean blandable,
            boolean blander, boolean heatable, boolean heater, boolean solution) {

        this.equipmentName = equipmentName;
        this.description = description;
        this.creator = creator;
        this.material = material;
        this.unit = unit;
        this.blandable = blandable;
        this.blander = blander;
        this.heatable = heatable;
        this.heater = heater;
        this.solution = solution;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public Binary getImage() {
        return image;
    }

    public void setImage(Binary image) {
        this.image = image;
    }

    public String getStringImage(){
        return Base64.getEncoder().encodeToString(image.getData());
    }

    public String get_id() {
        return _id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public String toString(){
        String str = "{\n"+
                    "\"equipmentName\":"+equipmentName+
                    "\n\"description\":"+description+
                    "\n\"creator\":"+creator+
                    "\n\"material\":"+material+
                    "\n\"blandable\":"+blandable+
                    "\n\"blander\":"+blander+
                    "\n\"heatable\":"+heatable+
                    "\n\"heater\":"+heater+
                    "\n}";
        return str;
    }

    public Equipment clone(){
        Equipment e = new Equipment(this.equipmentName, this.description, this.creator, this.material, this.unit, this.blandable,
                this.blander, this.heatable, this.heater, this.solution);
        e.setImage(this.image);
        return e;

    }
    

}