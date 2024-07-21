package com.example.springbootreporestapi.payload;

import lombok.Data;

@Data
public class LoginDto {
    private String loginEmail;
    private String loginPassword;
}
