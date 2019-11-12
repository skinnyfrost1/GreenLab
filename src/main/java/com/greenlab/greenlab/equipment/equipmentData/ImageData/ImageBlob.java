package com.greenlab.greenlab.equipment.equipmentData.ImageData;

import org.springframework.data.annotation.Id;

import java.sql.Timestamp;

public class ImageBlob {

    @Id
    private String id;

    private String blob;

    //private String

    private Timestamp timestamp;


    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBlob() {
        return blob;
    }

    public void setBlob(String blob) {
        this.blob = blob;
    }

}
