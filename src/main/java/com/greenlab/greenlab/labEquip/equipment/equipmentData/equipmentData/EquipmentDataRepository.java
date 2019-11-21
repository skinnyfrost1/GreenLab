package com.greenlab.greenlab.labEquip.equipment.equipmentData.equipmentData;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EquipmentDataRepository extends MongoRepository<EquipmentData , String> {



    //ublic Optional<EquipmentData> findById(String id);

    public EquipmentData getById(String id);

    public void deleteBy(String id);
}
