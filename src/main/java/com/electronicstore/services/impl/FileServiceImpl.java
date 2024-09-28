package com.electronicstore.services.impl;
import com.electronicstore.dtos.UserDto;
import com.electronicstore.exceptions.BadApiRequest;
import com.electronicstore.helper.ApplicationConstants;
import com.electronicstore.services.FileService;
import com.electronicstore.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private UserService userService;


    @Override

    public String uploadFile(MultipartFile multipartFile, String path) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        logger.info("original file name : {}",originalFilename);
        String fileName = UUID.randomUUID().toString();
        //get the extension from the file name
        //suppose the file name is abc.png then get substring from .
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFileName = fileName+extension;
        logger.info("modified file name  : {}",uniqueFileName);
        //full path with file name
        String fullPathWithFileName = path + File.separator + uniqueFileName;
        logger.info("fullPathWithFileName : {}",fullPathWithFileName);
        if (ApplicationConstants.ALLOWED_FILE_TYPES.contains(extension)) {
            //create a folder to store the images
            logger.info("valid file extension: {}",extension);

            File folder = new File(path);
            if(!folder.exists()){
                //create a folder
                logger.info("folder created");
                folder.mkdirs();
            }

            //upload the file to fullPath
            Files.copy(multipartFile.getInputStream(), Paths.get(fullPathWithFileName));
            return uniqueFileName;
        }
        else{
            //throw the exception
            logger.info("invalid file extension: {}",extension);
            throw new BadApiRequest("The only allowed extension are "+ApplicationConstants.ALLOWED_FILE_TYPES);
        }
    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        String fullPath = path + File.separator + name;
        InputStream inputStream = new FileInputStream(fullPath);
        if(inputStream==null){
            logger.info("File not found ");
            throw new FileNotFoundException();
        }
        return inputStream;
    }

    @Override
    public String updateImage(MultipartFile multipartFile, String path, String resourceId) throws IOException {
        String newProfileImage = multipartFile.getOriginalFilename();
        //now find the image details with the help of resourceId

        UserDto userByUserId = this.userService.getUserByUserId(resourceId);
        //get the image name from userByUserId bean and delete it

        String oldProfileImage = userByUserId.getImageName();

        String oldPathNameWithImage=path+File.separator+oldProfileImage;

        Path pathDetails = Path.of(oldPathNameWithImage);

        Files.delete(pathDetails);

        //now get the file extension from the new profile image

        String newProfilePicExtension = newProfileImage.substring(newProfileImage.lastIndexOf('.'));

        //generate a random file name using UUID

        String randomFileName = UUID.randomUUID().toString();

        //now append the randomFileName with the extension

        String uniqueFileName = randomFileName + newProfilePicExtension;

        //now define the complete path with path and file name

        String newPathWithPathNameAndImageName = path+File.separator+uniqueFileName;

        if (ApplicationConstants.ALLOWED_FILE_TYPES.contains(newProfilePicExtension)) {
            //create a folder to store the images
            logger.info("valid file extension: {}",newProfilePicExtension);

            File folder = new File(path);
            if(!folder.exists()){
                //create a folder
                logger.info("folder created");
                folder.mkdirs();
            }

            //upload the file to fullPath
            Files.copy(multipartFile.getInputStream(), Paths.get(newPathWithPathNameAndImageName));
            //now update the new profile in DB
            userByUserId.setImageName(uniqueFileName);
            this.userService.updateUser(userByUserId,resourceId);
            return uniqueFileName;
        }
        else{
            //throw the exception
            logger.info("invalid file extension: {}",newProfilePicExtension);
            throw new BadApiRequest("The only allowed extension are "+ApplicationConstants.ALLOWED_FILE_TYPES);
        }




    }
}
