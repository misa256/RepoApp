package com.example.springbootreporestapi.payload;

import lombok.Data;

@Data
public class UserUpdateDto {
    private String email;
    private String name;
    private String[] roles;
}
