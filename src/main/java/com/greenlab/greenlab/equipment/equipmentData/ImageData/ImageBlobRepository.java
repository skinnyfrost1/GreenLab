package com.greenlab.greenlab.equipment.equipmentData.ImageData;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImageBlobRepository extends MongoRepository<ImageBlob , String> {


    public ImageBlob getById(String id);

    public void deleteById(String id);


}
