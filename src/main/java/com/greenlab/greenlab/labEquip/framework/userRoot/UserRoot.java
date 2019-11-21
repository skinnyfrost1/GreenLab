package com.greenlab.greenlab.labEquip.framework.userRoot;

import org.springframework.data.annotation.Id;

import java.util.LinkedList;

public abstract class UserRoot {

    @Id
    private String userId;


    private LinkedList<String> folderIds;  // so the equipmentFolderIds cannot be folder name


    public UserRoot(){
        folderIds = new LinkedList<>();
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public LinkedList<String> getFolderIds() {
        return folderIds;
    }

    public void setFolderIds(LinkedList<String> folderIds) {
        this.folderIds = folderIds;
    }

    public Boolean compare( UserRoot  userRoot ){
        if( this.folderIds.size() != userRoot.getFolderIds().size() ){
            return false;
        }
        for( int i = 0 ; i< this.folderIds.size() ; i++ ){
            if( this.folderIds.contains( userRoot.getFolderIds().get(i) )==false ){
                return false;
            }
        }
        return true;
    }

}
