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
public class StuLab{
    @Id
    String _id; 
    String studentEmail;
    @DBRef
    List<Lab> enrolledLabs;
    List<Lab> doingLabs;



    public StuLab(){
        this.enrolledLabs = new ArrayList<>();
        this.doingLabs = new ArrayList<>();
    }
}