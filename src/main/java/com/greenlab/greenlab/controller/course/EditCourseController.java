package com.greenlab.greenlab.controller.course;

import com.greenlab.greenlab.model.Course;
import com.greenlab.greenlab.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class EditCourseController{

    @Autowired
    private CourseRepository courseRepository;
    @GetMapping(value = "/course/edit")
    public String getCreateCourse(ModelMap model, @RequestParam(value = "course", required = false) String cou,
                                  HttpServletRequest request) {
//        if (request.getSession().getAttribute("email") == null)
//            return "redirect:/login";
//        Course course

        return "profEditCourse";
    }

    @GetMapping("/profEditCourse")
    public String profEditCourse(@RequestParam(value = "id") String id,
                                 ModelMap model,
                                 HttpServletRequest request) {
//        if (request.getSession().getAttribute("email") == null)
//            return "redirect:/login";
        Course course = courseRepository.findByCourseId(id);

        System.out.println(id);

        return "profEditCourse";
    }



}