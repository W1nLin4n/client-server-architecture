package com.w1nlin4n.practice3.networking.message;

public enum MessageCommand {
    INFORMATION,
    ERROR,
    CREATE_PRODUCT,
    GET_PRODUCT,
    UPDATE_PRODUCT,
    DELETE_PRODUCT,
    ADD_PRODUCT_AMOUNT,
    REMOVE_PRODUCT_AMOUNT,
    GET_ALL_PRODUCTS,
    CREATE_PRODUCT_GROUP,
    GET_PRODUCT_GROUP,
    UPDATE_PRODUCT_GROUP,
    DELETE_PRODUCT_GROUP,
    ADD_PRODUCT_TO_GROUP,
    GET_PRODUCTS_FROM_GROUP,
    GET_ALL_PRODUCT_GROUPS;

    public static MessageCommand getCommand(int command) {
        return switch (command) {
            case 0 -> INFORMATION;
            case 1 -> ERROR;
            case 2 -> CREATE_PRODUCT;
            case 3 -> GET_PRODUCT;
            case 4 -> UPDATE_PRODUCT;
            case 5 -> DELETE_PRODUCT;
            case 6 -> ADD_PRODUCT_AMOUNT;
            case 7 -> REMOVE_PRODUCT_AMOUNT;
            case 8 -> GET_ALL_PRODUCTS;
            case 9 -> CREATE_PRODUCT_GROUP;
            case 10 -> GET_PRODUCT_GROUP;
            case 11 -> UPDATE_PRODUCT_GROUP;
            case 12 -> DELETE_PRODUCT_GROUP;
            case 13 -> ADD_PRODUCT_TO_GROUP;
            case 14 -> GET_PRODUCTS_FROM_GROUP;
            case 15 -> GET_ALL_PRODUCT_GROUPS;
            default -> null;
        };
    }
}
