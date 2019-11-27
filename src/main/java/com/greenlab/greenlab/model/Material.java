package com.greenlab.greenlab.model;

import com.greenlab.greenlab.lab.Equipments;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document
public class Material extends Equipments{
    private String _id;
    public Material(){
        super.setEquipmentType("Material");
    }
}