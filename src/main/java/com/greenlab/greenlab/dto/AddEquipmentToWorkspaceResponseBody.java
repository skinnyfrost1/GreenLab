package com.greenlab.greenlab.dto;

import com.greenlab.greenlab.lab.LabEquipment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddEquipmentToWorkspaceResponseBody{
    private String message;
    private LabEquipment labEquipment;
}