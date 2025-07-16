package model.constants;

public class UrlPath {
    public static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/";
    public static final String CREATE_COURIER_URL_PATH = "/api/v1/courier";
    public static final String LOGIN_COURIER_URL_PATH = "/api/v1/courier/login";
    public static final String DELETE_COURIER_URL_PATH = "/api/v1/courier/%s";
    public static final String CREATE_ORDER_URL_PATH = "/api/v1/orders";
    public static final String GET_ORDER_LIST_URL_PATH = "/api/v1/orders";
    public static final String ACCEPT_ORDER_URL_PATH = "/api/v1/orders/accept/%s";
    public static final String GET_ORDER_BY_TRACK_URL_PATH = "/api/v1/orders/track";
}
