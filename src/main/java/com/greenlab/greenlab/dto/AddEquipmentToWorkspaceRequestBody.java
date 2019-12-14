package com.greenlab.greenlab.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddEquipmentToWorkspaceRequestBody {

    private String equipment_id;
    private String htmlid;
    private String nickname;

}