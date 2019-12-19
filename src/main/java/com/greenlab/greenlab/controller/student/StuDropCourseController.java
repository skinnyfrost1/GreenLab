package com.greenlab.greenlab.controller.student;

import com.greenlab.greenlab.model.Course;
import com.greenlab.greenlab.model.StuCourse;
import com.greenlab.greenlab.model.User;
import com.greenlab.greenlab.repository.CourseRepository;
import com.greenlab.greenlab.repository.StuCourseRepository;
import com.greenlab.greenlab.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class StuDropCourseController{
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StuCourseRepository stuCourseRepository;
    @Autowired
    private UserRepository userRepository;

     @PostMapping("/course/drop")
     public String getCoursesDrop(@RequestParam(value = "courseObjectId", required = false) String courseObjectId,
                                  ModelMap model, HttpServletRequest request){
         String email = (String)request.getSession().getAttribute("email");
         if (request.getSession().getAttribute("email")==null){
             return "redirect:/index";
         }
         User user = userRepository.findByEmail(email);
         StuCourse c = stuCourseRepository.findByCourseObjectIdAndStudentEmail(courseObjectId,email);
         stuCourseRepository.delete(c);
         Course course = courseRepository.findBy_id(courseObjectId);
         List<User> students = course.getStudents();
         students.remove(user);
         course.setStudents(students);
         courseRepository.save(course);
         return "redirect:/courses";
        
     }
}