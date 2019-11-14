package com.greenlab.greenlab.controller.user;

import javax.servlet.http.HttpServletRequest;

import com.greenlab.greenlab.miscellaneous.PasswordChecker;
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

    @GetMapping(value = "/index")
    public String getLogin(ModelMap model, HttpServletRequest request) {
        if (request.getSession().getAttribute("email") != null) {
            String role = (String) request.getSession().getAttribute("role");
            if (role.equals("student")) {
                return "profViewCourse";
            } else {
                return "profViewCourse";
            }
        } else
            return "index";

    }

    @PostMapping(value = "/index")
    public String postLogin(@RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "password", required = false) String password,
             ModelMap model, HttpServletRequest request) {

        System.out.println("[post/login]email=" + email + " password=" + password);
        User user = userRepository.findByEmail(email);
        request.getSession().setAttribute("role",user.getRole());

        if (user != null) {
            // check passwor d
            String encryptedPassword = PasswordChecker.encryptSHA512(password);
            if ((user.getPassword()).equals(encryptedPassword)) {
                System.out.println("password correct");
                System.out.println(encryptedPassword);

//                request.getSession().setAttribute("role",role);
                request.getSession().setAttribute("email",email);

                // Todo: Check the role of the user. if stu, return stuviewcourse. if prof,
                // return profViewCourse
                return "redirect:/courses";
            } else {
                System.out.print("password incorrect");
            }
        } else {
            System.out.println("user = null");
        }
        return "index";
    }

    @PostMapping(value = "/logout")
    public String postLogout(ModelMap model, HttpServletRequest request) {

        return "login";
    }

}
