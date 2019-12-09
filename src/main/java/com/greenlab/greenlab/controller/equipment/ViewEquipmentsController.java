package com.greenlab.greenlab.controller.equipment;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.greenlab.greenlab.model.Equipment;
import com.greenlab.greenlab.repository.EquipmentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewEquipmentsController {

    @Autowired
    private EquipmentRepository equipRepo;

    @GetMapping("/equipments")
    public String getEquipments(HttpServletRequest request, ModelMap model){
        String role = (String) request.getSession().getAttribute("role");
        if (role == null)
            return "redirect:/index";
        if (!role.equals("professor")) {
            model.addAttribute("errormsg", "Only professors can view equipments");
            return "error";
        }

        String creator = (String) request.getSession().getAttribute("email");
        List<Equipment> equipments = equipRepo.findByCreator(creator);

        

    }
    
}