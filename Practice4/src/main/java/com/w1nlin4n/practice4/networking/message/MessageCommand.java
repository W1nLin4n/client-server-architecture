package com.w1nlin4n.practice4.networking.message;

public enum MessageCommand {
    ESTABLISH_CONNECTION,
    CLOSE_CONNECTION,
    SUCCESS,
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
        return MessageCommand.values()[command];
    }
}
