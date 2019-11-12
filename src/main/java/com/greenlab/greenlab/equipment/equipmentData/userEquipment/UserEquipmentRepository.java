package com.greenlab.greenlab.equipment.equipmentData.userEquipment;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserEquipmentRepository extends MongoRepository<UserEquipment, String> {

    public UserEquipment findByUserId(String userId);

    public UserEquipment findByCurrentEquipmentId(String currentEquipmentId);

    public void deleteById(String userId);

}
