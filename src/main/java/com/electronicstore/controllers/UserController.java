package com.electronicstore.controllers;
import com.electronicstore.dtos.UserDto;
import com.electronicstore.exceptions.ResourceAlreadyExistException;
import com.electronicstore.helper.ApiResponseMessage;
import com.electronicstore.helper.ApplicationConstants;
import com.electronicstore.helper.ImageResponse;
import com.electronicstore.helper.PageableResponse;
import com.electronicstore.services.FileService;
import com.electronicstore.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/electronicstore/user/v1")
@Tag(name = "User Controller", description = "consists APIs which are used to manage users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Operation(summary = "create new user",description = "this method can be used to create a new user")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",description = "successfully created a user"),
                    @ApiResponse(responseCode = "401",description = "forbidden")
            }
    )
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
            @RequestParam(defaultValue = ApplicationConstants.SORT_BY_FIELD_USER, value = "field", required = false) String field,
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
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId) throws IOException {
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

    //upload the user image
    @PostMapping("/upload/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("userImage") MultipartFile multiPartFile, @PathVariable String userId) throws IOException {
        logger.info("user controller -> uploadUserImage() ");
        UserDto userByUserId = this.userService.getUserByUserId(userId);
        if(userByUserId.getImageName()!=null){
            throw new ResourceAlreadyExistException("Profile picture","user",userId);
        }
        String imageName = fileService.uploadFile(multiPartFile, imageUploadPath);
        ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).success(true).status(HttpStatus.CREATED).message("Profile picture uploaded successfully enjoy !").build();
        userByUserId.setImageName(imageName);
        this.userService.updateUser(userByUserId,userId);
        logger.info("Image response {}",imageResponse);
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }

    //retrieve the profile picture by passing the userId
    @GetMapping("/retrieve/profilepic/{userId}")
    public void retrieveProfilePictureByUserId(@PathVariable String userId, HttpServletResponse httpServletResponse) throws IOException {
        logger.info("retrieveProfilePictureByUserId triggered");
        UserDto userByUserId = this.userService.getUserByUserId(userId);
        logger.info(userByUserId.getName()+" image name :{} "+userByUserId.getImageName());
        InputStream resource = this.fileService.getResource(imageUploadPath, userByUserId.getImageName());
        httpServletResponse.setContentType(MediaType.IMAGE_JPEG_VALUE);
        //add to response
        StreamUtils.copy(resource,httpServletResponse.getOutputStream());

    }

    @PutMapping("/update/image/{userId}")
    public ResponseEntity<ImageResponse> updateUserImage(@RequestParam("userImage") MultipartFile multiPartFile, @PathVariable String userId) throws IOException {
        logger.info("user controller -> updateUserImage() ");
        String  newProfileImageName = this.fileService.updateImage(multiPartFile, imageUploadPath, userId);
        ImageResponse imageResponse = ImageResponse.builder().imageName(newProfileImageName).success(true).status(HttpStatus.CREATED).message("Profile picture updated successfully enjoy !").build();
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }




}
