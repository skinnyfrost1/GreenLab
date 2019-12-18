package com.greenlab.greenlab.repository;
import java.util.List;

import com.greenlab.greenlab.model.StuCourse;
import com.greenlab.greenlab.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StuCourseRepository extends MongoRepository<StuCourse, String>  {
    List<StuCourse> findAllByStudentEmail(String student);
    StuCourse findByCourseObjectIdAndStudentEmail(String courseId,String studentEmail);
    List<StuCourse> findAllByCourseObjectId(String id);
}
