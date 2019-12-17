package com.greenlab.greenlab.controller.equipment;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import com.greenlab.greenlab.model.Equipment;
import com.greenlab.greenlab.repository.EquipmentRepository;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class CreateEquipmentController {
    @Autowired
    private EquipmentRepository equipmentRepository;

    @GetMapping(value = "/equipment/create")
    public String getEquipmentCreate(HttpServletRequest request, ModelMap model) {
        String role = (String) request.getSession().getAttribute("role");
        if (role == null)
            return "redirect:/index";
        if (!role.equals("professor")) {
            model.addAttribute("errormsg", "Only professors can create new courses");
            return "error";
        }

        return "profCreateEquip";
    }

    @PostMapping(value = "/equipment/create")
    public String postEquipmentCreate(@RequestParam("equipmentName") String equipmentName,
            @RequestParam("description") String description, @RequestParam("isMaterial") String isMaterialString,
            @RequestParam("unit") String unit, @RequestParam("blander") String blanderString,
            @RequestParam("blandable") String blandableString, @RequestParam("heater") String heaterString,
            @RequestParam("heatable") String heatableString, @RequestParam("solution") String solutionString,
            @RequestParam("image") MultipartFile file, HttpServletRequest request, ModelMap model) throws IOException {

        String role = (String) request.getSession().getAttribute("role");
        if (role == null)
            return "redirect:/index";
        if (!role.equals("professor")) {
            model.addAttribute("errormsg", "Only professors can create new courses");
            return "error";
        }
        String creator = (String) request.getSession().getAttribute("email");
        Boolean material = false;
        Boolean blandable = false;
        Boolean blander = false;
        Boolean heatable = false;
        Boolean heater = false;
        Boolean solution = false;

        if (isMaterialString.equals("yes"))
            material = true;
        else {
            if (blandableString.equals("yes"))
                blandable = true;
            if (blanderString.equals("yes"))
                blander = true;
            if (heatableString.equals("yes"))
                heatable = true;
            if (heaterString.equals("yes"))
                heater = true;
            if (solutionString.equals("yes"))
                solution = true;
        }

        Equipment equipment = new Equipment(equipmentName, description, creator, material, unit, blandable, blander, heatable,
                heater, solution);
        equipment.setImage(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
        equipmentRepository.save(equipment);

        return "redirect:/equipments";

        // // debug
        // model.addAttribute("errormsg", equipment.toString());
        // return "error";
    }

}