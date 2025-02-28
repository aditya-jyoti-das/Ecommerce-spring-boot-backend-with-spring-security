package com.aditya.Ecommerce_java_spring_boot_backend.Service;

import com.aditya.Ecommerce_java_spring_boot_backend.Model.MyUserDetails;
import com.aditya.Ecommerce_java_spring_boot_backend.Model.User;
import com.aditya.Ecommerce_java_spring_boot_backend.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    public UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepo.findByUsername(username);
        System.out.println("MyUserDetailService:: "+user);
        if(user==null){
            throw new UsernameNotFoundException("NULL exception occurred USER NAME NOT FOUND");
        }
        return new MyUserDetails(user);
    }

    public User addUserByEncryption(User user) {
        // encryption
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        try{
            user.setPassword(encoder.encode(user.getPassword()));
            return this.userRepo.save(user);
        }catch(Exception e){
            return null;
        }
    }

    public User getUserByUsername(String username){
        return this.userRepo.findByUsername(username);
    }

}
