package com.greenlab.greenlab.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabNameAndId {
    private String _id;
    private String labName;

    public LabNameAndId() {

    }

    public LabNameAndId(String _id, String labName) {
        this._id = _id;
        this.labName = labName;
    }
}