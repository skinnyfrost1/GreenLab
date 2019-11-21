package com.greenlab.greenlab.labEquip.equipment.equipmentData.userEquipment;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserEquipmentRepository extends MongoRepository<UserEquipment, String> {

    public UserEquipment findByUserId(String userId);

    public void deleteByUserId(String userId);

}
