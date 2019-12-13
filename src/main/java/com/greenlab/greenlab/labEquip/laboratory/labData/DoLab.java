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

    private LabData labData = null;

    private int currentStep = 0;

    private boolean isComplete = false;

    public DoLab(){

    }

}
