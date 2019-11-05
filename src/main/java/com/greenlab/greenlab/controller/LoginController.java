package com.greenlab.greenlab.controller;

import javax.servlet.http.HttpServletRequest;

import com.greenlab.greenlab.model.User;
import com.greenlab.greenlab.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @Autowired
    private UserRepository userRepository;

    
    @GetMapping(value="/login")
    public String getLogin(ModelMap model, HttpServletRequest request) {

        return "login";

    }

    @PostMapping(value="/login")
    public String postLogin(@RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "password", required = false) String password, 
            @RequestParam(value = "role", required = false) String role,
            ModelMap model, HttpServletRequest request) {

        System.out.println("[post/login]email="+email+" password="+password);
        User user = userRepository.findByEmail(email);
        if (user!=null){
            System.out.println("we found user");
        }
        else{
            System.out.println("user = null");
        }
        return "home";
    }


    @PostMapping(value="/logout")
    public String postLogout(ModelMap model, HttpServletRequest request){

        return "login";
    }

}

