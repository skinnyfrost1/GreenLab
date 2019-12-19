package com.greenlab.greenlab.controller.student;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.greenlab.greenlab.dto.AddEquipmentToWorkspaceRequestBody;
import com.greenlab.greenlab.dto.AddEquipmentToWorkspaceResponseBody;
import com.greenlab.greenlab.dto.ResponseEquipment;
import com.greenlab.greenlab.dto.StepHintRequestBody;
import com.greenlab.greenlab.dto.StepHintResponseBody;
import com.greenlab.greenlab.lab.LabEquipment;
import com.greenlab.greenlab.lab.Step;
import com.greenlab.greenlab.model.Equipment;
import com.greenlab.greenlab.model.Lab;
import com.greenlab.greenlab.model.StuLab;
import com.greenlab.greenlab.repository.EquipmentRepository;
import com.greenlab.greenlab.repository.LabRepository;
import com.greenlab.greenlab.repository.StuLabRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class StuDoLabController {
    @Autowired
    private LabRepository labRepository;

    @Autowired
    private StuLabRepository stuLabRepo;

    @Autowired
    private EquipmentRepository equipRepo;

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
        List<Lab> enrolledLabs = sl.getEnrolledLabs();
        Lab lab = null;
        for (Lab l : doingLabs) {
            if (l.get_id().equals(_id)) {
                lab = l;
                break;
            }
        }
        if (lab == null) {
            for (Lab l : enrolledLabs) {
                if (l.get_id().equals(_id)) {
                    lab = l.deepClone();
                    List<LabEquipment> equipmentsInLab = new ArrayList<>();
                    lab.setEquipmentsInLab(equipmentsInLab);
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

    @PostMapping("/workspace/stuaddequipment/{_id}")
    public ResponseEntity<?> postWorkspaceAddEquipment(@PathVariable String _id,
            @RequestBody AddEquipmentToWorkspaceRequestBody requestBody, HttpServletRequest request, Errors errors) {
        System.out.println("lab/dolab/workspace/addequipment/" + _id);
        AddEquipmentToWorkspaceResponseBody result = new AddEquipmentToWorkspaceResponseBody();
        if (errors.hasErrors()) {
            result.setMessage(
                    errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(result);
        }
        if (request.getSession().getAttribute("email") == null) {
            result.setMessage("Please Login.");
            return ResponseEntity.badRequest().body(result);
        }
        if (!request.getSession().getAttribute("role").equals("student")) {
            result.setMessage("Only Student can add equipment to lab.");
            return ResponseEntity.badRequest().body(result);
        }
        // find current lab
        String email = (String) request.getSession().getAttribute("email");
        StuLab sl = stuLabRepo.findByStudentEmail(email);
        List<Lab> labs = sl.getDoingLabs();
        Lab lab = null;

        for (Lab l : labs) {
            if (l.get_id().equals(_id)) {
                lab = l;
            }
        }
        if (lab != null) {
            // find the equipment we are going to create on workspace
            Equipment equipment = equipRepo.findBy_id(requestBody.getEquipment_id());
            // create a new labEquipment
            String htmlid = requestBody.getHtmlid();
            String nickname = requestBody.getNickname();
            LabEquipment newLabEquipment = new LabEquipment(equipment, htmlid, nickname);

            // put the labEquipment into current lab.
            List<LabEquipment> equipmentsInLab = lab.getEquipmentsInLab();
            if (equipmentsInLab != null) {
                equipmentsInLab.add(newLabEquipment);
                System.out.println("add new lab equipment to existing list");
            } else {
                List<LabEquipment> newEquipmentsInLab = new ArrayList<>();
                newEquipmentsInLab.add(newLabEquipment);
                lab.setEquipmentsInLab(newEquipmentsInLab);
                System.out.println("create new lab equipment for new list");
            }
            stuLabRepo.save(sl);
            result.setLabEquipment(newLabEquipment);
            return ResponseEntity.ok(result);
        } else
            return ResponseEntity.badRequest().body("error");
    }

    @PostMapping("/workspace/stu/getstepinfo/{_id}")
    public ResponseEntity<?> postWorkspaceStuGetstepinfo(@PathVariable String _id,
    @RequestBody StepHintRequestBody requestBody, HttpServletRequest request, Errors errors) {
        System.out.println("----------------/workspace/stu/getstepinfo/" + _id);
        StepHintResponseBody result = new StepHintResponseBody();
        if (errors.hasErrors()) {
            result.setMessage(
                    errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(result);
        }
        if (request.getSession().getAttribute("email") == null) {
            result.setMessage("Please Login.");
            return ResponseEntity.badRequest().body(result);
        }
        if (!request.getSession().getAttribute("role").equals("student")) {
            result.setMessage("Only Student can add equipment to lab.");
            return ResponseEntity.badRequest().body(result);
        }
        String email = (String) request.getSession().getAttribute("email");
        Lab lab = findDoingLabByEmailAnd_id(email, _id);
        if (lab != null) {
            int stepnumber = requestBody.getStepnumber();
            stepnumber--;
            Step step = lab.getSteps().get(stepnumber);
            String hint = step.getHint();
            result.setHint(hint);
            result.setMessage("!Success");
            return ResponseEntity.ok(result);
        } else
            System.out.println("can't find lab");
            return ResponseEntity.badRequest().body("error");
    }















    public Lab findDoingLabByEmailAnd_id(String email, String _id) {
        Lab lab = null;
        StuLab sl = stuLabRepo.findByStudentEmail(email);
        if (sl != null) {
            for (Lab l : sl.getDoingLabs()) {
                if (l.get_id().equals(_id)) {
                    lab = l;
                    return lab;
                }
            }
        }
        return null;
    }

    public Step findStepByStepNumber(List<Step> steps, int stepnumber) {
        if (steps != null) {
            for (Step s : steps) {

            }
        }

        return null;
    }

}