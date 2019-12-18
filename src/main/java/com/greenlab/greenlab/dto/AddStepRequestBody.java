package com.greenlab.greenlab.dto;

import java.util.List;

import com.greenlab.greenlab.lab.LabEquipment;
import com.greenlab.greenlab.lab.LabMaterials;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AddStepRequestBody {

    private String _id;
    private LabEquipment selectedData;
    private LabEquipment associatedData;
    private List<LabMaterials> solutionMaterialsS;
    private List<LabMaterials> solutionMaterialsA;
    private String NewLookS_id;
    private String NewLookA_id;
    private int stepNumber;
    private String hint;

}