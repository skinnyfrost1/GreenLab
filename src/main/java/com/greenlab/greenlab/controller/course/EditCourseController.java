package com.greenlab.greenlab.controller.course;

import com.greenlab.greenlab.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class EditCourseController{

    @Autowired
    private CourseRepository courseRepository;
    @GetMapping(value = "/course/edit")
    public String getCreateCourse(ModelMap model, HttpServletRequest request) {
//        if (request.getSession().getAttribute("email") == null)
//            return "redirect:/login";
//        Course course
        return "profEditCourse";
    }



}