package com.greenlab.greenlab.controller.student;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class StuSearchCourseController{
    @GetMapping(value="/course/search")
    public String getCourses(ModelMap model, HttpServletRequest request) {
        return "studentSearchCourse";
    }
    
}