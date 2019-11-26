package com.greenlab.greenlab.controller.user;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.greenlab.greenlab.dto.BooleanResponseBody;
import com.greenlab.greenlab.dto.ForgotPasswordRequestBody;
import com.greenlab.greenlab.miscellaneous.PasswordChecker;
import com.greenlab.greenlab.model.User;
import com.greenlab.greenlab.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class ForgotPasswordController {
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/forgotpassword")
    public String getForgotPassword(ModelMap model, HttpServletRequest request){
        if (request.getSession().getAttribute("email") != null) {
            System.out.println("[getForgotPassword] login already");
            return "redirect:/courses";
        } else{            
            System.out.println("[getForgotPassword] going to render a forgot password template");
            return "forgotPassword";
        }
    }

    @PostMapping("/forgotpassword")
    public ResponseEntity<?> postForgotPassword(@Valid @RequestBody ForgotPasswordRequestBody forgotPassword, Errors errors, ModelMap model, HttpServletRequest request){
        System.out.println("[POST forgotpassword]");
        BooleanResponseBody result = new BooleanResponseBody();
        String email = forgotPassword.getEmail().toLowerCase();
        User user = userRepository.findByEmail(email);
        if (user==null){
            System.out.println("can't find user");
            result.setMessage("Your infomation doesn't match.");
            result.setBool(false);
            return ResponseEntity.ok(result);
        }
        String uid = forgotPassword.getUid();
        if (!user.getUid().equals(uid)){
            System.out.println("UID NOT MATCH");
            result.setMessage("Your infomation doesn't match.");
            result.setBool(false);
            return ResponseEntity.ok(result);
        }
        String password = forgotPassword.getPassword();
            password = PasswordChecker.encryptSHA512(password);
            user.setPassword(password);
            userRepository.save(user);
            System.out.println("save password success.");
            result.setMessage("Success!");
            result.setBool(true);
            return ResponseEntity.ok(result);
    }
}
