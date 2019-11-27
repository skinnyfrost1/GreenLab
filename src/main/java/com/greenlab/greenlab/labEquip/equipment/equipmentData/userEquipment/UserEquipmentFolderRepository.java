package com.greenlab.greenlab.labEquip.equipment.equipmentData.userEquipment;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserEquipmentFolderRepository extends MongoRepository<UserEquipmentFolder, String> {


    public UserEquipmentFolder findByUserFolderId(String id);

    public void deleteByUserFolderId(String id);

    public List<UserEquipmentFolder> findAllByOwner(String owner);

    public UserEquipmentFolder findByOwnerAndType(String owner, String type);
}