package com.electronicstore.helper;

import java.util.List;

public class ApplicationConstants {
    public static final String PAGE_NUMBER = "0";
    public static final String PAGE_SIZE = "5";
    public static final String SORT_BY_FIELD_USER = "name";
    public static final String SORT_BY_FIELD_CATEGORY = "categoryTitle";
    public static final String SORT_BY_FIELD_PRODUCT = "title";
    public static final String SORT_BY_DIRECTION = "asc";
    public static final String IMAGE_UPLOAD_PATH = "asc";
    public static final List<String> ALLOWED_FILE_TYPES = List.of(".jpg", ".png", ".jpeg");
    public static final String ADD_ITEM_TO_CART = "ADD";
    public static final String REMOVE_ITEM_FROM_CART = "REMOVE";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_GUEST = "ROLE_GUEST";
    public static final String[] PUBLIC_URLS_GET = {
            "/electronicstore/user/v1/**",
            "/electronicstore/product/v1/**",
            "/electronicstore/category/v1/**",
            "/electronicstore/cart/v1/**",
            "/electronicstore/orders/v1/**",
    };
    public static final String[] PROTECTED_URLS_POST = {
            "/electronicstore/user/v1/**",
            "/electronicstore/auth/v1/**"
    };
    public static final String[] PROTECTED_URLS_POST_ADMIN_USER = {
            "/electronicstore/product/v1/**",
            "/electronicstore/category/v1/**",

    };
    public static final String[] PROTECTED_URLS_PUT_ADMIN_USER = {
            "/electronicstore/product/v1/**",
            "/electronicstore/category/v1/**",
            "/electronicstore/orders/v1/**",
    };
    public static final String[] PROTECTED_URLS_DELETE_ADMIN_USER = {
            "/electronicstore/product/v1/**",
            "/electronicstore/category/v1/**",
            "/electronicstore/user/v1/**",
            "/electronicstore/orders/v1/**",
    };

}
