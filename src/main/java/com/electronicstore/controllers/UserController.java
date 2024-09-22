package com.electronicstore.controllers;

import com.electronicstore.dtos.UserDto;
import com.electronicstore.helper.ApiResponseMessage;
import com.electronicstore.services.UserService;
import com.electronicstore.services.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/electronicstore/user/v1")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/addUser")
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto userDto){
        logger.info("user controller --> save user");
        UserDto user = this.userService.createUser(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserDto>> getAllUser(){
        logger.info("user controller -> getAllUser() triggered ");
        List<UserDto> allUser = this.userService.getAllUser();
        return new ResponseEntity<>(allUser,HttpStatus.OK);
    }

    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto, @PathVariable String userId){
        logger.info(("user controller -> updateUser() triggered"));
        UserDto userDto1 = this.userService.updateUser(userDto, userId);
        return new ResponseEntity<>(userDto1,HttpStatus.OK);
    }

    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId){
        ApiResponseMessage message = ApiResponseMessage.builder().message("User is removed / deleted successfully").success(true).status(HttpStatus.OK).build();
        logger.info("user controller -> deleteUser() ");
        this.userService.deleteUser(userId);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    @GetMapping("/getUserByUserId/{userId}")
    public ResponseEntity<UserDto> getUserByUserId(@PathVariable String userId){
        logger.info("user controller -> getUserByUserId() ");
        UserDto userByUserIdDto = this.userService.getUserByUserId(userId);
        return new ResponseEntity<>(userByUserIdDto,HttpStatus.OK);
    }

    @GetMapping("/getUserByEmail/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email){
        logger.info("user controller -> getUserByEmail() ");
        UserDto userByUserIdDto = this.userService.getUserByEmail(email);
        return new ResponseEntity<>(userByUserIdDto,HttpStatus.OK);
    }

    @GetMapping("/searchUser/{keyword}")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keyword){
        logger.info("user controller -> searchUser() ");
        List<UserDto> searchedUsers = this.userService.searchUser(keyword);
        return new ResponseEntity<>(searchedUsers,HttpStatus.OK);


    }

}
