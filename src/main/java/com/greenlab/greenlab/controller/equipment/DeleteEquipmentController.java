package com.greenlab.greenlab.controller.equipment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.greenlab.greenlab.dto.ResponseEquipment;
import com.greenlab.greenlab.model.Equipment;
import com.greenlab.greenlab.model.Lab;
import com.greenlab.greenlab.repository.EquipmentRepository;

import com.greenlab.greenlab.repository.LabRepository;
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
public class DeleteEquipmentController {
    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private LabRepository labRepository;


//    @GetMapping(value = "/equipment/delete")
//    public String getEquipmentCreate(HttpServletRequest request, ModelMap model) {
//        String role = (String) request.getSession().getAttribute("role");
//        if (role == null)
//            return "redirect:/index";
//        if (!role.equals("professor")) {
//            model.addAttribute("errormsg", "Only professors can create new courses");
//            return "error";
//        }
//        if (request.getSession().getAttribute("email") == null)
//            return "redirect:/login";
//
//
//        String creator = (String) request.getSession().getAttribute("email");
//        List<Equipment> equipments = equipmentRepository.findByCreator(creator);
//        List<ResponseEquipment> responseEquipments = new ArrayList<>();
//        ResponseEquipment tempRE;
//        for (Equipment equipment : equipments) {
//            String image = Base64.getEncoder().encodeToString(equipment.getImage().getData());
//            image = "data:image/png;base64," + image;
//            // System.out.println(image);
//            tempRE = new ResponseEquipment();
//            tempRE.set_id(equipment.get_id());
//            tempRE.setEquipmentName(equipment.getEquipmentName());
//            tempRE.setDescription(equipment.getDescription());
//            tempRE.setCreator(equipment.getCreator());
//            tempRE.setMaterial(equipment.isMaterial());
//            tempRE.setBlandable(equipment.isBlandable());
//            tempRE.setBlander(equipment.isBlander());
//            tempRE.setHeatable(equipment.isHeater());
//            tempRE.setHeatable(equipment.isHeatable());
//            tempRE.setImage(image);
//            responseEquipments.add(tempRE);
//        }
//
//        model.addAttribute("equipments", responseEquipments);
//
//        return "profDeleteEquipment";
//    }

    @PostMapping(value = "/equipment/delete")
    public String postCreateLabInfo(@RequestParam(value = "viewEquipmentsList") List<String> equiplist,
                                    HttpServletRequest request) {
        if (request.getSession().getAttribute("email") == null)
            return "redirect:/login";
        if (!request.getSession().getAttribute("role").equals("professor"))
            return "redirect:/login";

        System.out.println("fkyou1");

        //
        List<Equipment> equipments = new ArrayList<>();
        for (String str : equiplist) {
            System.out.println(str);
//            prof1@greenlab.edu

            Equipment equipment = equipmentRepository.findBy_id(str);
            if (equipment != null) {
                equipments.add(equipment);
                System.out.println("fkyou3");

            }
        }

        for (Equipment equip : equipments) {
            System.out.println("fkyou4");
            equipmentRepository.delete(equip);
        }
        return "redirect:/equipments";

    }
        //

//
//        List<Equipment> preparedEquipment = new ArrayList<>();
//        for (String str : createLabEquipSelected) {
//            Equipment equipment = equipmentRepository.findBy_id(str);
//            if (equipment != null) {
//                preparedEquipment.add(equipment);
//            }
//        }
//        String creator = (String) request.getSession().getAttribute("email");
//        courseId = courseId.replaceAll(" ", "");
//        courseId = courseId.toUpperCase();
//        labName = labName.replaceAll(" ", "");
//        labName = labName.toUpperCase();
//        // String id = courseId + labName + creator;
//        // Lab lab = new Lab(id, courseId, labName, labDiscription, creator,
//        // preparedEquipment);
//        Lab lab = new Lab(courseId, labName, labDiscription, creator, preparedEquipment);
//        labRepository.save(lab);
//        System.out.println("lab.getId=" + lab.get_id());
//        return "redirect:/lab/create/workspace/" + lab.get_id();
//    }
//    @PostMapping(value = "/equipment/create")
//    public String postEquipmentCreate(@RequestParam("equipmentName") String equipmentName,
//                                      @RequestParam("description") String description, @RequestParam("isMaterial") String isMaterialString,
//                                      @RequestParam("unit") String unit, @RequestParam("blander") String blanderString,
//                                      @RequestParam("blandable") String blandableString, @RequestParam("heater") String heaterString,
//                                      @RequestParam("heatable") String heatableString, @RequestParam("solution") String solutionString,
//                                      @RequestParam("image") MultipartFile file, HttpServletRequest request, ModelMap model) throws IOException {
//
//        String role = (String) request.getSession().getAttribute("role");
//        if (role == null)
//            return "redirect:/index";
//        if (!role.equals("professor")) {
//            model.addAttribute("errormsg", "Only professors can create new courses");
//            return "error";
//        }
//        String creator = (String) request.getSession().getAttribute("email");
//        Boolean material = false;
//        Boolean blandable = false;
//        Boolean blander = false;
//        Boolean heatable = false;
//        Boolean heater = false;
//        Boolean solution = false;
//
//        if (isMaterialString.equals("yes"))
//            material = true;
//        else {
//            if (blandableString.equals("yes"))
//                blandable = true;
//            if (blanderString.equals("yes"))
//                blander = true;
//            if (heatableString.equals("yes"))
//                heatable = true;
//            if (heaterString.equals("yes"))
//                heater = true;
//            if (solutionString.equals("yes"))
//                solution = true;
//        }
//
//        Equipment equipment = new Equipment(equipmentName, description, creator, material, unit, blandable, blander, heatable,
//                heater, solution);
//        equipment.setImage(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
//        equipmentRepository.save(equipment);
//
//        return "redirect:/equipments";
//
//        // // debug
//        // model.addAttribute("errormsg", equipment.toString());
//        // return "error";
//    }

}