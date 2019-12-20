package com.greenlab.greenlab.labEquip.equipment.equipmentData.equipmentData;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EquipmentDataRepository extends MongoRepository<EquipmentData, String> {



    //ublic Optional<EquipmentData> findById(String id);

    public EquipmentData getById(String id);

    public List<EquipmentData> findAllByOwnerId(String ownerId);

    public List<EquipmentData> findAllByOwnerIdAndAndFavourite(String ownerId, Boolean favourite);

    public List<EquipmentData>  findAllByOwnerIdAndShared(String ownerId, Boolean shared);

    public void deleteById(String id);

   // public List<EquipmentData>

}
