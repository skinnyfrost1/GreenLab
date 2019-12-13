package com.greenlab.greenlab.labEquip.laboratory.labData;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface DoLabRepository extends MongoRepository<DoLab, String> {


    public DoLab getById( String id );


}
