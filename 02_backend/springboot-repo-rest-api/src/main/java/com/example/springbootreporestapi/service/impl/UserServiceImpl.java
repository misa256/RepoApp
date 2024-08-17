package com.example.springbootreporestapi.service.impl;

import com.example.springbootreporestapi.auth.LoginUserDetails;
import com.example.springbootreporestapi.entity.User;
import com.example.springbootreporestapi.exception.RepoAPIException;
import com.example.springbootreporestapi.payload.LoginDto;
import com.example.springbootreporestapi.payload.UserDto;
import com.example.springbootreporestapi.payload.UserUpdateDto;
import com.example.springbootreporestapi.repository.UserRepository;
import com.example.springbootreporestapi.service.UserService;
import jakarta.transaction.Transactional;
import org.hibernate.JDBCException;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.sql.SQLException;
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
    public UserUpdateDto updateUser(Long userId, UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new RepoAPIException(HttpStatus.NOT_FOUND, "ユーザーが登録されていません。");
        });

        try {
            user.setEmail(userUpdateDto.getEmail());
            user.setName(userUpdateDto.getName());
            user.setRoles(userUpdateDto.getRoles());
            User updatedUser = userRepository.save(user);
            UserUpdateDto newUser = new UserUpdateDto();
            newUser.setEmail(updatedUser.getEmail());
            newUser.setName(updatedUser.getName());
            newUser.setRoles(updatedUser.getRoles());
            return newUser;
        }catch(DataAccessException dataAccessException){
            // SQL エラーが発生した場合の処理
            Throwable cause = dataAccessException.getCause();
            if (cause instanceof JDBCException) {
                JDBCException jdbcException = (JDBCException) cause;
                String sqlState = jdbcException.getSQLState();

                // SQLSTATE コードに基づくエラーメッセージの出力
                switch (sqlState) {
                    case "23505": // unique_violation
                        throw new RepoAPIException(HttpStatus.BAD_REQUEST, "そのメールアドレスはすでに使用されています。");
                    case "23503": // foreign_key_violation
                        throw new RepoAPIException(HttpStatus.BAD_REQUEST, "関連するデータが存在しません。");
                    case "42P01": // undefined_table
                        throw new RepoAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "参照しているテーブルが存在しません。");
                    default:
                        throw new RepoAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "データベースエラーが発生しました: " + jdbcException.getMessage());
                }
            } else {
                // SQLSTATE が取得できない場合のデフォルトエラー処理
                throw new RepoAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "データベースエラーが発生しました: " + dataAccessException.getMessage());
            }
        }
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
