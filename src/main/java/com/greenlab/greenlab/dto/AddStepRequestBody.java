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

    @Override
    public String toString() {
        return "AddStepRequestBody{" +
                "_id='" + _id + '\'' +
                ", selectedData=" + selectedData +
                ", selectedData_material=" + selectedData_material +
                ", selectedData_quantity=" + selectedData_quantity +
                ", selectedData_unit=" + selectedData_unit +
                ", associatedData=" + associatedData +
                ", associatedData_material=" + associatedData_material +
                ", associatedData_quantity=" + associatedData_quantity +
                ", associatedData_unit=" + associatedData_unit +
                ", solutionMaterialsS_material=" + solutionMaterialsS_material +
                ", solutionMaterialsS_quantity=" + solutionMaterialsS_quantity +
                ", solutionMaterialsS_unit=" + solutionMaterialsS_unit +
                ", solutionMaterialsA_material=" + solutionMaterialsA_material +
                ", solutionMaterialsA_quantity=" + solutionMaterialsA_quantity +
                ", solutionMaterialsA_unit=" + solutionMaterialsA_unit +
                ", NewLookS_id='" + NewLookS_id + '\'' +
                ", NewLookA_id='" + NewLookA_id + '\'' +
                ", stepnumber=" + stepnumber +
                ", hint='" + hint + '\'' +
                '}';
    }
}