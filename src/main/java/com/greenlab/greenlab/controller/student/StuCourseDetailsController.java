package com.greenlab.greenlab.controller.student;

import javax.servlet.http.HttpServletRequest;

import com.greenlab.greenlab.model.Course;
import com.greenlab.greenlab.model.Lab;
import com.greenlab.greenlab.repository.CourseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class StuCourseDetailsController {
    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/course/details")
    public String getCoursesDetails(ModelMap model, @RequestParam(value = "id") String id, HttpServletRequest request) {
        Course course = courseRepository.findBy_id(id);
        String role = (String) request.getSession().getAttribute("role");
        List<Lab> labs = course.getLabs();
        model.addAttribute("role", role);
        model.addAttribute("course", course);
        model.addAttribute("labs", labs);
        return "stuCourseDetails";
    }

    
}