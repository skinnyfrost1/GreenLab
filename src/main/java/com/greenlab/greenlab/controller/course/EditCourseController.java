package com.greenlab.greenlab.controller.course;

import com.greenlab.greenlab.model.Course;
import com.greenlab.greenlab.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class EditCourseController{

    @Autowired
    private CourseRepository courseRepository;
    @GetMapping(value = "/course/edit")
    public String getCreateCourse(ModelMap model, @RequestParam(value = "id") String id,
                                  HttpServletRequest request) {
//        if (request.getSession().getAttribute("email") == null)
//            return "redirect:/login";
//        Course course
        Course course = courseRepository.findByCourseId(id);
        model.addAttribute("course",course);
        System.out.println(id);

        return "profEditCourse";
    }

    @GetMapping("/profEditCourse")
    public String profEditCourse(@RequestParam(value = "id") String id,
                                 ModelMap model,
                                 HttpServletRequest request) {
//        if (request.getSession().getAttribute("email") == null)
//            return "redirect:/login";
        Course course = courseRepository.findByCourseId(id);
        model.addAttribute("course",course);
        System.out.println(id);

        return "profEditCourse";
    }

    @PostMapping(value = "/course/edit")
    public String postEditCourse(@RequestParam(value = "courseId", required = false) String courseId,
                                   @RequestParam(value = "courseName", required = false) String courseName,
                                   @RequestParam(value = "semester", required = false) String semester,
                                   @RequestParam(value = "courseDescription", required = false) String courseDescription, ModelMap model,
                                   HttpServletRequest request) {

//        String creator = (String) request.getSession().getAttribute("email");
        Course course = courseRepository.findByCourseId(courseId);
        course.setCourseName(courseName);
        course.setSemester(semester);
        course.setCourseDescription(courseDescription);
        System.out.println(course.get_id());
        courseRepository.save(course);
        return "redirect:/courses";
    }




}