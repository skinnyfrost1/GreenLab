package com.greenlab.greenlab.controller.course;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DeleteCourseController {

    @PostMapping(value = "/courses/delete")
    public String psotDelete(@RequestParam(value = "email", required = false) String username,
            @RequestParam(value = "password", required = false) String password, ModelMap model,
            HttpServletRequest request) {

        return null;
    }

}
