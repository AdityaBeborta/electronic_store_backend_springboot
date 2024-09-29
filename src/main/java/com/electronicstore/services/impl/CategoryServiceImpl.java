package com.electronicstore.services.impl;

import com.electronicstore.dtos.CategoryDto;
import com.electronicstore.entities.Category;
import com.electronicstore.exceptions.BadApiRequest;
import com.electronicstore.exceptions.ResourceNotFoundException;
import com.electronicstore.helper.ApiResponseMessage;
import com.electronicstore.helper.ApplicationConstants;
import com.electronicstore.helper.ImageResponse;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        categoryFromDB.setCategoryCoverImage(categoryDto.getCategoryCoverImage());
        categoryFromDB.setCategoryTitle(categoryDto.getCategoryTitle());
        Category save = this.categoryRepository.save(this.modelMapper.map(categoryDto, Category.class));
        return this.modelMapper.map(save,CategoryDto.class);
    }

    @Override
    public ApiResponseMessage deleteCategory(String categoryId) {
        this.getASingleCategory(categoryId);
        this.categoryRepository.deleteById(categoryId);
        ApiResponseMessage apiResponse = ApiResponseMessage.builder().message("category deleted successfully").status(HttpStatus.OK).success(true).build();
        return apiResponse;
    }

    @Override
    public ImageResponse uploadCategoryImage(MultipartFile multipartFile, String filePath, String categoryId) throws IOException {

        //find the category details with the category id
        CategoryDto aSingleCategory = this.getASingleCategory(categoryId);

        //Create an object of ImageResponse
        ImageResponse imageResponse=new ImageResponse();

        //now find the image details
        String imageNameFromDB = aSingleCategory.getCategoryCoverImage();

        //full path for new image
        String fullPathNewWithCategoryImageName = "";

        //full path for the existing image
        String fullPathOldWithCategoryImageName = filePath + File.separator + imageNameFromDB;

        //get the original file name
        String originalCategoryImageName = multipartFile.getOriginalFilename();

        //generate a unique name
        String randomCategoryImageName = UUID.randomUUID().toString();

        //now get the extension from the originalCategoryImageName

        String originalCategoryImageExtension = originalCategoryImageName.substring(originalCategoryImageName.lastIndexOf("."));

        //now append the unique file name with the extension
        String uniqueCategoryImageName = randomCategoryImageName+originalCategoryImageExtension;

        //full path with unique file name

        fullPathNewWithCategoryImageName = filePath +File.separator+ uniqueCategoryImageName;



        //now check if the extension is valid

        if(ApplicationConstants.ALLOWED_FILE_TYPES.contains((originalCategoryImageExtension))){

            //valid extension check if image is already exist

            if(imageNameFromDB!=null){
                //delete from the folder
                Path of = Path.of(fullPathOldWithCategoryImageName);
                Files.delete(of);
            }

            //if image does not exist the just update it

            File folder = new File(filePath);
            if(!folder.exists()){
                //create a folder
                logger.info("folder created");
                folder.mkdirs();
            }

            //upload the file to fullPath
            Files.copy(multipartFile.getInputStream(), Paths.get(fullPathNewWithCategoryImageName));
            //now update the new profile in DB
            aSingleCategory.setCategoryCoverImage(uniqueCategoryImageName);
            this.updateCategory(categoryId,aSingleCategory);
            imageResponse.setImageName(uniqueCategoryImageName);
            imageResponse.setSuccess(true);
            imageResponse.setStatus(HttpStatus.OK);
            imageResponse.setMessage("successfully uploaded / updated the Image");
            System.out.println(imageResponse);
            return imageResponse;

        }
        else{
            throw new BadApiRequest("The only allowed extension are "+ApplicationConstants.ALLOWED_FILE_TYPES);
        }

    }

    @Override
    public void retrieveCategoryImage(String filePath, String fileName) {

    }
}
