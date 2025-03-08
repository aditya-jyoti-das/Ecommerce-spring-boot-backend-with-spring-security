package com.aditya.Ecommerce_java_spring_boot_backend.Configuration;

import com.aditya.Ecommerce_java_spring_boot_backend.Service.JwtService;
import com.aditya.Ecommerce_java_spring_boot_backend.Service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //getting the header
        //getting token and username
        //check SecurityContextHolder.getContext().getAuthentication() is null or not if null then
        // setAuthentication() to UsernamePasswordAuthenticationToken() which contain validated username and Authorization.

        String authHeader = request.getHeader("Authorization");

        String username=null;
        String token = null;

        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            token=authHeader.substring(7);
            username=jwtService.getUsernameFromToken(token);

        }
        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){

            MyUserDetailsService userDetailService = context.getBean(MyUserDetailsService.class);
            UserDetails userDetails = userDetailService.loadUserByUsername(username);

            if(jwtService.validateToken(token,userDetails)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

        }
        filterChain.doFilter(request,response);

    }
}
