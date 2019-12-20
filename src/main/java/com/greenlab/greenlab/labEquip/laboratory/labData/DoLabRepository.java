

package com.greenlab.greenlab.labEquip.laboratory.labData;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DoLabRepository extends MongoRepository<DoLab, String> {

    public DoLab getBy_id(String _id);

    public List<DoLab> getAllByCreator(String creator );

    public List<DoLab> findAllByCreatorAndCourseId(String creator, String courseId);

    
}



