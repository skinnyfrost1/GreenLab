package com.greenlab.greenlab.controller.student;

import com.greenlab.greenlab.model.Course;
import com.greenlab.greenlab.model.StuCourse;
import com.greenlab.greenlab.model.User;
import com.greenlab.greenlab.repository.CourseRepository;
import com.greenlab.greenlab.repository.StuCourseRepository;
import com.greenlab.greenlab.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class StuEnrollCourseController{
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StuCourseRepository stuCourseRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping(value="/course/enroll")
    public String getEnrollCourses(ModelMap model, HttpServletRequest request) {
        String email = (String)request.getSession().getAttribute("email");
        if (request.getSession().getAttribute("email")==null){
            return "redirect:/index";
        }
        User student = userRepository.findByEmail(email);
        return "studentEnrollCourse";
    }

    @PostMapping(value="/course/enroll")
    public String postEnrollCourses(@RequestParam(value = "courseSelected", required = false) String courseSelected,
            ModelMap model, HttpServletRequest request) {
        String email = (String)request.getSession().getAttribute("email");
        if (request.getSession().getAttribute("email")==null){
            return "redirect:/index";
        }
        User student = userRepository.findByEmail(email);
        Course course = courseRepository.findBy_id(courseSelected);
        List<User> students = course.getStudents();
        if(students!=null){
            students.add(student);
            course.setStudents(students);
        }
        StuCourse stuCourse = new StuCourse(courseSelected,email,course.getCourseId());
        stuCourse.set_id(courseSelected+email);
        stuCourseRepository.save(stuCourse);
        courseRepository.save(course);
        return "redirect:/courses";
    }
    
}