package com.greenlab.greenlab.repository;
import java.util.List;
import com.greenlab.greenlab.model.Course;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourseRepository extends MongoRepository<Course, String> {
//     Course findByCourseId(String courseId);
     List<Course> findByCreator(String creator);
     Course findBy_id(String id);
     List<Course> findByCourseId(String courseId);
     Course findByCourseIdAndCreator(String courseId,String creator);
//     Course findCourseById(String Id);
}