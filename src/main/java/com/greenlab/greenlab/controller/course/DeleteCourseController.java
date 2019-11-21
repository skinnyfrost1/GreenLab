package com.greenlab.greenlab.controller.course;

import javax.servlet.http.HttpServletRequest;

import com.greenlab.greenlab.model.Course;
import com.greenlab.greenlab.model.StuCourse;
import com.greenlab.greenlab.repository.CourseRepository;
import com.greenlab.greenlab.repository.StuCourseRepository;
import com.greenlab.greenlab.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class DeleteCourseController {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StuCourseRepository stuCourseRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "/course/delete")
    public String postDelete(@RequestParam(value = "courseObjectId", required = false) String courseObjectId, ModelMap model,
            HttpServletRequest request) {
        Course course = courseRepository.findBy_id(courseObjectId);
        List<StuCourse> studentCourses = stuCourseRepository.findAllByCourseObjectId(courseObjectId);
        courseRepository.delete(course);
        for(StuCourse s:studentCourses){
            stuCourseRepository.delete(s);
        }
        return "redirect:/courses";
    }

}
