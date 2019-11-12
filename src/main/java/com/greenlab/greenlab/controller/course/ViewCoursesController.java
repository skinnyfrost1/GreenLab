package com.greenlab.greenlab.controller.course;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.greenlab.greenlab.repository.CourseRepository;
import com.greenlab.greenlab.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewCoursesController {
    @Autowired
    private CourseRepository courseRepository;
    
    @GetMapping(value="/courses")
    public String getCourses(ModelMap model, HttpServletRequest request) {
        if (request.getSession().getAttribute("email")==null){
            return "login";
        }
        if (request.getSession().getAttribute("role").equals("Student")){
            return "login";
        }
        String creator = (String) request.getSession().getAttribute("email");
        List<Course> courses = courseRepository.findByCreator(creator);


        
        return null;
        

    }



}