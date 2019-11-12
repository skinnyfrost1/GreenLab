package com.greenlab.greenlab.repository;
import java.util.List;
import com.greenlab.greenlab.model.Course;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourseRepository extends MongoRepository<Course, String> {
    public List<Course> findByCourseId(String courseId);
    public List<Course> findByCreator(String creator);
}