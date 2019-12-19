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
    private List<String> selectedData_material;
    private List<Integer> selectedData_quantity;
    private List<String> selectedData_unit;

    private LabEquipment associatedData;
    private List<String> associatedData_material;
    private List<Integer> associatedData_quantity;
    private List<String> associatedData_unit;

    // private List<LabMaterials> solutionMaterialsS;
    private List<String> solutionMaterialsS_material;
    private List<Integer> solutionMaterialsS_quantity;
    private List<String> solutionMaterialsS_unit;

    // private List<LabMaterials> solutionMaterialsA;
    private List<String> solutionMaterialsA_material;
    private List<Integer> solutionMaterialsA_quantity;
    private List<String> solutionMaterialsA_unit;

    private String NewLookS_id;
    private String NewLookA_id;
    private int stepnumber;
    private String hint;

}