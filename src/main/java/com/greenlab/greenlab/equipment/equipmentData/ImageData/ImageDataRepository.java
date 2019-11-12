package com.greenlab.greenlab.equipment.equipmentData.ImageData;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImageDataRepository extends MongoRepository<ImageData , String> {


    //public Optional<ImageData> findById(String id );

    public ImageData getById(String id);


    public void deleteById(String id);

}
