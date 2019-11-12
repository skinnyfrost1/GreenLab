package com.greenlab.greenlab.equipment.equipmentData.userEquipment;

import org.springframework.data.annotation.Id;

import java.util.LinkedList;

public class UserEquipmentFolder {

    @Id
    private String userEquipmentFolderId;

    private String FolderName;

    private LinkedList<String> equipmentIdsInFolder;

    public String getFolderName() {
        return FolderName;
    }

    public void setFolderName(String folderName) {
        FolderName = folderName;
    }

    public LinkedList<String> getEquipmentIdsInFolder() {
        return equipmentIdsInFolder;
    }

    public String getUserEquipmentFolderId() {
        return userEquipmentFolderId;
    }

    public void setEquipmentIdsInFolder(LinkedList<String> equipmentIdsInFolder) {
        this.equipmentIdsInFolder = equipmentIdsInFolder;
    }

    public void setUserEquipmentFolderId(String userEquipmentFolderId) {
        this.userEquipmentFolderId = userEquipmentFolderId;
    }


}
