package com.greenlab.greenlab.controller.lab;

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

public class DeleteLabController{


    @Autowired
    private LabRepository labRepository;


    @PostMapping(value = "/lab/delete")
    public String postCreateLabInfo(@RequestParam(value = "labsToDelete") List<String> lablist,
                                    HttpServletRequest request) {
        if (request.getSession().getAttribute("email") == null)
            return "redirect:/login";
        if (!request.getSession().getAttribute("role").equals("professor"))
            return "redirect:/login";

        System.out.println("fkyou1");

        //
        List<Lab> labs = new ArrayList<>();
        for (String str : lablist) {
            System.out.println(str);
//            prof1@greenlab.edu

            Lab lab = labRepository.findBy_id(str);
            if (lab != null) {
                labs.add(lab);
                System.out.println("fkyou3");

            }
        }

        for (Lab lab : labs) {
            System.out.println("fkyou4");
            labRepository.delete(lab);
        }
        return "redirect:/labs";

    }
}