package com.electronicstore.services.impl;
import com.electronicstore.dtos.UserDto;
import com.electronicstore.entities.User;
import com.electronicstore.exceptions.ResourceNotFoundException;
import com.electronicstore.helper.PageableResponse;
import com.electronicstore.repositories.UserRepository;
import com.electronicstore.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user","user id",userId));
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
        User userFromDB = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user","user id",userId));
        this.userRepository.delete(userFromDB);
        logger.info("deleted successfully");

    }

    @Override
    public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String field, String direction) {
        logger.info("get all user triggered");
        //coding for sorting
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(field).ascending() : Sort.by(field).descending();
        //coding for pagination
        PageRequest pageDetails = PageRequest.of(pageNumber, pageSize,sort);
        //find all the users from DB using pagination inputs
        Page<User> allUsersWithPagination = this.userRepository.findAll(pageDetails);
        //get the contents
        List<User> allUsersWithPaginationContents = allUsersWithPagination.getContent();
        //convert user to userDto
        List<UserDto> userDto = allUsersWithPaginationContents.stream().map((user -> this.modelMapper.map(user, UserDto.class))).collect(Collectors.toList());
        //throw if userDto is null means there is no data in that page
        if(allUsersWithPagination.getContent().size()==0){
            throw new ResourceNotFoundException("page","page number",String.valueOf(allUsersWithPagination.getNumber()));
        }
        //create UserResponseWithPagination
        PageableResponse pageableResponse = new PageableResponse();
        pageableResponse.setContent(userDto);
        pageableResponse.setLastPage(allUsersWithPagination.isLast());
        pageableResponse.setPageSize(allUsersWithPagination.getSize());
        pageableResponse.setTotalPages(allUsersWithPagination.getTotalPages());
        pageableResponse.setPageNumber(allUsersWithPagination.getNumber());
        pageableResponse.setTotalElements(allUsersWithPagination.getTotalElements());


        return pageableResponse;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        logger.info("get user by user id triggered ");
        User userFromDB = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user","user id",userId));
        logger.info("user by user id {}",userFromDB);
        return this.modelMapper.map(userFromDB,UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        logger.info("get user by email triggered");
        User userFromEmail = this.userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("user","email",email));
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
        User userFromDBByEmailAndPassword = this.userRepository.findByEmailAndPassword(email, password).orElseThrow(() -> new ResourceNotFoundException("user","email",email));
        UserDto userDto = this.modelMapper.map(userFromDBByEmailAndPassword, UserDto.class);
        logger.info("user from username and password {}",userDto);
        return userDto;
    }


}
