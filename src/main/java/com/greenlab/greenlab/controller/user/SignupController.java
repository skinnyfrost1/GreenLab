package com.greenlab.greenlab.controller.user;

import javax.servlet.http.HttpServletRequest;

import com.greenlab.greenlab.model.User;
import com.greenlab.greenlab.repository.UserRepository;
import com.greenlab.greenlab.miscellaneous.PasswordChecker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.greenlab.greenlab.controller.course.ViewCoursesController;


@Controller
public class SignupController {
    // ViewCoursesController viewCoursesController;
    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/signup")
    public String getLogin(ModelMap model, HttpServletRequest request) {
        if (request.getSession().getAttribute("email") != null)
            return "redirect:/courses";
        else
            return "signUp";

    }

    @PostMapping(value = "/signup")
    public String postLogin(@RequestParam(value = "uid", required = false) String uid,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "firstname", required = false) String firstname,
            @RequestParam(value = "lastname", required = false) String lastname,
            @RequestParam(value = "role", required = false) String role, ModelMap model, HttpServletRequest request) {

        if (request.getSession().getAttribute("email") != null)
            return "redirect:/courses";

        System.out.println("[post/login]" + "uid=" + uid + "email=" + email + " password=" + password + " firstname="
                + firstname + " lastname=" + lastname + " role= " + role);
        password = PasswordChecker.encryptSHA512(password);
        User user = new User(uid, email, password, firstname, lastname, role);
        userRepository.save(user);
        return "ddd";
    }



}