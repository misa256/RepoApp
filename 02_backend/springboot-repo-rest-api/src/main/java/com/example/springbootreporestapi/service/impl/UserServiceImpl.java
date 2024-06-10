package com.example.springbootreporestapi.service.impl;

import com.example.springbootreporestapi.auth.LoginUserDetails;
import com.example.springbootreporestapi.entity.User;
import com.example.springbootreporestapi.exception.RepoAPIException;
import com.example.springbootreporestapi.payload.UserDto;
import com.example.springbootreporestapi.repository.UserRepository;
import com.example.springbootreporestapi.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            throw new RepoAPIException(HttpStatus.NOT_FOUND, "ユーザーが登録されていません。");
        });
        return mapToDto(user);
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> {
                    throw new RepoAPIException(HttpStatus.NOT_FOUND, "ユーザーが登録されていません。");
                }
        );
        return mapToDto(user);
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> mapToDto(user)).collect(Collectors.toList());
    }

    @Override
    public UserDto createUser(UserDto userDto) {
//        すでにそのUserが登録されていないかの確認
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RepoAPIException(HttpStatus.BAD_REQUEST, "既にそのユーザーは登録されています。");
        }
//        User新規登録
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setRoles(userDto.getRoles());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
//        DBに登録
        userRepository.save(user);
        return mapToDto(user);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            throw new RepoAPIException(HttpStatus.NOT_FOUND, "ユーザーが登録されていません。");
        });
        if (!user.getEmail().equals(getLoginUserDetails().getUsername())) {
            throw new RepoAPIException(HttpStatus.UNAUTHORIZED, "ユーザー情報を更新する権限がありません。");
        }
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(userDto.getRoles());
        User updatedUser = userRepository.save(user);
        return mapToDto(updatedUser);
    }

    @Override
    public String deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            throw new RepoAPIException(HttpStatus.NOT_FOUND, "ユーザーが登録されていません。");
        });
        if (!user.getEmail().equals(getLoginUserDetails().getUsername())) {
            throw new RepoAPIException(HttpStatus.UNAUTHORIZED, "ユーザー情報を削除する権限がありません。");
        }
        userRepository.delete(user);
        return "ユーザーを削除しました。";
    }

    private UserDto mapToDto(User user) {
        return mapper.map(user, UserDto.class);
    }

    private User mapToEntity(UserDto userDto) {
        return mapper.map(userDto, User.class);
    }

    private LoginUserDetails getLoginUserDetails() {
//        現在認証されているプリンシパルを取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        認証されるプリンシパルのIDを返す
        Object principal = authentication.getPrincipal();
        if (principal instanceof LoginUserDetails) {
            return (LoginUserDetails) principal;
        } else {
            return null;
        }
    }
}
