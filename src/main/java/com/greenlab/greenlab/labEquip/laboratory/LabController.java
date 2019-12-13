package com.greenlab.greenlab.labEquip.laboratory;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LabController {


    @RequestMapping( value = "/dolab", method = RequestMethod.GET )
    public String getEquipmentBoard(Model model, @PathVariable("equipmentId") String equipmentId, HttpServletRequest request  ){



        return "lab/dolab";

    }






}
