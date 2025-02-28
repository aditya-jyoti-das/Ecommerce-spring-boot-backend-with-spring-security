package com.aditya.Ecommerce_java_spring_boot_backend.Controller;


import com.aditya.Ecommerce_java_spring_boot_backend.Model.User;
import com.aditya.Ecommerce_java_spring_boot_backend.Service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @Autowired
    public MyUserDetailsService userService;

    @PostMapping("/register")
    public ResponseEntity<String> postRegister(@RequestBody User user) {
        User existUser = this.userService.getUserByUsername(user.getUsername());
        if (existUser != null) {
            return new ResponseEntity<>("USER WITH THIS USERNAME %s ALREADY EXIST".formatted(user.getUsername()), HttpStatus.FORBIDDEN);
        } else {
            User user1 = this.userService.addUserByEncryption(user);
            if (user1 == null) {
                return new ResponseEntity<>("ERROR OCCURRED DURING USER REGISTRATION TRY IT AGAIN", HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                return new ResponseEntity<>("REGISTERED SUCCESSFULLY",HttpStatus.ACCEPTED);
            }
        }
    }
}
