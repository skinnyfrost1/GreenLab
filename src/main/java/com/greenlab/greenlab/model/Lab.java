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
    private List<Equipment> preparedEquipment; // equipments which the professor pick to use in the lab.
    private List<LabEquipment> equipmentsInLab; // equipments that have been created in Lab.

    // private List<Steps> steps;

    private Boolean doneWorkSpace;

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
