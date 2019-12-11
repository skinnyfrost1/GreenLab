package com.greenlab.greenlab.controller;

import java.util.ArrayList;
import java.util.List;

import com.greenlab.greenlab.miscellaneous.PasswordChecker;
import com.greenlab.greenlab.model.Course;
import com.greenlab.greenlab.model.Lab;
import com.greenlab.greenlab.model.User;
import com.greenlab.greenlab.repository.CourseRepository;
import com.greenlab.greenlab.repository.LabRepository;
import com.greenlab.greenlab.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class TestController{

    @Autowired
    UserRepository userRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    LabRepository labRepository;


    @GetMapping(value="/test/add5stu")
    public String getAdd5Stu() {
        String password = "123";
        password = PasswordChecker.encryptSHA512(password);
        String role = "student";
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
        String role = "professor";
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

    @GetMapping(value="/test/add30courses")
    public String getAdd30Courses() {
        for (int i=0;i<30;i++){
            String courseId = "CHE1"+Integer.toString(i);
            String courseName = "Course name "+Integer.toString(i);
            String semester = "Spring "+Integer.toString(i);
            String courseDescription = "no bb";
            String creator = "prof1@greenlab.edu";
            String createDate = "11/21/2019";
            Course course = new Course(courseId, courseName, semester, courseDescription,createDate,creator,null);
            courseRepository.save(course);
        }
        return "add 30 courses";
    }

    @GetMapping(value="/test/dbref")
    public String getDbref(){
        String courseId = "CHE 999";
        String courseName = "no bb";
        String semester = "spring 2019";
        String courseDescription = "descrition";
        String creator = "prof2@greenlab.edu";
        String createDate = "11/21/2019";
        List<User> students = new ArrayList<User>();
        Course c = new Course(courseId, courseName, semester, courseDescription, creator,createDate,students);
        User s = userRepository.findByEmail("stu1@greenlab.edu");
        c.getStudents().add(s);
        c.set_id("niceday");
        courseRepository.save(c);

        Course cc = courseRepository.findBy_id("niceday");
        User ss = cc.getStudents().get(0);
    
        return ss.getEmail();
    }

    @GetMapping(value="/test/add10labs")
    public String getAdd10Labs(){
        String courseId = "TES111";
        String labDescription = "this is lab description";
        String creator = "prof1@greenlab.edu";
        
        for (int i = 0; i<10;i++){
            String labName = "Lab "+Integer.toString(i);
            String buf = courseId + labName + creator;
            buf = buf.toLowerCase();
            buf = buf.replaceAll(" ","");
            String _id = buf;
            Lab l = labRepository.findBy_id(_id);
            if (l==null)
                // l = new Lab(_id, courseId, labName, labDescription, creator, null, null, null);
                l = new Lab(_id, courseId, labName, labDescription, creator,null);

            
            labRepository.save(l);
        }
        return "added 10 labs.";
    }

    @GetMapping(value="/test/add5labs")
    public String getAdd5Labs(){
        String courseId = "CHE133";
        String labDescription = "this is lab description";
        String creator = "anhui.jiang@stonybrook.edu";

        for (int i = 0; i<10;i++){
            String labName = "Lab "+Integer.toString(i);
            String buf = courseId + labName + creator;
            buf = buf.toLowerCase();
            buf = buf.replaceAll(" ","");
            String _id = buf;
            Lab l = labRepository.findBy_id(_id);
            if (l==null)
                l = new Lab(_id, courseId, labName, labDescription, creator,null);

            labRepository.save(l);
        }
        return "added 5 labs.";
    }

    @GetMapping(value="/test/add10student")
    public String getAdd10Student() {
        String password = "123";
        password = PasswordChecker.encryptSHA512(password);
        String role = "student";
        User user;
        for (int i = 0; i<10; i++){
            int temp = 111111111+i;
            String uid = Integer.toString(temp);
            String email = "student" + Integer.toString(i) +"@greenlab.edu";
            String firstname = "First"+Integer.toString(i);
            String lastname = "Last"+Integer.toString(i);
            user = new User(uid,email,password,firstname,lastname,role);
            User temp_user = userRepository.findByEmail(email);
            if (temp_user!=null)
                return "<div>You already added 10 students.</div>";
            userRepository.save(user);
        }
        return "successfully add 10 students.";
    }
}