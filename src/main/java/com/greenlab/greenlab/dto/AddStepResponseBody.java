package com.greenlab.greenlab.dto;

import com.greenlab.greenlab.lab.LabEquipment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddStepResponseBody {
    private String message;
    private LabEquipment labEquipS;
    private LabEquipment labEquipA;
    private String imageS;
    private String imageA;

}