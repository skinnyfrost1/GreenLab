package com.greenlab.greenlab.controller.user;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.greenlab.greenlab.model.User;
import com.greenlab.greenlab.repository.UserRepository;
import com.greenlab.greenlab.dto.CheckEmailRequestBody;
import com.greenlab.greenlab.dto.CheckEmailResponseBody;
import com.greenlab.greenlab.miscellaneous.PasswordChecker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SignupController {
    // ViewCoursesController viewCoursesController;
    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/signup")
    public String getLogin(ModelMap model, HttpServletRequest request) {
        // if (request.getSession().getAttribute("email") != null)
        //     return "redirect:/courses";
//        else
            return "signUp";

    }

    @PostMapping(value = "/signup")
    public String postLogin(@RequestParam(value = "uid", required = false) String uid,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "firstname", required = false) String firstname,
            @RequestParam(value = "lastname", required = false) String lastname,
            @RequestParam(value = "role", required = false) String role, ModelMap model, HttpServletRequest request) {


        System.out.println("[post/login]" + "uid=" + uid + "email=" + email + " password=" + password + " firstname="
                + firstname + " lastname=" + lastname + " role= " + role);
        password = PasswordChecker.encryptSHA512(password);
        User user = new User(uid, email, password, firstname, lastname, role);
        userRepository.save(user);
        if (request.getSession().getAttribute("email") != null) {
            request.getSession().setAttribute("role", role);
            return "redirect:/courses";
        }
        return "signUp";
    }

    @PostMapping(value = "/checkemail")
    public ResponseEntity<?> postCheckEmail(@Valid @RequestBody CheckEmailRequestBody checkEmail, Errors errors) {
        CheckEmailResponseBody result = new CheckEmailResponseBody();
        if (errors.hasErrors()) {
            result.setMessage(
                    errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(result);
        }
        User user = userRepository.findByEmail(checkEmail.getEmail());
        if (user == null) {
            result.setMessage("email not found");
            result.setExist(false);
        } else {
            result.setMessage("email is exist");
            result.setExist(true);
        }
        return ResponseEntity.ok(result);
    }

}
