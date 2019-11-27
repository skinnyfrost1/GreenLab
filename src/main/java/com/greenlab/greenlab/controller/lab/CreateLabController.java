package com.greenlab.greenlab.controller.lab;

import com.greenlab.greenlab.model.Lab;
import com.greenlab.greenlab.repository.LabRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CreateLabController{
    @Autowired
    private LabRepository labRepository;

    @GetMapping(value = "/lab/create")
    public String getCreateLabInfo(ModelMap model, HttpServletRequest request){
        if (request.getSession().getAttribute("email") == null)
            return "redirect:/login";
        return "profCreateLabInfo";
    }

    @PostMapping(value = "/lab/create")
    public String postCreateLabInfo(@RequestParam(value = "courseId")String courseId,
                                    @RequestParam(value = "labName")String labName,
                                    @RequestParam(value = "labDescription")String labDiscription,
                                    ModelMap model, HttpServletRequest request){
        if (request.getSession().getAttribute("email") == null)
            return "redirect:/login";
        String email = (String)request.getSession().getAttribute("email");
        courseId = courseId.replaceAll(" ","");
        courseId = courseId.toUpperCase();
        labName = labName.replaceAll(" ","");
        labName = labName.toUpperCase();
        String id = courseId+labName+email;
        Lab lab = new Lab(id,courseId,labName,labDiscription,email);
        labRepository.save(lab);
        return "profCreateLab";
    }

}