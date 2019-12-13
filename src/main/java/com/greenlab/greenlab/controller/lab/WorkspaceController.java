package com.greenlab.greenlab.controller.lab;

import com.greenlab.greenlab.repository.EquipmentRepository;
import com.greenlab.greenlab.repository.LabRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WorkspaceController{

    @Autowired
    private LabRepository labRepo;

    @Autowired
    private EquipmentRepository equipRepo;

    // @PostMapping("/workspace/addequipment")

    // }


    

}