package com.electronicstore.services.impl;
import com.electronicstore.dtos.UserDto;
import com.electronicstore.entities.User;
import com.electronicstore.repositories.UserRepository;
import com.electronicstore.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserDto createUser(UserDto userDto) {
        //set the unique ID
        logger.info("create user method triggered");
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);
        User user = this.modelMapper.map(userDto, User.class);
        User userFromDB = this.userRepository.save(user);
        logger.info("Save the user {}",userFromDB);
        return this.modelMapper.map(userFromDB,UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        logger.info("update user method triggered");
        User user = this.userRepository.findById(userId).orElseThrow(() -> new RuntimeException("user not found with the id"));
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());
        user.setImageName(userDto.getImageName());
        User updatedUser = this.userRepository.save(user);
        logger.info("updated user {}",updatedUser);
        return this.modelMapper.map(updatedUser,UserDto.class);

    }

    @Override
    public void deleteUser(String userId) {
        logger.info("delete user method triggered");
        User userFromDB = this.userRepository.findById(userId).orElseThrow(() -> new RuntimeException("user not found"));
        this.userRepository.delete(userFromDB);
        logger.info("deleted successfully");

    }

    @Override
    public List<UserDto> getAllUser() {
        logger.info("get all user triggered");
        List<User> allUser = this.userRepository.findAll();
        logger.info("all users {} ",allUser);
        //The {} placeholders make logging statements much cleaner and easier to read. Instead of manually concatenating strings, you can just pass the object, and SLF4J handles the interpolation.
        return allUser.stream().map((user -> this.modelMapper.map(user, UserDto.class))).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        logger.info("get user by user id triggered ");
        User userFromDB = this.userRepository.findById(userId).orElseThrow(() -> new RuntimeException("user not found"));
        logger.info("user by user id {}",userFromDB);
        return this.modelMapper.map(userFromDB,UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        logger.info("get user by email triggered");
        User userFromEmail = this.userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("user with the email not found "));
        logger.info("user fetched from email {}",userFromEmail);
        return this.modelMapper.map(userFromEmail,UserDto.class);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        logger.info("inside search user method");
        List<UserDto> searchUser = this.userRepository.findByNameContains(keyword).stream().map(user -> this.modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
        logger.info("searched user {}",searchUser);
        return searchUser;
    }

    @Override
    public UserDto getUserByEmailAndPassword(String email, String password) {
        logger.info("inside getUserByEmailAndPassword method");
        User userFromDBByEmailAndPassword = this.userRepository.findByEmailAndPassword(email, password).orElseThrow(() -> new RuntimeException("user not found"));
        UserDto userDto = this.modelMapper.map(userFromDBByEmailAndPassword, UserDto.class);
        logger.info("user from username and password {}",userDto);
        return userDto;
    }


}
