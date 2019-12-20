package com.greenlab.greenlab.controller.equipment;

import com.greenlab.greenlab.model.Equipment;
import com.greenlab.greenlab.repository.EquipmentRepository;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CreateAlbumController {
    @Autowired
    private EquipmentRepository equipmentRepository;

    @PostMapping(value = "/equipment/createAlbum")
    public String postCreateLabInfo(@RequestParam(value = "viewEquipmentsList") List<String> equiplist,
                                    HttpServletRequest request)  {
        String role = (String) request.getSession().getAttribute("role");
        if (role == null)
            return "redirect:/index";
        if (!role.equals("professor")){
            return "error";
        }



        return "profCreateCourse";
    }

}