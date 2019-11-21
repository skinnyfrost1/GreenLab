package com.greenlab.greenlab.labEquip.laboratory.userLab;

import com.greenlab.greenlab.labEquip.framework.userRoot.UserRoot;


public class UserLabFolder extends UserRoot {

}

//
//    @Id
//    private String userLabFolderId;
//
//    private String FolderName;
//
//    private LinkedList<String> labIdsInFolder;
//
//    public UserLabFolder(){
//
//        labIdsInFolder = new LinkedList<>();
//
//    }
//
//    public void setFolderName(String folderName) {
//        FolderName = folderName;
//    }
//
//    public String getFolderName() {
//        return FolderName;
//    }
//
//    public LinkedList<String> getLabIdsInFolder() {
//        return labIdsInFolder;
//    }
//
//    public String getUserLabFolderId() {
//        return userLabFolderId;
//    }
//
//    public void setLabIdsInFolder(LinkedList<String> labIdsInFolder) {
//        this.labIdsInFolder = labIdsInFolder;
//    }
//
//    public void setUserLabFolderId(String userLabFolderId) {
//        this.userLabFolderId = userLabFolderId;
//    }

//
//public class UserEquipmentFolder {
//
//    @Id
//    private String userEquipmentFolderId;
//
//    private String FolderName;
//
//    private LinkedList<String> equipmentIdsInFolder;
//
//    public UserEquipmentFolder(){
//        equipmentIdsInFolder = new LinkedList<>();
//    }
//
//    public String getFolderName() {
//        return FolderName;
//    }
//
//    public void setFolderName(String folderName) {
//        FolderName = folderName;
//    }
//
//    public LinkedList<String> getEquipmentIdsInFolder() {
//        return equipmentIdsInFolder;
//    }
//
//    public String getUserEquipmentFolderId() {
//        return userEquipmentFolderId;
//    }
//
//    public void setEquipmentIdsInFolder(LinkedList<String> equipmentIdsInFolder) {
//        this.equipmentIdsInFolder = equipmentIdsInFolder;
//    }
//
//    public void setUserEquipmentFolderId(String userEquipmentFolderId) {
//        this.userEquipmentFolderId = userEquipmentFolderId;
//    }
//
//    public boolean equals(green.lab.equipment.equipmentData.userEquipment.UserEquipmentFolder userEquipmentFolder) {
//
//
//        if (this.getFolderName() != userEquipmentFolder.getFolderName()) {
//            return false;
//        }
//
//        if (this.getUserEquipmentFolderId() != userEquipmentFolder.getUserEquipmentFolderId()) {
//            return false;
//        }
//
//        if (this.getEquipmentIdsInFolder().size() != userEquipmentFolder.getEquipmentIdsInFolder().size()) {
//            return false;
//        }
//
//        for (int i = 0; i < this.getEquipmentIdsInFolder().size(); i++) {
//            if (this.getEquipmentIdsInFolder().contains(userEquipmentFolder.getEquipmentIdsInFolder().get(i)) == false) {
//                return false;
//            }
//        }
//
//        return true;
//    }
//}
