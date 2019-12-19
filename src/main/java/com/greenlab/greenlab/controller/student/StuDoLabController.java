package com.greenlab.greenlab.controller.student;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.greenlab.greenlab.dto.ResponseEquipment;
import com.greenlab.greenlab.model.Equipment;
import com.greenlab.greenlab.model.Lab;
import com.greenlab.greenlab.model.StuLab;
import com.greenlab.greenlab.repository.LabRepository;
import com.greenlab.greenlab.repository.StuLabRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class StuDoLabController {
    @Autowired
    private LabRepository labRepository;

    @Autowired
    private StuLabRepository stuLabRepo;

    @GetMapping(value = "/lab/dolab/workspace/{_id}")
    public String getLabCreateWorkspace(@PathVariable String _id, HttpServletRequest request, ModelMap model) {
        if (request.getSession().getAttribute("email") == null)
            return "redirect:/login";
        if (!request.getSession().getAttribute("role").equals("student")) {
            return "redirect:/login";
        }
        String email = (String) request.getSession().getAttribute("email");
        System.out.println("[GET]/lab/dolab/workspace/" + _id);
        StuLab sl = stuLabRepo.findByStudentEmail(email);
        List<Lab> doingLabs = sl.getDoingLabs();
        List<Lab> enrolledLabs= sl.getEnrolledLabs();
        Lab lab = null;
        for (Lab l : doingLabs) {
            if (l.get_id().equals(_id)) {
                lab = l;
                break;
            }
        }
        if (lab==null){
            for (Lab l : enrolledLabs){
                if (l.get_id().equals(_id)){
                    lab = l.deepClone();
                    doingLabs.add(lab);
                    stuLabRepo.save(sl);
                    break;
                }
            }
        }
        if (lab != null) {

            sl.getDoingLabs().add(lab);

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
            return "studentDoLab";
        } else {
            model.addAttribute("errormsg", "can't find the lab" + _id);
            return "error";
        }
    }

}