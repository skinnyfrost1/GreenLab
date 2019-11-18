package com.greenlab.greenlab.controller.course;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.multipart.MultipartFile;

@Controller

public class CreateCourseController {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StuCourseRepository stuCourseRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/course/create")
    public String getCreateCourse(ModelMap model, HttpServletRequest request) {
        return "profCreateCourse";
    }

    @PostMapping(value = "/course/create")
    public String postCreateCourse(@RequestParam(value = "courseId", required = false) String courseId,
                                   @RequestParam(value = "courseName", required = false) String courseName,
                                   @RequestParam(value = "semester", required = false) String semester,
                                   @RequestParam(value = "courseDescription", required = false) String courseDescription,
                                   @RequestParam(value = "file", required = false) MultipartFile file, ModelMap model,
                                   HttpServletRequest request) {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        String createDate = dateFormat.format(date);
        String creator = (String) request.getSession().getAttribute("email");
        courseId = courseId.replaceAll(" ","");
        courseId = courseId.toUpperCase();
//        String id = createDate+courseId;
        List<User> students = new ArrayList<>();
        Course course = new Course(courseId, courseName, semester, courseDescription,createDate,creator,students);
        System.out.println(course.toString());
        courseRepository.save(course);
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                ByteArrayInputStream inputFilestream = new ByteArrayInputStream(bytes);
                BufferedReader br = new BufferedReader(new InputStreamReader(inputFilestream ));
                String line = "";
                int count = 0;
                while ((line = br.readLine()) != null) {
                    if(count==0){
                        //First Line
                    }else {
                        String[] columns = line.split(",");
                        System.out.println(columns[2]);
                        String stuID = columns[2];
                        if(userRepository.findByUid(stuID)!=null){
                            User student = userRepository.findByUid(stuID);
                            StuCourse stuCourse = new StuCourse(course.get_id(),student.getEmail(),course.getCourseId());
                            stuCourse.set_id(course.get_id()+student.getEmail());
                            System.out.println(stuCourse.toString());
                            stuCourseRepository.save(stuCourse);
                            students.add(student);
                            course.setStudents(students);
                            courseRepository.save(course);
                        }
                    }
                    count++;

                }
                br.close();
//                String completeData = new String(bytes);
//                String[] rows = completeData.split("#");
//                for (int i = 0; i < rows.length; i++) {
//                    String[] columns = rows[i].split(",");
//                    System.out.println(columns[0] + " " + columns[1] + " " + columns[2]);
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        System.out.println(course.toString());
        courseRepository.save(course);
        return "redirect:/courses";

    }

}