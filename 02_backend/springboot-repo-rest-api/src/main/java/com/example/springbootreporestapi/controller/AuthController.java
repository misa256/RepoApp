package com.example.springbootreporestapi.controller;

import com.example.springbootreporestapi.payload.JwtAuthResponse;
import com.example.springbootreporestapi.payload.LoginDto;
import com.example.springbootreporestapi.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/repoApi/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto){
        JwtAuthResponse jwtAuthResponse = authService.login(loginDto);
        return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    }
}
