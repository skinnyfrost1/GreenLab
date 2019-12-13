package com.greenlab.greenlab.controller.lab;

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
        AddEquipmentToWorkspaceResponseBody result = new AddEquipmentToWorkspaceResponseBody();
        if (errors.hasErrors()) {
            result.setMessage(
                    errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(result);
        }
        if (request.getSession().getAttribute("email") == null){
            result.setMessage("Please Login.");
            return ResponseEntity.badRequest().body(result);
        }
        if (!request.getSession().getAttribute("role").equals("professor")) {
            result.setMessage("Only Professor can add equipment to lab.");
            return ResponseEntity.badRequest().body(result);
        }

        Lab lab = labRepo.findBy_id(_id);
        List<LabEquipment> equipmentsInLab = lab.getEquipmentsInLab();
        Equipment equipment = equipRepo.findBy_id(requestBody.getEquipment_id());
        LabEquipment newLabEquipment = new LabEquipment(equipment, requestBody.getNameInWorkspace());
        equipmentsInLab.add(newLabEquipment);
        result.setLabEquipment(newLabEquipment);
        return ResponseEntity.ok(result);
    }
}