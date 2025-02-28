package com.aditya.Ecommerce_java_spring_boot_backend.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Table(name="UserOfEcomWS")
public class User{


    @Id
    public String username;
    private String password;

    @Autowired
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public User(){

    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password){
        this.password=password;
    }

    @Override
    public String toString() {
        return "User{" +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
