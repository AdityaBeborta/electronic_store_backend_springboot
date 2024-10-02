package com.electronicstore.services.impl;

import com.electronicstore.dtos.CategoryDto;
import com.electronicstore.dtos.ProductDto;
import com.electronicstore.entities.Product;
import com.electronicstore.exceptions.BadApiRequest;
import com.electronicstore.exceptions.ResourceNotFoundException;
import com.electronicstore.helper.ApiResponseMessage;
import com.electronicstore.helper.ApplicationConstants;
import com.electronicstore.helper.ImageResponse;
import com.electronicstore.helper.PageableResponse;
import com.electronicstore.repositories.ProductRepository;
import com.electronicstore.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public ProductDto addProduct(ProductDto productDto) {
        productDto.setProductId(UUID.randomUUID().toString());
        productDto.setAddedDate(new Date());
        Product product = this.modelMapper.map(productDto, Product.class);
        Product productFromDB = this.productRepository.save(product);
        ProductDto productDtoFromDB = this.modelMapper.map(productFromDB, ProductDto.class);
        return productDtoFromDB;
    }

    @Override
    public ProductDto updateExistingProduct(ProductDto productDto, String productId) {
        ProductDto oldProduct = this.getASingleProduct(productId);
        oldProduct.setTitle(productDto.getTitle());
        oldProduct.setDescription(productDto.getDescription());
        oldProduct.setPrice(productDto.getPrice());
        oldProduct.setDiscountedPrice(productDto.getDiscountedPrice());
        oldProduct.setQuantity(productDto.getQuantity());
        oldProduct.setLive(productDto.isLive());
        oldProduct.setStock(productDto.isStock());
        oldProduct.setProductImageName(productDto.getProductImageName());
        Product updatedProduct = this.productRepository.save(this.modelMapper.map(oldProduct, Product.class));
        return this.modelMapper.map(updatedProduct,ProductDto.class);
    }

    @Override
    public ApiResponseMessage deleteExistingProduct(String productId) {
        ProductDto aSingleProduct = this.getASingleProduct(productId);
        this.productRepository.delete(this.modelMapper.map(aSingleProduct,Product.class));
        ApiResponseMessage apiRes = ApiResponseMessage.builder().status(HttpStatus.OK).success(true).message("product deleted successfully").build();
        return apiRes;
    }

    @Override
    public ProductDto getASingleProduct(String productId) {
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product", "product id", productId));
        ProductDto productDto = this.modelMapper.map(product, ProductDto.class);
        return productDto;
    }

    @Override
    public PageableResponse getAllProducts(int pageNumber, int pageSize, String field, String direction) {
        //coding for sorting
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(field).ascending() : Sort.by(field).descending();
        PageRequest pageableRequest = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> allProductsWithPagination = this.productRepository.findAll(pageableRequest);
        List<Product> allProductsWithContentOnly = allProductsWithPagination.getContent();
        if(allProductsWithPagination.getContent().size()==0){
            throw new ResourceNotFoundException("page","page number",String.valueOf(allProductsWithPagination.getNumber()));
        }
        PageableResponse pageableResponse = new PageableResponse();
        pageableResponse.setContent(allProductsWithContentOnly.stream().map(product -> this.modelMapper.map(product, ProductDto.class)).collect(Collectors.toList()));
        pageableResponse.setLastPage(allProductsWithPagination.isLast());
        pageableResponse.setPageSize(allProductsWithPagination.getSize());
        pageableResponse.setTotalPages(allProductsWithPagination.getTotalPages());
        pageableResponse.setPageNumber(allProductsWithPagination.getNumber());
        pageableResponse.setTotalElements(allProductsWithPagination.getTotalElements());

        return pageableResponse;
    }

    @Override
    public List<ProductDto> getAllLiveProducts() {
        List<Product> byLiveTrue = this.productRepository.findByLiveTrue();
        List<ProductDto> listOfProductDTto = byLiveTrue.stream().map(product -> this.modelMapper.map(product, ProductDto.class)).collect(Collectors.toList());
        return listOfProductDTto;
    }


    @Override
    public List<ProductDto> searchProducts(String keyword) {
        List<Product> byTitleContaining = this.productRepository.findByTitleContaining(keyword);
        List<ProductDto> listOfProductDto = byTitleContaining.stream().map(product -> this.modelMapper.map(product, ProductDto.class)).collect(Collectors.toList());
//        if(listOfProductDto.size()==0){
//            throw new ResourceNotFoundException("product","title",keyword);
//        }
        return listOfProductDto;
    }

    @Override
    public ImageResponse uploadProductImage(MultipartFile multipartFile, String filePath, String productId) throws IOException {
        //find the category details with the category id
        ProductDto aSingleProduct = this.getASingleProduct(productId);

        System.out.println(aSingleProduct);

        //Create an object of ImageResponse
        ImageResponse imageResponse=new ImageResponse();

        //now find the image details
        String imageNameFromDB = aSingleProduct.getProductImageName();
        System.out.println("imageNameFromDB "+imageNameFromDB);

        //full path for new image
        String fullPathNewWithProductImageName = "";

        //full path for the existing image
        String fullPathOldWithProductImageName = filePath + File.separator + imageNameFromDB;

        //get the original file name
        String originalProductImageName = multipartFile.getOriginalFilename();

        //generate a unique name
        String randomProductImageName = UUID.randomUUID().toString();

        //now get the extension from the originalCategoryImageName

        String originalProductImageExtension = originalProductImageName.substring(originalProductImageName.lastIndexOf("."));

        //now append the unique file name with the extension
        String uniqueProductImageName = randomProductImageName+originalProductImageExtension;

        //full path with unique file name

        fullPathNewWithProductImageName = filePath +File.separator+ uniqueProductImageName;



        //now check if the extension is valid

        if(ApplicationConstants.ALLOWED_FILE_TYPES.contains((originalProductImageExtension))){

            //valid extension check if image is already exist

            if(imageNameFromDB!=null){
                System.out.println("image name fro DB is not null");
                //delete from the folder
                Path of = Path.of(fullPathOldWithProductImageName);
                Files.delete(of);
            }

            //if image does not exist the just update it
            System.out.println("not inside IF");

            File folder = new File(filePath);
            if(!folder.exists()){
                //create a folder

                folder.mkdirs();
            }

            //upload the file to fullPath
            Files.copy(multipartFile.getInputStream(), Paths.get(fullPathNewWithProductImageName));
            //now update the new profile in DB
            aSingleProduct.setProductImageName(uniqueProductImageName);
            this.updateExistingProduct(aSingleProduct,productId);
            imageResponse.setImageName(uniqueProductImageName);
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
    public InputStream retrieveProductImage(String path, String name) throws FileNotFoundException {
        String fullPath = path + File.separator + name;
        InputStream inputStream = new FileInputStream(fullPath);
        if(inputStream==null){
            throw new FileNotFoundException();
        }
        return inputStream;
    }
}
