package com.example.springbootreporestapi.service;

import com.example.springbootreporestapi.entity.User;
import com.example.springbootreporestapi.payload.LoginDto;
import com.example.springbootreporestapi.payload.UserDto;

import java.util.List;

public interface UserService {
//    特定のユーザー検索
    UserDto getUserByEmail(String email);
//    IDで、特定のユーザー検索
    UserDto getUserById(Long id);
//    ユーザー全件表示
    List<UserDto> getUsers();
//    新規登録
    UserDto createUser(UserDto userDto);
//    ユーザー更新
    UserDto updateUser(Long id, UserDto userDto);
//    ユーザー削除
    String deleteUser(Long id);
}
