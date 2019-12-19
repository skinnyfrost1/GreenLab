package com.greenlab.greenlab.repository;

import java.util.List;

import com.greenlab.greenlab.model.Equipment;
import com.greenlab.greenlab.model.StuLab;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface StuLabRepository extends MongoRepository<StuLab, String> {
    StuLab findBy_id(String _id);
    StuLab findByStudentEmail(String studentEmail);
}