package com.expensetracker.userservice.controller;

import com.expensetracker.userservice.entities.UserInfo;
import com.expensetracker.userservice.entities.UserInfoDto;
import com.expensetracker.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {


    @Autowired
    UserService userService;

    @PostMapping("/user/v1/createUpdate")
    public ResponseEntity<UserInfoDto> createUpdate(UserInfoDto userInfoDto){
        try{
            UserInfoDto user = userService.createOrUpdateUser(userInfoDto);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/v1/getUser")
    public ResponseEntity<UserInfoDto> getUser(@RequestBody UserInfoDto userInfoDto){
        try{
            UserInfoDto user = userService.getUser(userInfoDto);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Boolean> checkHealth(){
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

}
