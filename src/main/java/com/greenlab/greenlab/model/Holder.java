package com.greenlab.greenlab.model;

import java.util.List;

import com.greenlab.greenlab.lab.Equipments;
import com.greenlab.greenlab.model.Container;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document
public class Holder extends Equipments{

    @Id
    private String _id;
    private List<Container> contains;

    public Holder(){
        super.setEquipmentType("Holder");
    }

}