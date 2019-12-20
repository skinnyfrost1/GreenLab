package com.greenlab.greenlab.labEquip.laboratory.labData;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class DoLab {

    @Id
    private String _id;

    private LabData labData = null;

    private String creator = null;

    private String courseId  ;   //cse308

    private String labName ;

    public DoLab(){

    }

}
