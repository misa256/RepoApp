package com.example.springbootreporestapi.service.impl;

import com.example.springbootreporestapi.auth.JwtTokenProvider;
import com.example.springbootreporestapi.entity.User;
import com.example.springbootreporestapi.exception.RepoAPIException;
import com.example.springbootreporestapi.payload.JwtAuthResponse;
import com.example.springbootreporestapi.payload.LoginDto;
import com.example.springbootreporestapi.repository.UserRepository;
import com.example.springbootreporestapi.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Override
    public JwtAuthResponse login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getLoginEmail(),
                loginDto.getLoginPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        User loggedInUser = userRepository.findByEmail(loginDto.getLoginEmail()).orElseThrow(() -> {
            throw new RepoAPIException(HttpStatus.NOT_FOUND, "ユーザーが登録されていません。ユーザー登録を行ってください。");
        });

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(token);
        jwtAuthResponse.setUserId(loggedInUser.getId());
        jwtAuthResponse.setEmail(loginDto.getLoginEmail());
        jwtAuthResponse.setName(loggedInUser.getName());
        jwtAuthResponse.setRoles(loggedInUser.getRoles());

        return jwtAuthResponse;
    }
}
