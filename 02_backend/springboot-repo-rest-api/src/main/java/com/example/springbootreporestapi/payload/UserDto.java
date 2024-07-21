package com.example.springbootreporestapi.payload;

import com.example.springbootreporestapi.entity.Report;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserDto {
    private long id;
    private String email;
    private String name;
    private String password;
    private String[] roles;
}
