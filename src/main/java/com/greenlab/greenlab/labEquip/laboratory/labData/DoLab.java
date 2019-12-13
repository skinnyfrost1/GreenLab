package com.greenlab.greenlab.labEquip.laboratory.labData;

import com.greenlab.greenlab.labEquip.framework.userRoot.ItemData;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class DoLab {

    @Id
    private String id;

    private boolean isComplete ;
    private int currentStep  ;
    private int maxStep  ;
    private String labData ;


    public DoLab(){

    }

}
