package com.greenlab.greenlab.labEquip.laboratory.labData;


import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LabDataRepository extends MongoRepository<LabData, String> {


    public LabData getById( String id );

    public List<LabData> findByOwnerId( String ownerId );


}
