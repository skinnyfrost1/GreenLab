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
public class SignupController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/signup")
    public String getLogin(ModelMap model, HttpServletRequest request) {

        return "signup";

    }

    @PostMapping(value = "/signup")
    public String postLogin(@RequestParam(value = "uid", required = false) String uid,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "role", required = false) String role, ModelMap model, HttpServletRequest request) {

        User user = new User(uid, email, password, firstName, lastName, role);
        userRepository.save(user);

        return "login";
    }

}