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
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.greenlab.greenlab.dto.RequestLabMenuResponseBody;
import com.greenlab.greenlab.dto.SingleStringRequestBody;
import com.greenlab.greenlab.model.Course;
import com.greenlab.greenlab.model.Lab;
import com.greenlab.greenlab.model.StuCourse;
import com.greenlab.greenlab.model.User;
import com.greenlab.greenlab.repository.CourseRepository;
import com.greenlab.greenlab.repository.LabRepository;
import com.greenlab.greenlab.repository.StuCourseRepository;
import com.greenlab.greenlab.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @Autowired
    private LabRepository labRepository;

    @GetMapping(value = "/course/create")
    public String getCreateCourse(ModelMap model, HttpServletRequest request) {
        String role = (String) request.getSession().getAttribute("role");
        if (role == null)
            return "redirect:/index";
        if (!role.equals("professor")){
            model.addAttribute("errormsg", "Only professors can create new courses");
            return "error";
        }
        return "profCreateCourse";
    }

    @PostMapping(value = "/course/create")
    public String postCreateCourse(@RequestParam(value = "courseId", required = false) String courseId,
                                   @RequestParam(value = "courseName", required = false) String courseName,
                                   @RequestParam(value = "semester", required = false) String semester,
                                   @RequestParam(value = "courseDescription", required = false) String courseDescription,
                                   @RequestParam(value = "select", required = false) List<String> labNameList,
//                                   @RequestParam(value = "select", required = false) List<String> lab_IdList,
                                   @RequestParam(value = "file", required = false) MultipartFile file, ModelMap model,
                                   HttpServletRequest request) {
        String role = (String) request.getSession().getAttribute("role");
        if (role == null)
            return "redirect:/index";
        if (!role.equals("professor")){
            model.addAttribute("errormsg", "Only professors can create new courses");
            return "error";
        }

        List<Lab> labs = new ArrayList<>();
//        Lab tempLab;
//        for (String _id : lab_IdList){
//            tempLab = labRepository.findBy_id(_id);
//            labs.add(tempLab);
//        }
        List<Lab> tempLab = (List<Lab>) request.getSession().getAttribute("labs");
        for(Lab l : tempLab){
            for(String name : labNameList){
                if (l.getLabName().equals(name)){
                    labs.add(l);
                }
            }
        }
        request.getSession().removeAttribute("labs");


        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        String createDate = dateFormat.format(date);
        String creator = (String) request.getSession().getAttribute("email");
        courseId = courseId.replaceAll(" ","");
        courseId = courseId.toUpperCase();
//        String id = createDate+courseId;
        List<User> students = new ArrayList<>();
        Course course = new Course(courseId, courseName, semester, courseDescription,createDate,creator,students,labs);
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
                            String courseObjectId = course.get_id();
                            if(stuCourseRepository.findByCourseObjectIdAndStudentEmail(courseObjectId,student.getEmail()) != null){
                                continue;
                            }
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

    @PostMapping(value = "/course/create/requestlabmenu")
    public ResponseEntity<?> postRequestLabMenu(@Valid @RequestBody SingleStringRequestBody reqBody, Errors errors, HttpServletRequest request){
        System.out.println(reqBody.getStr());
        RequestLabMenuResponseBody result = new RequestLabMenuResponseBody();
        if (errors.hasErrors()) {
            result.setMessage(
                    errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(result);
        }
        String email = (String) request.getSession().getAttribute("email");
        List<String> labNameList = new ArrayList<>();
//        List<String> lab_IdList = new ArrayList<>();
        String courseId = reqBody.getStr();
        courseId = courseId.replaceAll(" ","");
        courseId = courseId.toUpperCase();
        List<Lab> labs = labRepository.findByCourseId(courseId);
        for (Lab l : labs){
            if (l.getCreator().equals(email)){
                labNameList.add(l.getLabName());
//                lab_IdList.add(l.get_id());
            }
            else{
                labs.remove(l);
            }
        }
        request.getSession().setAttribute("labs", labs);
        result.setLabNameList(labNameList);
//        result.setLab_IdList(lab_IdList);
        result.setMessage("Success!");
        return ResponseEntity.ok(result);
    }
}