package com.greenlab.greenlab.controller.course;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.greenlab.greenlab.model.Course;
import com.greenlab.greenlab.repository.CourseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class CreateCourseController {
    @Autowired
    private CourseRepository courseRepository;

    @GetMapping(value = "/course/create")
    public String getCreateCourse(ModelMap model, HttpServletRequest request) {
        return "profCreateCourse";
    }

    @PostMapping(value = "/course/create")
    public String postCreateCourse(@RequestParam(value = "courseId", required = false) String courseId,
            @RequestParam(value = "courseName", required = false) String courseName,
            @RequestParam(value = "semester", required = false) String semester,
            @RequestParam(value = "courseDescription", required = false) String courseDescription, ModelMap model,
            HttpServletRequest request) {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        String createDate = dateFormat.format(date);
        String creator = (String) request.getSession().getAttribute("email");
        String id = createDate+courseId;
        Course course = new Course(courseId, courseName, semester, courseDescription,createDate,creator,null);
        System.out.println(course.toString());
        courseRepository.save(course); 
        return "redirect:/courses";

    }

}