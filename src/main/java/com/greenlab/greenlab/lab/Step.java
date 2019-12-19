package com.greenlab.greenlab.lab;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Step {

    private String _id;
    private List<LabEquipment> equipmentsInLab;
    private LabEquipment selectedData;
    private LabEquipment associatedData;
    private List<LabMaterials> solutionMaterialsS;
    private List<LabMaterials> solutionMaterialsA;
    private String newLookS_id;
    private String newLookA_id;
    private int stepnumber;
    private String hint;

    public Step() {

    }

    public Step(LabEquipment selectedData, LabEquipment associatedData, List<LabMaterials> solutionMaterialsS,
            List<LabMaterials> solutionMaterialsA, String newLookS_id, String newLookA_id, int stepnumber,
            String hint) {
        // this.equipmentsInLab = equipmentsInLab;
        this.selectedData = selectedData;
        this.associatedData = associatedData;
        this.solutionMaterialsS = solutionMaterialsS;
        this.solutionMaterialsA = solutionMaterialsA;
        this.newLookS_id=newLookS_id;
        this.newLookA_id = newLookA_id;
        this.stepnumber = stepnumber;
        this.hint = hint;
    }
}