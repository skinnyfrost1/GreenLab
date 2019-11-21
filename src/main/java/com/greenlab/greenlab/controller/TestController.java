package com.greenlab.greenlab.controller;

import com.greenlab.greenlab.miscellaneous.PasswordChecker;
import com.greenlab.greenlab.model.User;
import com.greenlab.greenlab.repository.ContainerRepository;
import com.greenlab.greenlab.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController{

    @Autowired
    UserRepository userRepository;

    @Autowired
    ContainerRepository containerRepository;

    @GetMapping(value="/test/add5stu")
    public String getAdd5Stu() {
        String password = "123";
        password = PasswordChecker.encryptSHA512(password);
        String role = "Student";
        User user;
        for (int i = 0; i<5; i++){
            int temp = 800000000+i;
            String uid = Integer.toString(temp);
            String email = "stu" + Integer.toString(i) +"@greenlab.edu";
            String firstname = "First"+Integer.toString(i);
            String lastname = "Last"+Integer.toString(i);
            user = new User(uid,email,password,firstname,lastname,role);
            User temp_user = userRepository.findByEmail(email);
            if (temp_user!=null)
                return "<div>You already added five student.</div>";
            userRepository.save(user);
        }
        return "success";
    }

    @GetMapping(value="/test/add5prof")
    public String getAdd5prof() {
        String password = "123";
        password = PasswordChecker.encryptSHA512(password);
        String role = "Professor";
        User user;
        for (int i = 0; i<5; i++){
            int temp = 880000000+i;
            String uid = Integer.toString(temp);
            String email = "prof" + Integer.toString(i) +"@greenlab.edu";
            String firstname = "First"+Integer.toString(i);
            String lastname = "Last"+Integer.toString(i);
            user = new User(uid,email,password,firstname,lastname,role);
            User temp_user = userRepository.findByEmail(email);
            if (temp_user!=null)
                return "<div>You already added five prof.</div>";
            userRepository.save(user);
        }
        return "successfully add 5 prof.";
    }
}