package com.greenlab.greenlab.controller.equipment;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.greenlab.greenlab.dto.EquipmentsResponseBody;
import com.greenlab.greenlab.dto.ResponseEquipment;
import com.greenlab.greenlab.dto.SingleStringRequestBody;
import com.greenlab.greenlab.model.Equipment;
import com.greenlab.greenlab.repository.EquipmentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
        return "profViewEquip";
    }

    @PostMapping("/equipments")
    public ResponseEntity<?> postEquipments(@RequestBody SingleStringRequestBody requestBody, HttpServletRequest request){
        String role = (String) request.getSession().getAttribute("role");
        String email = (String) request.getSession().getAttribute("email");

        System.out.println("[POST] equipments email="+email+"     role="+role);
        if (!role.equals("professor")|| email==null) {
            return ResponseEntity.badRequest().body("Only professor can view equiopment. please login as a professor. ");
        }
        String creator = (String) request.getSession().getAttribute("email");
        List<Equipment> equipments = equipRepo.findByCreator(creator);

        List<ResponseEquipment> responseEquipments = new ArrayList<>();
        ResponseEquipment tempRE;
        for (Equipment equipment : equipments){
            String image = Base64.getEncoder().encodeToString(equipment.getImage().getData());
            // System.out.println(image);
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

        // result.setEquipments(equipments);
        // result.setMessage("Success!");
        return ResponseEntity.ok(responseEquipments);
    }
    
}