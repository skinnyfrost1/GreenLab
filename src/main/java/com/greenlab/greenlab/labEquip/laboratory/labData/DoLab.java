package com.greenlab.greenlab.labEquip.laboratory.labData;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class DoLab {

    @Id
    private String id;

    private LabData labData = null;



    public DoLab(){

    }

}
