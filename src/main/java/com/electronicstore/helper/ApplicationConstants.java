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
}
