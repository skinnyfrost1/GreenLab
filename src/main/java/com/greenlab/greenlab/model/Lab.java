package com.greenlab.greenlab.model;

import java.util.List;

import com.greenlab.greenlab.lab.LabEquipment;

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
    @DBRef
    private List<Equipment> preparedEquipment;   //equipments which the professor pick to use in the lab. 
    private List<LabEquipment> equipmentInLab;      //equipments that have been created in Lab. 


    public Lab() {
        // materials = new ArrayList<Material>();
        // containers = new ArrayList<Container>();
        // holders = new ArrayList<Holder>();
        // steps = new ArrayList<Steps>();
    }

    public Lab(String _id,String courseId, String labName, String labDescription, String creator){
        this._id = _id;
        this.courseId = courseId;
        this.labName = labName;
        this.labDescription = labDescription;
        this.creator = creator;
    }
    // public Lab(String _id, String courseId, String labName, String labDescription, String creator,
    //         List<Material> materials, List<Container> containers, List<Holder> holders) {
    //     this._id = _id;
    //     this.courseId = courseId;
    //     this.labName = labName;
    //     this.labDescription = labDescription;
    //     this.creator = creator;
    //     this.materials = materials;
    //     this.containers = containers;
    //     this.holders = holders;
    // }


    public String testString() {
        return "Lab{" +
                "_id='" + _id + '\'' +
                ", courseId='" + courseId + '\'' +
                ", labName='" + labName + '\'' +
                ", creator='" + creator + '\'' +
                '}';
    }

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getLabDescription() {
        return labDescription;
    }

    public void setLabDescription(String labDescription) {
        this.labDescription = labDescription;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
