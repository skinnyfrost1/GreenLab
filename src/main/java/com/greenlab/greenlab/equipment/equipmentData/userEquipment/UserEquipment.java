package com.greenlab.greenlab.equipment.equipmentData.userEquipment;

import org.springframework.data.annotation.Id;

import java.util.List;

public class UserEquipment {

    @Id
    private String userId;

    private String currentEquipmentId;



    private List<String> equipmentFolderIds;  // so the equipmentFolderIds cannot be folder name

    public List<String> getEquipmentFolderIds() {
        return equipmentFolderIds;
    }

    public void setEquipmentFolderIds(List<String> equipmentFolderIds) {
        this.equipmentFolderIds = equipmentFolderIds;
    }

    public String getCurrentEquipmentId() {
        return currentEquipmentId;
    }

    public void setCurrentEquipmentId(String currentEquipmentId) {
        this.currentEquipmentId = currentEquipmentId;
    }



    public String getUserId() {
        return userId;
    }



    public void setUserId(String userId) {
        this.userId = userId;
    }


}
