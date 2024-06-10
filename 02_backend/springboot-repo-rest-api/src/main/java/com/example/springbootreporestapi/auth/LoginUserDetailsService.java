package com.example.springbootreporestapi.auth;

import com.example.springbootreporestapi.entity.User;
import com.example.springbootreporestapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
//                ユーザーがDBに存在しない場合、エラーを投げる
                .orElseThrow(()->new UsernameNotFoundException("ユーザーが登録されていません。"));
        return new LoginUserDetails(user);
    }
}
