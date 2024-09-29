package com.electronicstore.services.impl;

import com.electronicstore.dtos.CategoryDto;
import com.electronicstore.entities.Category;
import com.electronicstore.exceptions.ResourceNotFoundException;
import com.electronicstore.helper.ApiResponseMessage;
import com.electronicstore.helper.PageableResponse;
import com.electronicstore.repositories.CategoryRepository;
import com.electronicstore.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    private static final Logger logger=LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);
        Category map = this.modelMapper.map(categoryDto, Category.class);
        Category category = this.categoryRepository.save(map);
        CategoryDto categoryDtoFromDB = this.modelMapper.map(category, CategoryDto.class);
        logger.info("Category from DB {}",categoryDtoFromDB);
        return categoryDtoFromDB;
    }

    @Override
    public PageableResponse<CategoryDto> getAllCategories(int pageNumber, int pageSize, String field, String direction) {
        //coding for sorting
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(field).ascending() : Sort.by(field).descending();
        PageRequest pageableRequest = PageRequest.of(pageNumber, pageSize, sort);
        Page<Category> allCategoriesWithPagination = this.categoryRepository.findAll(pageableRequest);
        List<Category> allCategoriesWithOnlyContent = allCategoriesWithPagination.getContent();
        if(allCategoriesWithPagination.getContent().size()==0){
            throw new ResourceNotFoundException("page","page number",String.valueOf(allCategoriesWithPagination.getNumber()));
        }
        PageableResponse pageableResponse = new PageableResponse();
        pageableResponse.setContent(allCategoriesWithOnlyContent.stream().map(category -> this.modelMapper.map(category,CategoryDto.class)).collect(Collectors.toList()));
        pageableResponse.setLastPage(allCategoriesWithPagination.isLast());
        pageableResponse.setPageSize(allCategoriesWithPagination.getSize());
        pageableResponse.setTotalPages(allCategoriesWithPagination.getTotalPages());
        pageableResponse.setPageNumber(allCategoriesWithPagination.getNumber());
        pageableResponse.setTotalElements(allCategoriesWithPagination.getTotalElements());

        return pageableResponse;
    }

    @Override
    public CategoryDto getASingleCategory(String categoryId) {
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "category id", categoryId));
        CategoryDto categoryDto = this.modelMapper.map(category, CategoryDto.class);
        logger.info("single category {}",categoryDto);
        return categoryDto;
    }

    @Override
    public CategoryDto updateCategory(String categoryId, CategoryDto categoryDto) {
        CategoryDto categoryFromDB = this.getASingleCategory(categoryId);
        categoryFromDB.setCategoryDescription(categoryDto.getCategoryDescription());
        categoryFromDB.setCategoryTitle(categoryDto.getCategoryTitle());
        return this.addCategory(categoryFromDB);
    }

    @Override
    public ApiResponseMessage deleteCategory(String categoryId) {
        this.getASingleCategory(categoryId);
        this.categoryRepository.deleteById(categoryId);
        ApiResponseMessage apiResponse = ApiResponseMessage.builder().message("category deleted successfully").status(HttpStatus.OK).success(true).build();
        return apiResponse;
    }

    @Override
    public CategoryDto uploadCategoryImage(MultipartFile multipartFile, String filePath, String categoryId) {
        return null;
    }

    @Override
    public void retrieveCategoryImage(String filePath, String fileName) {

    }
}
