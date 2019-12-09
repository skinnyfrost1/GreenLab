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
            @RequestParam("equipmentDescription") String description, @RequestParam("isMaterial") Boolean isMaterial,
            @RequestParam("blander") Boolean blander, @RequestParam("blandable") Boolean blandable,
            @RequestParam("heater") Boolean heater, @RequestParam("heatable") Boolean heatable,
            @RequestParam("imageFile") MultipartFile file, HttpServletRequest request, ModelMap model)
            throws IOException {

        String role = (String) request.getSession().getAttribute("role");
        if (role == null)
            return "redirect:/index";
        if (!role.equals("professor")) {
            model.addAttribute("errormsg", "Only professors can create new courses");
            return "error";
        }

        String creator = (String) request.getSession().getAttribute("email");
        Equipment equipment = new Equipment(equipmentName, description, creator, isMaterial, blandable, blander,
                heatable, heater);
        equipment.setImage(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
        equipmentRepository.save(equipment);

        // return "profViewEquip";

        //debug
        model.addAttribute("errormsg",equipment.toString());
        return "error";
    }

}