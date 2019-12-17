package com.greenlab.greenlab.lab;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabMaterials {

    private String material;
    private double quantity;
    private String unit;

    public LabMaterials() {

    }

    public LabMaterials(String material, double quantity, String unit) {
        this.material = material;
        this.quantity = quantity;
        this.unit = unit;
    }
}