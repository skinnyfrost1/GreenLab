package com.greenlab.greenlab.lab;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Step{

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