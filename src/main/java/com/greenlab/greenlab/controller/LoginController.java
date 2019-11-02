package com.greenlab.greenlab.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String getLogin(ModelMap model, HttpServletRequest request) {

        return "login";

    }

    @PostMapping("/login")
    public String postLogin(@RequestParam(value = "email", required = false) String username,
            @RequestParam(value = "password", required = false) String password, ModelMap model,
            HttpServletRequest request) {
        return null;
    }


    @PostMapping("/logout")
    public String postLogout(ModelMap model, HttpServletRequest request){

        return "login";
    }

}

