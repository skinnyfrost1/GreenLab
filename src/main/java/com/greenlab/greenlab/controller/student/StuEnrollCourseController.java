package com.greenlab.greenlab.controller.student;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.greenlab.greenlab.model.Course;
import com.greenlab.greenlab.model.Lab;
import com.greenlab.greenlab.model.StuCourse;
import com.greenlab.greenlab.model.StuLab;
import com.greenlab.greenlab.model.User;
import com.greenlab.greenlab.repository.CourseRepository;
import com.greenlab.greenlab.repository.StuCourseRepository;
import com.greenlab.greenlab.repository.StuLabRepository;
import com.greenlab.greenlab.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StuEnrollCourseController{
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StuCourseRepository stuCourseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StuLabRepository stuLabRepo;

    @GetMapping(value="/course/enroll")
    public String getEnrollCourses(ModelMap model, HttpServletRequest request) {
        String email = (String)request.getSession().getAttribute("email");
        if (request.getSession().getAttribute("email")==null){
            return "redirect:/index";
        }
        User student = userRepository.findByEmail(email);
        return "studentEnrollCourse";
    }

    @PostMapping(value="/course/enroll")
    public String postEnrollCourses(@RequestParam(value = "courseSelected", required = false) String courseSelected,
            ModelMap model, HttpServletRequest request) {
        String email = (String)request.getSession().getAttribute("email");
        if (request.getSession().getAttribute("email")==null){
            return "redirect:/index";
        }
        User student = userRepository.findByEmail(email);
        Course course = courseRepository.findBy_id(courseSelected);
        List<User> students = course.getStudents();
        if(students!=null){
            students.add(student);
            course.setStudents(students);
        }
        if(stuCourseRepository.findByCourseObjectIdAndStudentEmail(courseSelected,student.getEmail()) != null){
            return "redirect:/courses";
        }
        StuCourse stuCourse = new StuCourse(courseSelected,email,course.getCourseId());
        stuCourse.set_id(courseSelected+email);
        stuCourseRepository.save(stuCourse);
        courseRepository.save(course);
        List<Lab> labs = course.getLabs();
        StuLab sl = stuLabRepo.findByStudentEmail(email);
        for (Lab l :labs){
            sl.getEnrolledLabs().add(l);
        }
        stuLabRepo.save(sl);

        return "redirect:/courses";
    }
    
}