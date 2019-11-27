package com.greenlab.greenlab.controller.course;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.greenlab.greenlab.model.StuCourse;
import com.greenlab.greenlab.repository.CourseRepository;
import com.greenlab.greenlab.model.Course;
import com.greenlab.greenlab.repository.StuCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewCoursesController {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StuCourseRepository stuCourseRepository;

    @GetMapping(value="/courses")
    public String getCourses(ModelMap model, HttpServletRequest request) {
        if (request.getSession().getAttribute("email")==null){
            return "redirect:/index";
        }
        if (request.getSession().getAttribute("role").equals("professor")){
            String creator = (String) request.getSession().getAttribute("email");
            List<Course> courses = courseRepository.findByCreator(creator);
            String role = (String) request.getSession().getAttribute("role");

            model.addAttribute("courses", courses);
            model.addAttribute("role", role);
            return "profViewCourse";

        }else if(request.getSession().getAttribute("role").equals("student")){
            String stuEmail = (String) request.getSession().getAttribute("email");
            List<StuCourse> temp = stuCourseRepository.findAllByStudentEmail(stuEmail);
            String role = (String) request.getSession().getAttribute("role");
            List<Course> courses = new ArrayList<>();
            for(StuCourse s: temp){
                Course c = courseRepository.findBy_id(s.getCourseObjectId());
                courses.add(c);
            }
            model.addAttribute("courses", courses);
            model.addAttribute("role", role);
            System.out.println("role = "+role);

        }
//         String creator = (String) request.getSession().getAttribute("email");
//         List<Course> courses = courseRepository.findByCreator(creator);
//        String role = (String) request.getSession().getAttribute("role");
//        model.addAttribute("courses", courses);
//        model.addAttribute("role", role);

        return "profViewCourse";


    }



}
