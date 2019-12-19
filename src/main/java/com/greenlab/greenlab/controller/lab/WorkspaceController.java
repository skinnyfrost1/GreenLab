package com.greenlab.greenlab.controller.lab;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.greenlab.greenlab.dto.AddEquipmentToWorkspaceRequestBody;
import com.greenlab.greenlab.dto.AddEquipmentToWorkspaceResponseBody;
import com.greenlab.greenlab.dto.AddStepRequestBody;
import com.greenlab.greenlab.dto.AddStepResponseBody;
import com.greenlab.greenlab.dto.NewLooksResponseBody;
import com.greenlab.greenlab.dto.ResponseEquipment;
import com.greenlab.greenlab.dto.SingleStringRequestBody;
import com.greenlab.greenlab.lab.LabEquipment;
import com.greenlab.greenlab.lab.LabMaterials;
import com.greenlab.greenlab.lab.Step;
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

    @PostMapping(value = "/lab/create/workspace/getnewlooks")
    public ResponseEntity<?> getMethodName(@RequestBody SingleStringRequestBody reqBody, HttpServletRequest request,
            Errors errors) {
        NewLooksResponseBody result = new NewLooksResponseBody();
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
        String _id = reqBody.getStr();
        Lab lab = labRepo.findBy_id(_id);
        List<Equipment> preparedEquipments = lab.getPreparedEquipment();
        List<Equipment> solutionEquipments = new ArrayList<>();
        for (Equipment equipment : preparedEquipments) {
            if (equipment.isSolution()) {
                solutionEquipments.add(equipment);
                System.out.println("length of equiopment=" + solutionEquipments.size());
            }
        }

        List<ResponseEquipment> equipments = new ArrayList<>();
        ResponseEquipment tempRE;
        if (solutionEquipments != null) {
            for (Equipment equipment : solutionEquipments) {
                if (!equipment.isSolution())
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
        result.setMessage("Success!");
        result.setResEquipments(equipments);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/lab/create/workspace/addstep/")
    public ResponseEntity<?> postLabCreateWorkspaceAddstep(@RequestBody AddStepRequestBody reqBody,
            HttpServletRequest request, Errors errors) {

        AddStepResponseBody result = new AddStepResponseBody();

        // -----------------begin of debug------------------------------
        // System.out.println("_id=" + reqBody.get_id());
        // System.out.println("NewLookS_id= " + reqBody.getNewLookS_id());
        // System.out.println("NewLookA_id= " + reqBody.getNewLookA_id());
        // System.out.println("StepNumber= " + reqBody.getStepnumber());
        // System.out.println("hint = " + reqBody.getHint());
        // for (Integer i : reqBody.getSelectedData_quantity()){
        // System.out.println(i);
        // }

        // if (reqBody.getSelectedData().isMaterial()){
        // System.out.println("htmlid="+reqBody.getSelectedData().getHtmlid());
        // }

        // if (reqBody.getSelectedData().isMaterial()){
        // System.out.println("yes!!material!!");
        // }
        // if (reqBody.getSelectedData_htmlid()==null){
        // System.out.println("htmlid=null");
        // }
        // else System.out.println("htmlid="+reqBody.getSelectedData_htmlid());

        // -----------------end of debug ------------------------------
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

        LabEquipment selectedData = labEquipmentFactory(reqBody.getSelectedData(), reqBody.getSelectedData_material(),
                reqBody.getSelectedData_quantity(), reqBody.getSelectedData_unit());
        System.out.println(selectedData.toString());

        LabEquipment associatedData = labEquipmentFactory(reqBody.getAssociatedData(),
                reqBody.getAssociatedData_material(), reqBody.getAssociatedData_quantity(),
                reqBody.getAssociatedData_unit());
        System.out.println(associatedData.toString());

        List<LabMaterials> solutionMaterialsS = labMaterialsFactory(reqBody.getSolutionMaterialsS_material(),
                reqBody.getSolutionMaterialsS_quantity(), reqBody.getSolutionMaterialsS_unit());

        if (solutionMaterialsS != null) {
            System.out.println("-------------solutionMaterialsS -----------size=" + solutionMaterialsS.size());
            for (LabMaterials l : solutionMaterialsS) {
                System.out.println(l);
            }
        }

        List<LabMaterials> solutionMaterialsA = labMaterialsFactory(reqBody.getSolutionMaterialsA_material(),
                reqBody.getSolutionMaterialsA_quantity(), reqBody.getSolutionMaterialsA_unit());

        if (solutionMaterialsA != null) {
            System.out.println("--------------solutionMaterialsA -----------size=" + solutionMaterialsA.size());

            for (LabMaterials l : solutionMaterialsA) {
                System.out.println(l);
            }
        }

        String _id = reqBody.get_id();
        Lab lab = labRepo.findBy_id(_id);
        List<Step> steps = lab.getSteps();
        // create a new List.
        if (steps == null) {
            System.out.println("Create a new step list");
            steps = new ArrayList<>();
            lab.setSteps(steps);
        }
        Step currentStep = new Step(selectedData, associatedData, solutionMaterialsS, solutionMaterialsA,
                reqBody.getNewLookS_id(), reqBody.getNewLookA_id(), reqBody.getStepnumber(), reqBody.getHint());
        steps.add(currentStep);

        LabEquipment labEquipS = null;
        LabEquipment labEquipA = null;
        String imageS = null;
        String imageA = null;

        List<LabEquipment> labequipments = lab.getEquipmentsInLab();
        // // processing SlectedData
        LabEquipment les = findLabEquipmentByHtmlid(labequipments, currentStep.getSelectedData().getHtmlid());
        if (les != null) {
            System.out.println("NewlookS_id =" + currentStep.getNewLookS_id());
            if (currentStep.getNewLookS_id() == null || currentStep.getNewLookS_id().length() == 0) {
                if (currentStep.getSolutionMaterialsS() == null || currentStep.getSolutionMaterialsS().size() == 0) {
                    les.setMaterials(currentStep.getSelectedData().getMaterials());
                } else {
                    // les = currentStep.getSelectedData();
                    les.setMaterials(currentStep.getSolutionMaterialsS());
                }
            }
            // new picture.
            else {
                Equipment newLookEquipment = equipRepo.findBy_id(currentStep.getNewLookS_id());
                imageS = Base64.getEncoder().encodeToString(newLookEquipment.getImage().getData());
                imageS = "data:image/png;base64," + imageS;
                // System.out.println(imageS);
                LabEquipment newLookLabEquipment = new LabEquipment(newLookEquipment,
                        currentStep.getSelectedData().getHtmlid(), currentStep.getSelectedData().getNickname());
                if (currentStep.getSolutionMaterialsS() == null || currentStep.getSolutionMaterialsS().size() == 0) {
                    newLookLabEquipment.setMaterials(currentStep.getSelectedData().getMaterials());
                } else {
                    newLookLabEquipment.setMaterials(currentStep.getSolutionMaterialsS());
                }
                les.copy(newLookLabEquipment);
            }
            labEquipS = les;
        }

        // // Processing AssociatedData
        LabEquipment lea = findLabEquipmentByHtmlid(labequipments, currentStep.getAssociatedData().getHtmlid());
        if (lea != null) {
            if (currentStep.getNewLookA_id() == null || currentStep.getNewLookA_id().length() == 0) {
                if (currentStep.getSolutionMaterialsA() == null || currentStep.getSolutionMaterialsA().size() == 0) {
                    lea.setMaterials(currentStep.getAssociatedData().getMaterials());
                } else {
                    // lea = currentStep.getAssociatedData();
                    lea.setMaterials(currentStep.getSolutionMaterialsA());
                }
            } else {
                Equipment newLookEquipment = equipRepo.findBy_id(currentStep.getNewLookA_id());
                imageA = Base64.getEncoder().encodeToString(newLookEquipment.getImage().getData());
                imageA = "data:image/png;base64," + imageA;
                LabEquipment newLookLabEquipment = new LabEquipment(newLookEquipment,
                        currentStep.getAssociatedData().getHtmlid(), currentStep.getAssociatedData().getNickname());
                if (currentStep.getSolutionMaterialsA() == null || currentStep.getSolutionMaterialsA().size() == 0) {
                    newLookLabEquipment.setMaterials(currentStep.getAssociatedData().getMaterials());
                } else {
                    newLookLabEquipment.setMaterials(currentStep.getSolutionMaterialsA());
                }
                lea.copy(newLookLabEquipment);
            }
            labEquipA = lea;
        }
        
        currentStep.setEquipmentsInLab(labequipments);
        labRepo.save(lab);
        result.setLabEquipS(labEquipS);
        result.setLabEquipA(labEquipA);
        result.setImageS(imageS);
        result.setImageA(imageA);
        result.setMessage("Success!");
        return ResponseEntity.ok(result);
    }

    public LabEquipment findLabEquipmentByHtmlid(List<LabEquipment> labEquipments, String htmlid) {
        for (LabEquipment l : labEquipments) {
            if (l.getHtmlid().equals(htmlid)) {
                return l;
            }
        }
        return null;
    }

    public LabEquipment labEquipmentFactory(LabEquipment selectedData, List<String> material, List<Integer> quantity,
            List<String> unit) {
        LabEquipment le = new LabEquipment();
        le.setEquipment_id(selectedData.getEquipment_id());
        le.setHtmlid(selectedData.getHtmlid());
        le.setNickname(selectedData.getNickname());
        le.setMaterial(selectedData.isMaterial());
        le.setBlandable(selectedData.isBlandable());
        le.setBlander(selectedData.isBlander());
        le.setHeatable(selectedData.isHeatable());
        le.setHeater(selectedData.isHeater());

        LabMaterials buffer;
        List<LabMaterials> materials = new ArrayList<>();
        for (int i = 0; i < material.size(); i++) {
            buffer = new LabMaterials(material.get(i), quantity.get(i), unit.get(i));
            materials.add(buffer);
        }
        le.setMaterials(materials);
        return le;

    }

    public List<LabMaterials> labMaterialsFactory(List<String> material, List<Integer> quantity, List<String> unit) {
        LabMaterials buffer;
        List<LabMaterials> materials = new ArrayList<>();
        for (int i = 0; i < material.size(); i++) {
            buffer = new LabMaterials(material.get(i), quantity.get(i), unit.get(i));
            materials.add(buffer);
        }
        return materials;
    }
}