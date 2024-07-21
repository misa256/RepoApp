package com.example.springbootreporestapi.service;

import com.example.springbootreporestapi.payload.JwtAuthResponse;
import com.example.springbootreporestapi.payload.LoginDto;

public interface AuthService {
//    ログイン
    JwtAuthResponse login(LoginDto loginDto);
}
