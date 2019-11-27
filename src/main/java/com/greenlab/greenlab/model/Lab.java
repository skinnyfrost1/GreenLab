package com.greenlab.greenlab.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document

public class Lab {

    @Id
    private String _id;
    private String courseId;
    private String labName;
    private String labDescription;
    private String creator;
    private String stepObjectId;
    private List<Material> materials;
    private List<Container> containers;
    private List<Holder> holders;
    // private List<Steps> steps;

    public Lab() {
        materials = new ArrayList<Material>();
        containers = new ArrayList<Container>();
        holders = new ArrayList<Holder>();
        // steps = new ArrayList<Steps>();
    }

    public Lab(String _id,String courseId, String labName, String labDescription, String creator){
        this._id = _id;
        this.courseId = courseId;
        this.labName = labName;
        this.labDescription = labDescription;
        this.creator = creator;
    }
    public Lab(String _id, String courseId, String labName, String labDescription, String creator,
            List<Material> materials, List<Container> containers, List<Holder> holders) {
        this._id = _id;
        this.courseId = courseId;
        this.labName = labName;
        this.labDescription = labDescription;
        this.creator = creator;
        this.materials = materials;
        this.containers = containers;
        this.holders = holders;
    }


    public String testString() {
        return "Lab{" +
                "_id='" + _id + '\'' +
                ", courseId='" + courseId + '\'' +
                ", labName='" + labName + '\'' +
                ", creator='" + creator + '\'' +
                '}';
    }

}
