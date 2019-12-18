package com.greenlab.greenlab.repository;
import java.util.List;

import com.greenlab.greenlab.model.Lab;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface LabRepository extends MongoRepository<Lab, String> {
    List<Lab> findByCourseId (String courseId);
    Lab findBy_id(String _id);
    List<Lab> findByCourseIdAndCreator(String courseId, String creator);
    List<Lab> findByCreator(String creator);
}