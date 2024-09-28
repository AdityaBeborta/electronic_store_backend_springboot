package com.electronicstore.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FileService {

    String uploadFile(MultipartFile multipartFile, String path) throws IOException;

    InputStream getResource(String path, String name) throws FileNotFoundException;

    String updateImage(MultipartFile multipartFile,String path,String resourceId) throws IOException;
}
