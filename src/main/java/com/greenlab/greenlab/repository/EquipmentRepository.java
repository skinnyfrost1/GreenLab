package com.greenlab.greenlab.repository;

import java.util.List;

import com.greenlab.greenlab.model.Equipment;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EquipmentRepository extends MongoRepository<Equipment, String> {
    List<Equipment> findByCreator (String creator);
    Equipment findBy_id(String _id);
    List<Equipment> findByEquipmentName(String equipmentName);
    List<Equipment> findByEquipmentNameContains(String equipmentName);
}