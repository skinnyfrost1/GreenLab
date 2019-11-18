package com.greenlab.greenlab.controller.student;

import com.greenlab.greenlab.model.Course;
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
public class StuSearchCourseController{
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StuCourseRepository stuCourseRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping(value="/course/search")
    public String getCourses(ModelMap model, HttpServletRequest request) {
        String email = (String)request.getSession().getAttribute("email");
        if (request.getSession().getAttribute("email")==null){
            return "redirect:/index";
        }
        User student = userRepository.findByEmail(email);
        return "studentSearchCourse";
    }

    @PostMapping(value="/course/search")
    public String postCourses(@RequestParam (value = "courseName", required = false) String courseId,
            @RequestParam(value = "semester",required = false) String semester,
            ModelMap model, HttpServletRequest request) {
        String email = (String)request.getSession().getAttribute("email");
        if (request.getSession().getAttribute("email")==null){
            return "redirect:/index";
        }
        courseId = courseId.replaceAll(" ","");
        courseId = courseId.toUpperCase();
        System.out.println("Course id is " + courseId);
        User student = userRepository.findByEmail(email);
        List<Course> courses = courseRepository.findByCourseId(courseId);
        model.addAttribute("courses",courses);
        return "studentEnrollCourse";
    }



    
}