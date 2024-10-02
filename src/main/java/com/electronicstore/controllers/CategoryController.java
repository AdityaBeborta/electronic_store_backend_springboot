package com.electronicstore.controllers;
import com.electronicstore.dtos.CategoryDto;
import com.electronicstore.dtos.UserDto;
import com.electronicstore.helper.ApiResponseMessage;
import com.electronicstore.helper.ApplicationConstants;
import com.electronicstore.helper.ImageResponse;
import com.electronicstore.helper.PageableResponse;
import com.electronicstore.services.CategoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/electronicstore/category/v1")
public class CategoryController {

    @Value("${category.cover.image.path}")
    private String imageUploadPath;

    @Autowired
    private CategoryService categoryService;

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @PostMapping("/add")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){

        CategoryDto categoryDtoFromDB = this.categoryService.addCategory(categoryDto);

        return new ResponseEntity<>(categoryDtoFromDB, HttpStatus.CREATED);
    }

    @GetMapping("/getAllCategories")
    public ResponseEntity<PageableResponse<CategoryDto>> getAllUser(
            @RequestParam(defaultValue = ApplicationConstants.PAGE_NUMBER, value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(defaultValue = ApplicationConstants.PAGE_SIZE, value = "pageSize", required = false) Integer pageSize,
            @RequestParam(defaultValue = ApplicationConstants.SORT_BY_FIELD_CATEGORY, value = "field", required = false) String field,
            @RequestParam(defaultValue = ApplicationConstants.SORT_BY_DIRECTION, value = "direction", required = false) String direction
    ){
        logger.info("category controller pageNumber {}",pageNumber);
        logger.info("category controller pageSize {}",pageSize);
        logger.info("category controller field {}",field);
        logger.info("category controller direction {}",direction);
        PageableResponse<CategoryDto> allCategories = this.categoryService.getAllCategories(pageNumber, pageSize, field, direction);
        return new ResponseEntity<>(allCategories,HttpStatus.OK);
    }

    @GetMapping("/getASingleCategory/{categoryId}")
    public ResponseEntity<CategoryDto> getASingleCategory(@PathVariable String categoryId){
        return new ResponseEntity<>(this.categoryService.getASingleCategory(categoryId),HttpStatus.FOUND);
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId){
        return new ResponseEntity<>(this.categoryService.deleteCategory(categoryId),HttpStatus.OK);
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto,@PathVariable String categoryId){
        return new ResponseEntity<>(this.categoryService.updateCategory(categoryId,categoryDto),HttpStatus.OK);
    }

    @PostMapping("/uploadOrUpdateCategoryImage/{categoryId}")
    public ResponseEntity<ImageResponse> uploadOrUpdateTheCategoryImage(@RequestParam("categoryImage") MultipartFile multipartFile,@PathVariable String categoryId) throws IOException {


        return new ResponseEntity<>(this.categoryService.uploadCategoryImage(multipartFile,imageUploadPath,categoryId),HttpStatus.OK);
    }




}
