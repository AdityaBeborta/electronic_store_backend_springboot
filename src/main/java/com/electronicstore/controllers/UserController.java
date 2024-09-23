package com.electronicstore.controllers;
import com.electronicstore.dtos.UserDto;
import com.electronicstore.helper.ApiResponseMessage;
import com.electronicstore.helper.ApplicationConstants;
import com.electronicstore.helper.PageableResponse;
import com.electronicstore.services.UserService;
import jakarta.validation.Valid;
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
    public ResponseEntity<UserDto> saveUser(@Valid @RequestBody UserDto userDto){
        logger.info("user controller --> save user");
        UserDto user = this.userService.createUser(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<PageableResponse<UserDto>> getAllUser(
            @RequestParam(defaultValue = ApplicationConstants.PAGE_NUMBER, value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(defaultValue = ApplicationConstants.PAGE_SIZE, value = "pageSize", required = false) Integer pageSize,
            @RequestParam(defaultValue = ApplicationConstants.SORT_BY_FIELD, value = "field", required = false) String field,
            @RequestParam(defaultValue = ApplicationConstants.SORT_BY_DIRECTION, value = "direction", required = false) String direction
    ){
        logger.info("user controller pageNumber {}",pageNumber);
        logger.info("user controller pageSize {}",pageSize);
        logger.info("user controller field {}",field);
        logger.info("user controller direction {}",direction);
        PageableResponse<UserDto> allUser = this.userService.getAllUser(pageNumber, pageSize, field, direction);
        return new ResponseEntity<>(allUser,HttpStatus.OK);
    }

    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable String userId){
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
