package com.electronicstore.services;

import com.electronicstore.dtos.CategoryDto;
import com.electronicstore.helper.ApiResponseMessage;
import com.electronicstore.helper.PageableResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {

    //add category
    CategoryDto addCategory(CategoryDto categoryDto);

    //get all category with pagination
    PageableResponse<CategoryDto> getAllCategories(int pageNumber, int pageSize, String field, String direction);

    //get a single category with category id
    CategoryDto getASingleCategory(String categoryId);

    //update a category by passing category id and new category details
    CategoryDto updateCategory(String categoryId, CategoryDto categoryDto);

    //delete a category by passing categoryId
    ApiResponseMessage deleteCategory(String categoryId);

    //add category image
    CategoryDto uploadCategoryImage(MultipartFile multipartFile, String filePath, String categoryId);

    //retrieve category image passing categoryId
    void retrieveCategoryImage(String filePath, String fileName);

    //get a single category with cate


}
