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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CreateLabController {
    @Autowired
    private LabRepository labRepository;

    @Autowired
    private EquipmentRepository equipRepo;

    @GetMapping(value = "/lab/create")
    public String getCreateLabInfo(ModelMap model, HttpServletRequest request) {
        if (request.getSession().getAttribute("email") == null)
            return "redirect:/login";
        String role = (String) request.getSession().getAttribute("role");
        if (!role.equals("professor"))
            return "redirect:/login";
        String creator = (String) request.getSession().getAttribute("email");
        List<Equipment> equipments = equipRepo.findByCreator(creator);
        List<ResponseEquipment> responseEquipments = new ArrayList<>();
        ResponseEquipment tempRE;
        for (Equipment equipment : equipments) {
            String image = Base64.getEncoder().encodeToString(equipment.getImage().getData());
            image = "data:image/png;base64," + image;
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

        model.addAttribute("equipments", responseEquipments);

        return "profCreateLabInfo";
    }

    @PostMapping(value = "/lab/create")
    public String postCreateLabInfo(@RequestParam(value = "courseId") String courseId,
            @RequestParam(value = "labName") String labName,
            @RequestParam(value = "labDescription") String labDiscription,
            @RequestParam(value = "createLabEquipSelected") List<String> createLabEquipSelected, ModelMap model,
            HttpServletRequest request) {
        if (request.getSession().getAttribute("email") == null)
            return "redirect:/login";
        if (!request.getSession().getAttribute("role").equals("professor"))
            return "redirect:/login";

        List<Equipment> preparedEquipment = new ArrayList<>();
        for (String str : createLabEquipSelected) {
            Equipment equipment = equipRepo.findBy_id(str);
            if (equipment != null) {
                preparedEquipment.add(equipment);
            }
        }
        String creator = (String) request.getSession().getAttribute("email");
        courseId = courseId.replaceAll(" ", "");
        courseId = courseId.toUpperCase();
        labName = labName.replaceAll(" ", "");
        labName = labName.toUpperCase();
        // String id = courseId + labName + creator;
        // Lab lab = new Lab(id, courseId, labName, labDiscription, creator,
        // preparedEquipment);
        Lab lab = new Lab(courseId, labName, labDiscription, creator, preparedEquipment);
        labRepository.save(lab);
        System.out.println("lab.getId=" + lab.get_id());
        return "redirect:/lab/create/workspace/" + lab.get_id();
    }

    @GetMapping(value = "/lab/create/workspace/{_id}")
    public String getLabCreateWorkspace(@PathVariable String _id, HttpServletRequest request, ModelMap model) {
        if (request.getSession().getAttribute("email") == null)
            return "redirect:/login";
        if (!request.getSession().getAttribute("role").equals("professor")) {
            return "redirect:/login";
        }

        System.out.println("[GET]create/workspace/" + _id);
        Lab lab = labRepository.findBy_id(_id);
        if (lab != null) {
            List<Equipment> preparedEquipment = lab.getPreparedEquipment();
            List<ResponseEquipment> equipments = new ArrayList<>();
            ResponseEquipment tempRE;
            if (preparedEquipment != null) {
                for (Equipment equipment : preparedEquipment) {
                    if (equipment.isSolution())
                        continue;
                    String image = Base64.getEncoder().encodeToString(equipment.getImage().getData());
                    image = "data:image/png;base64," + image;
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
                    equipments.add(tempRE);
                }
            }
            model.addAttribute("preparedEquipments", equipments);
            model.addAttribute("lab", lab);
            return "profCreateLab";
        }
        else{
            model.addAttribute("errormsg","can't find the lab"+_id);
            return "error";
        }

        
    }

}