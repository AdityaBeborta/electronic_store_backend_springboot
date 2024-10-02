package com.electronicstore.services;

import com.electronicstore.dtos.ProductDto;
import com.electronicstore.helper.ApiResponseMessage;
import com.electronicstore.helper.ImageResponse;
import com.electronicstore.helper.PageableResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ProductService {

    //create
    ProductDto addProduct(ProductDto productDto);
    //update
    ProductDto updateExistingProduct(ProductDto productDto,String productId);
    //delete
    ApiResponseMessage deleteExistingProduct(String productId);
    //get single
    ProductDto getASingleProduct(String productId);
    //get all
    PageableResponse getAllProducts(int pageNumber, int pageSize, String field, String direction);
    //get all live products
    List<ProductDto> getAllLiveProducts();
    //search product
    List<ProductDto> searchProducts(String keyword);
    //upload product image
    ImageResponse uploadProductImage(MultipartFile multipartFile, String filePath, String productId) throws IOException;
    //retrieve product image passing productImage
    InputStream retrieveProductImage(String filePath, String fileName) throws FileNotFoundException;

}
