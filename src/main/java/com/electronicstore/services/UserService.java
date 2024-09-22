package com.electronicstore.services;

import com.electronicstore.dtos.UserDto;
import com.electronicstore.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    //create
    UserDto createUser(UserDto userDto);

    //update
    UserDto updateUser(UserDto userDto, String userId);

    //delete
    void deleteUser(String userId);

    //get all user
    List<UserDto> getAllUser();

    //get a single user by user id
    UserDto getUserByUserId(String userId);

    //get a single user by email
    UserDto getUserByEmail(String email);

    //search user
    List<UserDto> searchUser(String keyword);

    //get user by email and password
    UserDto getUserByEmailAndPassword(String email, String password);



}
