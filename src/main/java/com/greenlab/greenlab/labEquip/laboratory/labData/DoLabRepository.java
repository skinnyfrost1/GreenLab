

package com.greenlab.greenlab.labEquip.laboratory.labData;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DoLabRepository extends MongoRepository<DoLab, String> {

    public DoLab getById(String id);

    public List<DoLab> getAllByCreator(String creator );

}



