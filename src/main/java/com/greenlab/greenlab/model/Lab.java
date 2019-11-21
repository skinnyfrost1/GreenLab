package com.greenlab.greenlab.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document

public class Lab{

    @Id
    private String _id;
    private String labId;
    private String labDescription;
    private String stepObjectId;
    private List<Material> materials;
    private List<Container> containers;
    private List<Holder> holders;
    // private List<Steps> steps;

    public Lab(){
        materials = new ArrayList<Material>();
        containers = new ArrayList<Container>();
        holders = new ArrayList<Holder>();
        // steps = new ArrayList<Steps>();
    }

    


    

}
