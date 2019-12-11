package com.greenlab.greenlab.controller.equipment;

import com.greenlab.greenlab.model.Course;
import com.greenlab.greenlab.model.Equipment;
import com.greenlab.greenlab.model.User;
import com.greenlab.greenlab.repository.CourseRepository;
import com.greenlab.greenlab.repository.EquipmentRepository;
import com.greenlab.greenlab.repository.StuCourseRepository;
import com.greenlab.greenlab.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;
@Controller
public class SearchEquipmentController {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StuCourseRepository stuCourseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EquipmentRepository equipmentRepository;

    @GetMapping(value="/equipment/search")
    public String getSearchEquipments(ModelMap model, HttpServletRequest request) {
        String email = (String)request.getSession().getAttribute("email");
        if (request.getSession().getAttribute("email")==null){
            return "redirect:/index";
        }
        User student = userRepository.findByEmail(email);
        return "profSearchEquip";
    }

    @PostMapping(value="/equipment/search")
    public String postSearchEquips(@RequestParam(value = "equipName", required = false) String equipName,
                              ModelMap model, HttpServletRequest request) {
        String email = (String)request.getSession().getAttribute("email");
        if (request.getSession().getAttribute("email")==null){
            return "redirect:/index";
        }
        List<Equipment> equipments = equipmentRepository.findByEquipmentNameContains(equipName);
        Iterator<Equipment> it = equipments.iterator();
        while (it.hasNext()) {
            Equipment e = it.next();
            if (e.getCreator().equals(email)) {
                it.remove();
            }
        }

        model.addAttribute("equipments",equipments);
        return "profAddEquip";
    }

    @PostMapping(value="/equipment/add")
    public String postAddEquip(@RequestParam(value = "listOfEquipId", required = false) List<String> listOfEquipId,
                              ModelMap model, HttpServletRequest request) {
        String email = (String)request.getSession().getAttribute("email");
        if (request.getSession().getAttribute("email")==null){
            return "redirect:/index";
        }
        System.out.println(listOfEquipId.size());
        for(int i = 0;i<listOfEquipId.size();i ++){
            Equipment e = equipmentRepository.findBy_id(listOfEquipId.get(i));
            if(!e.getCreator().equals(email)){
                Equipment newE = e.clone();
                newE.setCreator(email);
                equipmentRepository.save(newE);
            }

        }
        return "success";
    }




}
