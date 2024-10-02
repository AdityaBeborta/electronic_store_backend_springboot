package com.electronicstore.controllers;
import com.electronicstore.dtos.CategoryDto;
import com.electronicstore.dtos.ProductDto;
import com.electronicstore.dtos.UserDto;
import com.electronicstore.helper.ApiResponseMessage;
import com.electronicstore.helper.ApplicationConstants;
import com.electronicstore.helper.ImageResponse;
import com.electronicstore.helper.PageableResponse;
import com.electronicstore.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/electronicstore/product/v1")
public class ProductController {



    @Value("${product.cover.image.path}")
    private String imageUploadPath;
    @Autowired
    private ProductService productService;

    @PostMapping("/addProduct")
    public ResponseEntity<ProductDto> addProduct(@Valid @RequestBody ProductDto productDto){
        return new ResponseEntity<>(productService.addProduct(productDto), HttpStatus.CREATED);
    }

    @GetMapping("/getAllProducts")
    public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(
            @RequestParam(defaultValue = ApplicationConstants.PAGE_NUMBER, value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(defaultValue = ApplicationConstants.PAGE_SIZE, value = "pageSize", required = false) Integer pageSize,
            @RequestParam(defaultValue = ApplicationConstants.SORT_BY_FIELD_PRODUCT, value = "field", required = false) String field,
            @RequestParam(defaultValue = ApplicationConstants.SORT_BY_DIRECTION, value = "direction", required = false) String direction
    ){

        PageableResponse<ProductDto> allProducts = this.productService.getAllProducts(pageNumber, pageSize, field, direction);
        return new ResponseEntity<>(allProducts,HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getASingleProductByPId(@PathVariable String productId){

        return new ResponseEntity<>(this.productService.getASingleProduct(productId),HttpStatus.FOUND);
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ProductDto> updateExistingProduct(@Valid @RequestBody ProductDto productDto,@PathVariable String productId){
        return new ResponseEntity<>(this.productService.updateExistingProduct(productDto,productId),HttpStatus.OK);
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ApiResponseMessage> deleteExistingProduct(@PathVariable String productId){
        return new ResponseEntity<>(this.productService.deleteExistingProduct(productId),HttpStatus.OK);
    }

    @GetMapping("/getAllLiveProducts")
    public ResponseEntity<List<ProductDto>> getAllLiveProducts(){
        List<ProductDto> allLiveProducts = this.productService.getAllLiveProducts();
        return new ResponseEntity<>(allLiveProducts,HttpStatus.OK);
    }

    @GetMapping("/searchProduct/{keyword}")
    public ResponseEntity<List<ProductDto>> searchProductByKeyword(@PathVariable String keyword){
        List<ProductDto> productDto = this.productService.searchProducts(keyword);
        return new ResponseEntity<>(productDto,HttpStatus.OK);

    }
    @PostMapping("/uploadOrUpdateProductImage/{productId}")
    public ResponseEntity<ImageResponse> uploadOrUpdateProductImage(@RequestParam("productImage") MultipartFile multipartFile, @PathVariable String productId) throws IOException {


        return new ResponseEntity<>(this.productService.uploadProductImage(multipartFile,imageUploadPath,productId),HttpStatus.OK);
    }

    @GetMapping("/retrieve/productImage/{productId}")
    public void retrieveProfilePictureByUserId(@PathVariable String productId, HttpServletResponse httpServletResponse) throws IOException {

        ProductDto aSingleProduct = this.productService.getASingleProduct(productId);

        InputStream resource = this.productService.retrieveProductImage(imageUploadPath, aSingleProduct.getProductImageName());
        httpServletResponse.setContentType(MediaType.IMAGE_JPEG_VALUE);
        //add to response
        StreamUtils.copy(resource,httpServletResponse.getOutputStream());

    }

}
