package com.greenlab.greenlab.labEquip.framework.imageBlob;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImageBlobRepository extends MongoRepository<ImageBlob , String> {


    public ImageBlob getById(String id);

    public void deleteById(String id);


}
