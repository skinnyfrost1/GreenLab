package com.greenlab.greenlab.labEquip.framework.userRoot;

import org.springframework.data.annotation.Id;

public abstract class ItemData {


    @Id
    private String id;

    private String name ="";

    private String coverBlobId = "";

    private String description  ="";

    private Boolean shared = false;

    private Boolean favourite = false;

    private String ownerId;  // when a person directly open this
                                    // which means when we create a equipment
                                        // we don't need all folder
                                            // we only need

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setShared(Boolean shared) {
        this.shared = shared;
    }

    public void setFavourite(Boolean favourite) {
        this.favourite = favourite;
    }

    public Boolean getShared() {
        return shared;
    }

    public Boolean getFavourite() {
        return favourite;
    }

    public void setCoverBlobId(String coverBlobId) {
        this.coverBlobId = coverBlobId;
    }

    public String getCoverBlobId() {
        return coverBlobId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


}
