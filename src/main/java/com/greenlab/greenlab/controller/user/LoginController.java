package com.greenlab.greenlab.controller.user;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.greenlab.greenlab.dto.BooleanResponseBody;
import com.greenlab.greenlab.dto.LoginRequestBody;
import com.greenlab.greenlab.miscellaneous.PasswordChecker;
import com.greenlab.greenlab.model.User;
import com.greenlab.greenlab.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/index")
    public String getLogin(ModelMap model, HttpServletRequest request) {
        if (request.getSession().getAttribute("email") != null) {
            return "redirect:/courses";
        } else
            return "index";
    }

    @PostMapping(value = "/index")
<<<<<<< HEAD
    public ResponseEntity<?> postLogin(@Valid @RequestBody LoginRequestBody login, Errors errors, ModelMap model, HttpServletRequest request){
        BooleanResponseBody result = new BooleanResponseBody();
        if (errors.hasErrors()) {
            result.setMessage(
                    errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(result);
        }        
        String email = login.getEmail();
        String password =login.getPassword();
=======
    public String postLogin(@RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "password", required = false) String password,
             ModelMap model, HttpServletRequest request) {

>>>>>>> origin/ann
        System.out.println("[post/login]email=" + email + " password=" + password);
        
        User user = userRepository.findByEmail(email);
<<<<<<< HEAD
        if (user != null) {
            String encryptedPassword = PasswordChecker.encryptSHA512(password);
            if ((user.getPassword()).equals(encryptedPassword)) {
                System.out.println("password correct");     //debug
                System.out.println(encryptedPassword);      //debug
                request.getSession().setAttribute("role",user.getRole());
                request.getSession().setAttribute("email",email);
                result.setMessage("Success!");
                result.setBool(true);
                return ResponseEntity.ok(result);
            }
        }
        result.setMessage("Email and password not match.");
        result.setBool(false);
        System.out.println("Email and password not match.");
        return ResponseEntity.ok(result);
=======
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
>>>>>>> origin/ann
    }
// public String postLogin(@RequestParam(value = "email", required = false) String email,
    //         @RequestParam(value = "password", required = false) String password,
    //         @RequestParam(value = "role", required = false) String role, ModelMap model, HttpServletRequest request) {
    // System.out.println("[post/login]email=" + email + " password=" + password);
    // User user = userRepository.findByEmail(email);
    // request.getSession().setAttribute("role",role);
    // return "redirect:/courses";
    // if (user != null) {
    // // check passwor d
    // String encryptedPassword = PasswordChecker.encryptSHA512(password);
    // if ((user.getPassword()).equals(encryptedPassword)) {
    // System.out.println("password correct");
    // System.out.println(encryptedPassword);
    //
    // request.getSession().setAttribute("role",role);
    // request.getSession().setAttribute("email",email);
    //
    // // Todo: Check the role of the user. if stu, return stuviewcourse. if prof,
    // // return profViewCourse
    // return "profViewCourse";
    // } else {
    // System.out.print("password incorrect");
    // }
    // } else {
    // System.out.println("user = null");
    // }
    // return "home";
    // }

    @PostMapping(value = "/logout")
    public String postLogout(ModelMap model, HttpServletRequest request) {

        return "index";
    }

}
