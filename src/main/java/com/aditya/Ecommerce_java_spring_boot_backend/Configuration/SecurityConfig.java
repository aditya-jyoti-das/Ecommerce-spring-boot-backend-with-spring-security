package com.aditya.Ecommerce_java_spring_boot_backend.Configuration;


import com.aditya.Ecommerce_java_spring_boot_backend.Service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private MyUserDetailsService myUserDetailService;

    @Bean
    public AuthenticationProvider MyAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();
        daoAuthProvider.setUserDetailsService(myUserDetailService);
        daoAuthProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        return daoAuthProvider;
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSec) throws Exception {

        return    httpSec.csrf(csrfCustomizer->csrfCustomizer.disable())
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(auth->auth.anyRequest().authenticated())
                .build();
    }





}
