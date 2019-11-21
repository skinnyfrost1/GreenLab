package com.greenlab.greenlab.labEquip.framework.userRoot;

import org.springframework.data.annotation.Id;

import java.util.LinkedList;

public abstract class UserRootFolder {

    @Id
    private String userFolderId;

    private String name ="";

    private String type =""; // the type will be
                            // share
                            // favourite
                            // all
                            // recent
                            // user define  for example user create folder for them self

    // we want the struture more general
    // so lab can just extend the structure
    // now just keep the same

    private LinkedList<String> itemIdsInFolder;

    public UserRootFolder(){

        itemIdsInFolder = new LinkedList<>();
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<String> getItemIdsInFolder() {
        return itemIdsInFolder;
    }

    public String getUserFolderId() {
        return userFolderId;
    }

    public void setItemIdsInFolder(LinkedList<String> itemIdsInFolder) {
        this.itemIdsInFolder = itemIdsInFolder;
    }

    public void setUserFolderId(String userFolderId) {
        this.userFolderId = userFolderId;
    }

    public Boolean compare( UserRootFolder  userRootFolder ){
        if( this.itemIdsInFolder.size() != userRootFolder.getItemIdsInFolder().size() ){
            return false;
        }
        for( int i = 0 ; i< this.itemIdsInFolder.size() ; i++ ){
            if( this.itemIdsInFolder.contains( userRootFolder.getItemIdsInFolder().get(i) )==false ){
                return false;
            }
        }
        return true;
    }
}
