package com.greenlab.greenlab.labEquip.equipment.equipmentData.userEquipment;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserEquipmentFolderRepository extends MongoRepository<UserEquipmentFolder, String> {


    public UserEquipmentFolder findByUserFolderId(String id);

    public void deleteByUserFolderId(String id);


}