package com.greenlab.greenlab.controller.course;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.greenlab.greenlab.model.Course;
import com.greenlab.greenlab.model.Lab;
import com.greenlab.greenlab.repository.CourseRepository;
import com.greenlab.greenlab.repository.LabRepository;
import com.greenlab.greenlab.dto.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class EditCourseController{

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private LabRepository labRepository;

    @GetMapping(value = "/course/edit")
    public String getCreateCourse(ModelMap model, @RequestParam(value = "id") String id,
                                  HttpServletRequest request) {
        if (request.getSession().getAttribute("email") == null)
            return "redirect:/login";
        String role =(String) request.getSession().getAttribute("role");
        System.out.println(role);
        Course course = courseRepository.findBy_id(id);
        model.addAttribute("course",course);
        model.addAttribute("role",role);
        List<Lab> labs = course.getLabs();
        model.addAttribute("labs",labs);
        // for(Lab temp : labs){
        //     System.out.println(temp.testString());
        // }
        return "profEditCourse";
    }


    @PostMapping(value = "/course/edit")
    public String postEditCourse(@RequestParam(value = "_id", required = false) String _id,
                                    @RequestParam(value = "courseId", required = false) String courseId,
                                   @RequestParam(value = "courseName", required = false) String courseName,
                                   @RequestParam(value = "semester", required = false) String semester,
                                   @RequestParam(value = "courseDescription", required = false) String courseDescription, ModelMap model,
                                   HttpServletRequest request) {

//        String creator = (String) request.getSession().getAttribute("email");
        Course course = courseRepository.findBy_id(_id);
        System.out.println(course.get_id());
        courseId = courseId.replaceAll(" ","");
        courseId = courseId.toUpperCase();
        course.setCourseId(courseId);
        course.setCourseName(courseName);
        course.setSemester(semester);
        course.setCourseDescription(courseDescription);
//        System.out.println(course.get_id());
        courseRepository.save(course);
        return "redirect:/courses";
    }
    


    //this method is use to delete labs from a course. 
    //input: courseObjectId, labObjectIds 
    @PostMapping(value = "/course/edit/dellabs")
    public ResponseEntity<?> postCourseEditDelLabs(@Valid @RequestBody MultiStringRequestBody reqBody, Errors errors, HttpServletRequest request){
        BooleanResponseBody result = new BooleanResponseBody();
        String message = "Success!\nRemove:\n";
        if (errors.hasErrors()) {
            result.setMessage(
                    errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(result);
        }
        String courseObjectId = reqBody.getCourseObjectId();
        List<String> labObjIds = reqBody.getStrs();

        Course course = courseRepository.findBy_id(courseObjectId);
        List<Lab> labs = course.getLabs();

        for (String s : labObjIds){
            for (Lab l :labs){
                if(l.get_id().equals(s)){
                    message=message+l.getLabName()+"\n";
                    labs.remove(l);
                }
            }
        }
        result.setMessage(message);
        result.setBool(true);
        return ResponseEntity.ok(result);
    }

    //input: courseObjectId,  labObjectIds
    //add selected labs to the course. 
    @PostMapping(value = "/course/edit/addlabs")
    public ResponseEntity<?> postCourseEditAddLabs(@Valid @RequestBody MultiStringRequestBody reqBody, Errors errors, HttpServletRequest request){
        BooleanResponseBody result = new BooleanResponseBody();
        if (errors.hasErrors()) {
            result.setMessage(
                    errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(result);
        }
        String courseObjectId = reqBody.getCourseObjectId();
        List<String> labObjIds = reqBody.getStrs();

        Course course = courseRepository.findBy_id(courseObjectId);
        List<Lab> labs = course.getLabs();

        for (String _id : labObjIds){
            labs.add(labRepository.findBy_id(_id));
            result.setMessage("_id="+_id+"\n");
        }
        return ResponseEntity.ok(result);
    }



    //post 一个courseObjectId  _id， 然后返回一个list of lab name 和lab的object id
    @PostMapping(value="/course/edit/requestlabmenu")
    public ResponseEntity<?> postCourseEditRequestLabMenu(@Valid @RequestBody SingleStringRequestBody reqBody, Errors errors, HttpServletRequest request){
        LabMenuRequestBody result = new LabMenuRequestBody();
        if (errors.hasErrors()) {
            result.setMessage(
                    errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(result);
        }
        String courseObjectId = reqBody.getStr();
        Course course = courseRepository.findBy_id(courseObjectId);
        List<Lab> labs = course.getLabs();

        String courseId = course.getCourseId();
        String creator = (String) request.getSession().getAttribute("email");
        List<Lab> labmenu = labRepository.findByCourseIdAndCreator(courseId, creator);
        
        for (Lab l : labs){
            if (labmenu.contains(l)){
                labmenu.remove(l);
            }
        }
        List<String> labNameList = new ArrayList<>();
        for(Lab l :labmenu){
            labNameList.add("[\""+l.getLabName()+"\", \""+l.get_id()+"\"]");
        }
        return ResponseEntity.ok(result);
    }

}