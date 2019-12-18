package com.greenlab.greenlab.controller.lab;

import com.greenlab.greenlab.model.Course;
import com.greenlab.greenlab.model.Equipment;
import com.greenlab.greenlab.model.Lab;
import com.greenlab.greenlab.model.StuCourse;
import com.greenlab.greenlab.repository.EquipmentRepository;
import com.greenlab.greenlab.repository.LabRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class EditLabController{

    @Autowired
    private LabRepository labRepository;

    @Autowired
    private EquipmentRepository equipRepo;

    @GetMapping(value="/labs")
    public String getCourses(ModelMap model, HttpServletRequest request) {
        if (request.getSession().getAttribute("email")==null){
            return "redirect:/index";
        }
        if (request.getSession().getAttribute("role").equals("professor")){
            String creator = (String) request.getSession().getAttribute("email");
            List<Lab> labs = labRepository.findByCreator(creator);
            String role = (String) request.getSession().getAttribute("role");

            model.addAttribute("labs", labs);
            model.addAttribute("role", role);
            return "profViewLabs";

        }else if(request.getSession().getAttribute("role").equals("student")){

        }
//         String creator = (String) request.getSession().getAttribute("email");
//         List<Course> courses = courseRepository.findByCreator(creator);
//        String role = (String) request.getSession().getAttribute("role");
//        model.addAttribute("courses", courses);
//        model.addAttribute("role", role);

        return "profViewLabs";


    }
}