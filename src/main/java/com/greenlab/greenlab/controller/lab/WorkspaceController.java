package com.greenlab.greenlab.controller.lab;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.greenlab.greenlab.dto.AddEquipmentToWorkspaceRequestBody;
import com.greenlab.greenlab.dto.AddEquipmentToWorkspaceResponseBody;
import com.greenlab.greenlab.lab.LabEquipment;
import com.greenlab.greenlab.model.Equipment;
import com.greenlab.greenlab.model.Lab;
import com.greenlab.greenlab.repository.EquipmentRepository;
import com.greenlab.greenlab.repository.LabRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class WorkspaceController {

    @Autowired
    private LabRepository labRepo;

    @Autowired
    private EquipmentRepository equipRepo;

    @PostMapping("/workspace/addequipment/{_id}")
    public ResponseEntity<?> postWorkspaceAddEquipment(@PathVariable String _id,
            @RequestBody AddEquipmentToWorkspaceRequestBody requestBody, HttpServletRequest request, Errors errors) {
        System.out.println("/workspace/addequipment/" + _id);
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
        if (!request.getSession().getAttribute("role").equals("professor")) {
            result.setMessage("Only Professor can add equipment to lab.");
            return ResponseEntity.badRequest().body(result);
        }
        // find current lab
        Lab lab = labRepo.findBy_id(_id);
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
        labRepo.save(lab);
        result.setLabEquipment(newLabEquipment);
        return ResponseEntity.ok(result);
    }
}