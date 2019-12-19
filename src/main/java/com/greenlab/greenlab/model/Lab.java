package com.greenlab.greenlab.model;

import java.util.ArrayList;
import java.util.List;

import com.greenlab.greenlab.lab.LabEquipment;
import com.greenlab.greenlab.lab.Step;

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
    private List<LabEquipment> equipmentsInLab; // equipments that have been created in Lab.
    private List<Step> steps;
    @DBRef
    private List<Equipment> preparedEquipment; // equipments which the professor pick to use in the lab.

    // private Boolean doneWorkSpace;

    public Lab() {

    }

    public Lab(String _id, String courseId, String labName, String labDescription, String creator, List<Equipment> preparedEquipment) {
        this._id = _id;
        this.courseId = courseId;
        this.labName = labName;
        this.labDescription = labDescription;
        this.creator = creator;
        this.preparedEquipment = preparedEquipment;
    }

    public Lab(String courseId, String labName, String labDescription, String creator, List<Equipment> preparedEquipment) {
        // this._id = _id;
        this.courseId = courseId;
        this.labName = labName;
        this.labDescription = labDescription;
        this.creator = creator;
        this.preparedEquipment = preparedEquipment;
    }


    public Lab deepClone(){
        Lab clone = new Lab();
        clone.set_id(this._id);
        clone.setCourseId(this.courseId);
        clone.setLabName(this.labName);
        clone.setLabDescription(this.labDescription);
        clone.setCreator(this.creator);
        clone.setStepObjectId(this.stepObjectId);
        List<LabEquipment> les = new ArrayList<>();
        for (LabEquipment le : this.equipmentsInLab ){
            les.add(le.deepClone());
        }
        clone.setEquipmentsInLab(les);


        List<Step> ss = new ArrayList<>();
        for (Step s : this.steps){
            ss.add(s.deepClone());
        }
        clone.setSteps(ss);

        List<Equipment> es = new ArrayList<>();
        for (Equipment e : this.preparedEquipment){
            es.add(e);
        }
        clone.setPreparedEquipment(es);

        
        

        return clone;

    }


    public String testString() {
        return "Lab{" + "_id='" + _id + '\'' + ", courseId='" + courseId + '\'' + ", labName='" + labName + '\''
                + ", creator='" + creator + '\'' + '}';
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
