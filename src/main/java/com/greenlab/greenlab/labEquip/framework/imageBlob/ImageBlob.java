package com.greenlab.greenlab.labEquip.framework.imageBlob;

import org.springframework.data.annotation.Id;

import java.util.List;

public class ImageBlob {

    @Id
    private String id; // be careful the image data id will not be same as imageblob

    private String blob;

    private int originalWidth;

    private int OriginalHeight;

    private int counter;   // this counter is use to determine   whether we need to delete this photo

                // when we need to increment the counter
                    //


    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    private List<String> imageDataIds; // if this is null then we will delete this image blob

    public List<String> getImageDataIds() {
        return imageDataIds;
    }

    public void setImageDataIds(List<String> imageDataIds) {
        this.imageDataIds = imageDataIds;
    }

    public void setOriginalWidth(int originalWidth) {
        this.originalWidth = originalWidth;
    }

    public void setOriginalHeight(int originalHeight) {
        OriginalHeight = originalHeight;
    }

    public int getOriginalWidth() {
        return originalWidth;
    }

    public int getOriginalHeight() {
        return OriginalHeight;
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
