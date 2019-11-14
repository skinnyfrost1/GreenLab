package com.greenlab.greenlab.controller.course;

import java.util.*;

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
             return "redirect:/index";
         }
         if (request.getSession().getAttribute("role").equals("Student")){
             return "redirect:/index";
         }
         String creator = (String) request.getSession().getAttribute("email");
         List<Course> courses = courseRepository.findByCreator(creator);
        String role = (String) request.getSession().getAttribute("role");
        Course course1 = new Course("BIO 201","Organims to Ecosystems","Spring 2019","Cool course","2019/11/12","Prof Anita",null);
        Course course2 = new Course("BIO 202","Molecular and Cellula Biology","Fall 2019","Cool course","2019/11/12","Prof Anita",null);
        Course course3 = new Course("BIO 203","Cellular and Organ physiology","Spring 2019","Cool course","2019/11/12","Prof Anita",null);
//        List<Course> courses = new ArrayList<>();
//        courses.add(course1);
//        courses.add(course2);
//        courses.add(course3);

        model.addAttribute("courses", courses);
        model.addAttribute("role", role);

        return "profViewCourse";


    }



}
