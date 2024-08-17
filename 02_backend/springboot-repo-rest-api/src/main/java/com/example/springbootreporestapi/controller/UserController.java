package com.example.springbootreporestapi.controller;

import com.example.springbootreporestapi.entity.User;
import com.example.springbootreporestapi.payload.UserDto;
import com.example.springbootreporestapi.payload.UserUpdateDto;
import com.example.springbootreporestapi.service.UserService;
import org.springframework.aop.target.LazyInitTargetSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/repoApi/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/email")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam("email") String email){
        return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
    }

//    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable (name = "userId") Long userId){
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(){
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto){
        return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserUpdateDto> updateUser(
            @PathVariable(name = "userId") Long id,
            @RequestBody UserUpdateDto userUpdateDto
    ){
        return new ResponseEntity<>(userService.updateUser(id, userUpdateDto), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "userId") Long id){
        return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.OK);
    }
}
