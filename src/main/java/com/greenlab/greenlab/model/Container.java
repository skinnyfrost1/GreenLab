package com.greenlab.greenlab.model;

import java.util.List;

import com.greenlab.greenlab.lab.Equipments;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document
public class Container extends Equipments{

    @Id
    private String _id;
    private List<Material> contains;

    public Container(){
        super.setEquipmentType("Container");
    }

}