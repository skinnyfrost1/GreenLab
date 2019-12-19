package com.greenlab.greenlab.controller.user;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.greenlab.greenlab.dto.BooleanResponseBody;
import com.greenlab.greenlab.dto.CheckEmailRequestBody;
import com.greenlab.greenlab.dto.CheckEmailResponseBody;
import com.greenlab.greenlab.dto.SingleStringRequestBody;
import com.greenlab.greenlab.miscellaneous.PasswordChecker;
import com.greenlab.greenlab.model.StuLab;
import com.greenlab.greenlab.model.User;
import com.greenlab.greenlab.repository.StuLabRepository;
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
public class SignupController {
    // ViewCoursesController viewCoursesController;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StuLabRepository stuLabRepo;

    @GetMapping(value = "/signup")
    public String getLogin(ModelMap model, HttpServletRequest request) {
        // if (request.getSession().getAttribute("email") != null)
        // return "redirect:/courses";
        // else
        return "signUp";

    }

    @PostMapping(value = "/signup")
    public String postLogin(@RequestParam(value = "uid", required = false) String uid,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "firstname", required = false) String firstname,
            @RequestParam(value = "lastname", required = false) String lastname,
            @RequestParam(value = "role", required = false) String role, ModelMap model, HttpServletRequest request) {
            System.out.println("[POST signup]");
            if (request.getSession().getAttribute("email") != null) {
                request.getSession().setAttribute("role", role);
                return "redirect:/courses";
            }
        email = email.toLowerCase();
        System.out.println("[post/login]" + "uid=" + uid + "email=" + email + " password=" + password + " firstname="
                + firstname + " lastname=" + lastname + " role= " + role);
        User user = userRepository.findByEmail(email);
        if (user != null){
            model.addAttribute("emailError","Email is exist.");
            return "signUp";
        }
        user = userRepository.findByUid(uid);
        if (user != null){
            model.addAttribute("uidError","UID is exist.");
            return "signUp";
        }
        password = PasswordChecker.encryptSHA512(password);
        user = new User(uid, email, password, firstname, lastname, role);
        userRepository.save(user);
        StuLab sl = new StuLab();
        sl.setStudentEmail(email);
        stuLabRepo.save(sl);
        return "redirect:/courses";
    }

    @PostMapping(value = "/checkemail")
    public ResponseEntity<?> postCheckEmail(@Valid @RequestBody CheckEmailRequestBody checkEmail, Errors errors) {
        CheckEmailResponseBody result = new CheckEmailResponseBody();
        if (errors.hasErrors()) {
            result.setMessage(
                    errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(result);
        }
        String email = checkEmail.getEmail().toLowerCase();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            result.setMessage("email not found");
            result.setExist(false);
        } else {
            result.setMessage("email is exist");
            result.setExist(true);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/checkuid")
    public ResponseEntity<?> postCheckUid(@Valid @RequestBody SingleStringRequestBody checkUid, Errors errors) {
        BooleanResponseBody result = new BooleanResponseBody();
        if (errors.hasErrors()) {
            result.setMessage(
                    errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(result);
        }
        User user = userRepository.findByUid(checkUid.getStr());
        if (user == null) {
            // if can't found the user
            result.setMessage("Uid not found");
            result.setBool(false);
            System.out.println("[checkuid] user = null");
            return ResponseEntity.ok(result);
        } else {
            // if found the user
            result.setMessage("Uid is exist");
            result.setBool(true);
            System.out.println("[checkuid] user = " + user.getEmail());
            return ResponseEntity.ok(result);
        }
    }

}
