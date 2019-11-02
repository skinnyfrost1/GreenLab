package com.greenlab.greenlab.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewCoursesController {

    @GetMapping(value="/viewcourses")
    public String getLogin(ModelMap model, HttpServletRequest request) {

        return "return";

    }



}