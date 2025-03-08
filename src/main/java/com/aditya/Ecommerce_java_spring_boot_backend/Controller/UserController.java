package com.aditya.Ecommerce_java_spring_boot_backend.Controller;


import com.aditya.Ecommerce_java_spring_boot_backend.Model.User;
import com.aditya.Ecommerce_java_spring_boot_backend.Service.JwtService;
import com.aditya.Ecommerce_java_spring_boot_backend.Service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.env.RandomValuePropertySourceEnvironmentPostProcessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    public MyUserDetailsService userService;

    @Autowired
    public AuthenticationManager authManager;


    @Autowired
    public JwtService jwtService;

    @PostMapping("/api/register")
    public ResponseEntity<Map<String,Object>> postRegister(@RequestBody User user) {
        User existUser = this.userService.getUserByUsername(user.getUsername());
        if (existUser != null) {
            return new ResponseEntity<>(Map.of("message","USER WITH THIS USERNAME %s ALREADY EXIST".formatted(user.getUsername())), HttpStatus.FORBIDDEN);
        } else {
            User user1 = this.userService.addUserByEncryption(user);
            if (user1 == null) {
                return new ResponseEntity<>(Map.of("message","ERROR OCCURRED DURING USER REGISTRATION TRY IT AGAIN"), HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                return new ResponseEntity<>(Map.of("message","REGISTERED SUCCESSFULLY"),HttpStatus.ACCEPTED);
            }
        }
    }

    @PostMapping("/api/login")
    public ResponseEntity<Map<String,Object>> PostLogin(@RequestBody User user){
        String username=user.getUsername();
        String password = user.getPassword();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = authManager.authenticate(usernamePasswordAuthenticationToken);
        if(authenticate.isAuthenticated()){


            return new ResponseEntity<Map<String,Object>>(Map.of("token", jwtService.generateToken(username)),HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(Map.of("message","login Failed"),HttpStatus.FORBIDDEN);
        }

    }
}
