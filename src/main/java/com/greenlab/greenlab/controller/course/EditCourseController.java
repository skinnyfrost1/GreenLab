package com.greenlab.greenlab.controller.course;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.greenlab.greenlab.dto.BooleanResponseBody;
import com.greenlab.greenlab.dto.DeleteStudentResponseBody;
import com.greenlab.greenlab.dto.LabMenuRequestBody;
import com.greenlab.greenlab.dto.LabNameAndId;
import com.greenlab.greenlab.dto.MultiStringRequestBody;
import com.greenlab.greenlab.dto.SingleStringRequestBody;
import com.greenlab.greenlab.model.Course;
import com.greenlab.greenlab.model.Lab;
import com.greenlab.greenlab.model.StuCourse;
import com.greenlab.greenlab.model.User;
import com.greenlab.greenlab.repository.CourseRepository;
import com.greenlab.greenlab.repository.LabRepository;
import com.greenlab.greenlab.repository.StuCourseRepository;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class EditCourseController {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private LabRepository labRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StuCourseRepository stuCourseRepository;

    @GetMapping(value = "/course/edit")
    public String getCreateCourse(ModelMap model, @RequestParam(value = "id") String id, HttpServletRequest request) {
        if (request.getSession().getAttribute("email") == null)
            return "redirect:/login";
        String role = (String) request.getSession().getAttribute("role");
        System.out.println(role);
        Course course = courseRepository.findBy_id(id);
        model.addAttribute("course", course);
        model.addAttribute("role", role);
        List<Lab> templabs = course.getLabs();
        List<Lab> temprestLabs = labRepository.findByCourseId(course.getCourseId());
        System.out.println("restLab.size() = " + temprestLabs.size());

        if (templabs != null) {
            for (Lab lab : templabs) {
                for (Lab rLab : temprestLabs) {
                    if (lab.get_id().equals(rLab.get_id())) {
                        temprestLabs.remove(rLab);
                        break;
                    }
                }
            }
        }
        List<LabNameAndId> labs = new ArrayList<>();
        List<LabNameAndId> restLabs = new ArrayList<>();

        LabNameAndId buffer;
        if (templabs != null) {
            for (Lab lab : templabs) {
                buffer = new LabNameAndId(lab.get_id(), lab.getLabName());
                labs.add(buffer);
            }
        }
        if (temprestLabs != null) {
            for (Lab lab : temprestLabs) {
                buffer = new LabNameAndId(lab.get_id(), lab.getLabName());
                restLabs.add(buffer);
            }
        }


        model.addAttribute("labs", labs);
        model.addAttribute("restLabs", restLabs);

        // for(Lab temp : labs){
        // System.out.println(temp.testString());
        // }
        List<User> students = course.getStudents();
        model.addAttribute("students", students);
        return "profEditCourse";
    }

    @PostMapping(value = "/course/edit")
    public String postEditCourse(@RequestParam(value = "_id", required = false) String _id,
            @RequestParam(value = "courseId", required = false) String courseId,
            @RequestParam(value = "courseName", required = false) String courseName,
            @RequestParam(value = "semester", required = false) String semester,
            @RequestParam(value = "courseDescription", required = false) String courseDescription,
            @RequestParam(value = "editLabSelected", required = false) List<String> editLabSelected, ModelMap model,
            HttpServletRequest request) {

        // String creator = (String) request.getSession().getAttribute("email");
        List<Lab> labs = new ArrayList<>();
        for (String id : editLabSelected) {
            Lab temp = labRepository.findBy_id(id);


            if (temp != null)
                labs.add(temp);
            else
                System.out.println("temp = null");

            System.out.println("EditLabSelected = " + id);
        }
        //

        Course course = courseRepository.findBy_id(_id);
        System.out.println(course.get_id());
        courseId = courseId.replaceAll(" ", "");
        courseId = courseId.toUpperCase();
        course.setCourseId(courseId);
        course.setCourseName(courseName);
        course.setSemester(semester);
        course.setCourseDescription(courseDescription);
        course.setLabs(labs);
        courseRepository.save(course);
        return "redirect:/courses";
    }

    // this method is use to delete labs from a course.
    // input: courseObjectId, labObjectIds
    @PostMapping(value = "/course/edit/dellabs")
    public ResponseEntity<?> postCourseEditDelLabs(@Valid @RequestBody MultiStringRequestBody reqBody, Errors errors,
            HttpServletRequest request) {
        BooleanResponseBody result = new BooleanResponseBody();
        String message = "Success!\nRemove:\n";
        if (errors.hasErrors()) {
            result.setMessage(
                    errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(result);
        }
        String courseObjectId = reqBody.getCourseObjectId();
        List<String> labObjIds = reqBody.getStrs();

        Course course = courseRepository.findBy_id(courseObjectId);
        List<Lab> labs = course.getLabs();

        for (String s : labObjIds) {
            for (Lab l : labs) {
                if (l.get_id().equals(s)) {
                    message = message + l.getLabName() + "\n";
                    labs.remove(l);
                }
            }
        }
        result.setMessage(message);
        result.setBool(true);
        return ResponseEntity.ok(result);
    }

    @ResponseBody
    @PostMapping(value = "/course/edit/deleteStu")
    public ResponseEntity<?> postCourseEditDelStu(@RequestParam(value = "courseId") String courseId,
            @RequestParam(value = "studentEmail") String studentEmail, ModelMap model, HttpServletRequest request) {
        DeleteStudentResponseBody result = new DeleteStudentResponseBody();
        // if (errors.hasErrors()) {
        // result.setMessage(
        // errors.getAllErrors().stream().map(x ->
        // x.getDefaultMessage()).collect(Collectors.joining(",")));
        // return ResponseEntity.badRequest().body(result);
        // }
        System.out.println("In delete student.");
        String email = (String) request.getSession().getAttribute("email");
        // String courseId = reqBody.getCourseId();
        // String studentEmail = reqBody.getStudentEmail();
        Course course = courseRepository.findByCourseIdAndCreator(courseId, email);// null
        User student = userRepository.findByEmail(studentEmail);
        List<User> students = course.getStudents();
        System.out.println(students.size());
        Iterator<User> it = students.iterator();
        while (it.hasNext()) {
            User s = it.next();
            if (s.getUid().equals(student.getUid())) {
                it.remove();
                break;
            }
        }
        System.out.println(students.size());
        course.setStudents(students);
        courseRepository.save(course);
        System.out.println(courseId + " " + studentEmail);
        StuCourse stuCourse = stuCourseRepository.findByCourseObjectIdAndStudentEmail(course.get_id(), studentEmail);
        System.out.println(stuCourse.toString());
        List<Lab> labs = labRepository.findByCourseId(courseId);
        stuCourseRepository.delete(stuCourse);
        // request.getSession().setAttribute("labs", labs);
        model.addAttribute("students", students);
        result.setStudents(students);
        return ResponseEntity.ok(result);
    }

    @ResponseBody
    @PostMapping(value = "/course/edit/uploadRoster2")
    public String postCourseEditUploadRoster(@RequestParam(value = "courseId", required = false) String courseId,
            @RequestParam(value = "file", required = false) MultipartFile file, ModelMap model,
            HttpServletRequest request) {
        String creator = (String) request.getSession().getAttribute("email");
        Course course = courseRepository.findByCourseIdAndCreator(courseId, creator);
        List<User> students = course.getStudents();
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                ByteArrayInputStream inputFilestream = new ByteArrayInputStream(bytes);
                BufferedReader br = new BufferedReader(new InputStreamReader(inputFilestream));
                String line = "";
                int count = 0;
                while ((line = br.readLine()) != null) {
                    if (count == 0) {
                        // First Line
                    } else {
                        String[] columns = line.split(",");
                        System.out.println(columns[2]);
                        String stuID = columns[2];
                        if (userRepository.findByUid(stuID) != null) {
                            User student = userRepository.findByUid(stuID);
                            if (stuCourseRepository.findByCourseObjectIdAndStudentEmail(course.get_id(),
                                    student.getEmail()) != null) {
                                continue;
                            }
                            StuCourse stuCourse = new StuCourse(course.get_id(), student.getEmail(),
                                    course.getCourseId());
                            stuCourse.set_id(course.get_id() + student.getEmail());
                            System.out.println(stuCourse.toString());
                            stuCourseRepository.save(stuCourse);
                            students.add(student);
                            course.setStudents(students);
                            courseRepository.save(course);
                        }
                    }
                    count++;

                }
                br.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        StringBuilder sb = new StringBuilder();
        // sb.append("<div class=\"notVeryBigContainer\" th:id=\"'div_' +
        // ${student.getEmail()}\" >");
        for (User s : students) {
            sb.append("<div class=\"notVeryBigContainer\" id=\"div_" + s.getEmail() + "\">");
            sb.append("<table class=\"courseContainer\">");
            sb.append("<col width=\"15%\"/><col width=\"25%\"/><col width=\"40%\"/><col width=\"20%\"/>");
            sb.append("<tr>");
            sb.append("<td class=\"courseCellPadding\">");
            sb.append("<div class=\"webfont\">").append(s.getFirstname()).append("</div>");
            sb.append("<input id=\"studentEmail\" type=\"text\" name=\"studentEmail\" value=\"${").append(s.getEmail())
                    .append("\"hidden>");
            sb.append("</td>");
            sb.append("<td class=\"courseCellPadding\">").append(s.getLastname()).append("</td>");
            sb.append("<td class=\"courseCellPadding\">").append(s.getUid()).append("<td/>");
            sb.append("<td class=\"courseCellPadding\">");
            sb.append("<a id=\"").append(s.getEmail()).append(
                    "\" class=\"headerUnselected\" onclick=\"deleteStudent(this)\"><i class='fas fa-trash-alt headerIcon'></i></a>");
            sb.append("</td>");
            sb.append("</tr>");
            sb.append("</table>");
            sb.append("</div>");

        }

        return sb.toString();
    }

    // post 一个courseObjectId _id， 然后返回一个list of lab name 和lab的object id
    @PostMapping(value = "/course/edit/requestlabmenu")
    public ResponseEntity<?> postCourseEditRequestLabMenu(@Valid @RequestBody SingleStringRequestBody reqBody,
            Errors errors, HttpServletRequest request) {
        LabMenuRequestBody result = new LabMenuRequestBody();
        if (errors.hasErrors()) {
            result.setMessage(
                    errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(result);
        }
        String courseObjectId = reqBody.getStr();
        Course course = courseRepository.findBy_id(courseObjectId);
        List<Lab> labs = course.getLabs();

        String courseId = course.getCourseId();
        String creator = (String) request.getSession().getAttribute("email");
        List<Lab> labmenu = labRepository.findByCourseIdAndCreator(courseId, creator);

        for (Lab l : labs) {
            if (labmenu.contains(l)) {
                labmenu.remove(l);
            }
        }
        List<String> labNameList = new ArrayList<>();
        for (Lab l : labmenu) {
            labNameList.add("[\"" + l.getLabName() + "\", \"" + l.get_id() + "\"]");
        }
        return ResponseEntity.ok(result);
    }

}