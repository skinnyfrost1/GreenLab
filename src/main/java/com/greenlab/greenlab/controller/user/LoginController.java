package com.greenlab.greenlab.controller.user;

import java.util.LinkedList;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.greenlab.greenlab.dto.BooleanResponseBody;
import com.greenlab.greenlab.dto.LoginRequestBody;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.ImageData.ImageDataRepository;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.equipmentData.EquipmentDataRepository;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.userEquipment.UserEquipment;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.userEquipment.UserEquipmentFolder;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.userEquipment.UserEquipmentFolderRepository;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.userEquipment.UserEquipmentRepository;
import com.greenlab.greenlab.labEquip.framework.imageBlob.ImageBlobRepository;
import com.greenlab.greenlab.miscellaneous.PasswordChecker;
import com.greenlab.greenlab.model.User;
import com.greenlab.greenlab.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class LoginController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = {"/index","/login"})
    public String getLogin(ModelMap model, HttpServletRequest request) {

        if (request.getSession().getAttribute("email") != null) {
            return "redirect:/courses";
        } else{
            return "index";
        }


    }

    @PostMapping(value = "/index")
    public ResponseEntity<?> postLogin(@Valid @RequestBody LoginRequestBody login, Errors errors, ModelMap model, HttpServletRequest request){
        BooleanResponseBody result = new BooleanResponseBody();
        if (errors.hasErrors()) {
            result.setMessage(
                    errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(result);
        }        
        String email = login.getEmail();
        String password =login.getPassword();
        System.out.println("[post/login]email=" + email + " password=" + password);

        if (userRepository.findByEmail(email) != null) {
            User user = userRepository.findByEmail(email);
            String encryptedPassword = PasswordChecker.encryptSHA512(password);
            if ((user.getPassword()).equals(encryptedPassword)) {
                System.out.println("password correct");     //debug
                System.out.println(encryptedPassword);      //debug
                request.getSession().setAttribute("role",user.getRole());
                request.getSession().setAttribute("email",email);
                result.setMessage("Success!");
                result.setBool(true);


                //System.out.println(" login success 666 ");

                // so here we need inital user here
                    // both equipment and lab

                String theUniqueId = user.getEmail();
                createOneUser( theUniqueId );



                return ResponseEntity.ok(result);
            }
        }
        result.setMessage("Email and password not match.");
        result.setBool(false);
        System.out.println("Email and password not match.");
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/logout")
    public String postLogout(ModelMap model, HttpServletRequest request) {
        request.getSession().removeAttribute("email");
        request.getSession().removeAttribute("role");
        return "index";
    }

    @Autowired
    private UserEquipmentRepository userEquipmentRepository;

    @Autowired
    private UserEquipmentFolderRepository userEquipmentFolderRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public EquipmentDataRepository equipmentDataRepository;

    @Autowired
    public ImageDataRepository imageDataRepository;

    @Autowired
    private ImageBlobRepository imageBlobRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void createOneUser( String emailAddress ){

        String defaultUser = emailAddress;

        UserEquipment userEquipment =  userEquipmentRepository.findByUserId(  defaultUser  );

        if( userEquipment == null ){

            LinkedList<String> folderIds = new LinkedList<>();
            UserEquipmentFolder userEquipmentFolder;

            userEquipmentFolder = new UserEquipmentFolder();
            userEquipmentFolder.setType("all");
            userEquipmentFolder.setOwner(emailAddress);
            folderIds.add(userEquipmentFolderRepository.save(userEquipmentFolder).getUserFolderId());
            userEquipmentFolder = new UserEquipmentFolder();
            userEquipmentFolder.setType("share");
            userEquipmentFolder.setOwner(emailAddress);
            folderIds.add(userEquipmentFolderRepository.save(userEquipmentFolder).getUserFolderId());
            userEquipmentFolder = new UserEquipmentFolder();
            userEquipmentFolder.setType("favourite");
            userEquipmentFolder.setOwner(emailAddress);
            folderIds.add(userEquipmentFolderRepository.save(userEquipmentFolder).getUserFolderId());


            userEquipmentFolder = new UserEquipmentFolder();
            userEquipmentFolder.setType("recent");
            userEquipmentFolder.setOwner(emailAddress);
            folderIds.add(userEquipmentFolderRepository.save(userEquipmentFolder).getUserFolderId());
            userEquipment = new UserEquipment();
            userEquipment.setUserId(emailAddress);
            userEquipment.setFolderIds(folderIds);
            userEquipmentRepository.save(userEquipment);

        }else{

        }

    }

}
