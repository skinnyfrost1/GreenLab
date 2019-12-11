package com.greenlab.greenlab.controller.lab;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.greenlab.greenlab.dto.ResponseEquipment;
import com.greenlab.greenlab.model.Equipment;
import com.greenlab.greenlab.model.Lab;
import com.greenlab.greenlab.repository.EquipmentRepository;
import com.greenlab.greenlab.repository.LabRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CreateLabController{
    @Autowired
    private LabRepository labRepository;

    @Autowired
    private EquipmentRepository equipRepo;

    @GetMapping(value = "/lab/create")
    public String getCreateLabInfo(ModelMap model, HttpServletRequest request){
        if (request.getSession().getAttribute("email") == null)
            return "redirect:/login";
        String role = (String)request.getSession().getAttribute("role");
        if (!role.equals("professor"))
            return "redirect:/login";
        
        String creator = (String) request.getSession().getAttribute("email");
        
        List<Equipment> equipments = equipRepo.findByCreator(creator);
        List<ResponseEquipment> responseEquipments = new ArrayList<>();
        ResponseEquipment tempRE;
        for (Equipment equipment : equipments){
            String image = Base64.getEncoder().encodeToString(equipment.getImage().getData());
            image = "data:image/png;base64," + image;
            System.out.println(image);
            tempRE = new ResponseEquipment();
            tempRE.set_id(equipment.get_id());
            tempRE.setEquipmentName(equipment.getEquipmentName());
            tempRE.setDescription(equipment.getDescription());
            tempRE.setCreator(equipment.getCreator());
            tempRE.setMaterial(equipment.isMaterial());
            tempRE.setBlandable(equipment.isBlandable());
            tempRE.setBlander(equipment.isBlander());
            tempRE.setHeatable(equipment.isHeater());
            tempRE.setHeatable(equipment.isHeatable());
            tempRE.setImage(image);
            responseEquipments.add(tempRE);
        }

        model.addAttribute("equipments", responseEquipments);

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