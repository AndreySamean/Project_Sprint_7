package client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.Order;

import static io.restassured.RestAssured.given;
import static model.constants.SpecificationTemplates.*;
import static model.constants.UrlPath.*;

public class QAScooterOrderClient {

    @Step("Создать заказ")
    public ValidatableResponse createOrder(Order order){
        return given().spec(REQUEST_SPECIFICATION_TEMPLATE)
                .body(order)
                .post(CREATE_ORDER_URL_PATH)
                .then()
                .spec(RESPONSE_SPECIFICATION_TEMPLATE);
    }

    @Step("Получить список заказов")
    public ValidatableResponse getOrderList(){
        return given()
                .spec(REQUEST_SPECIFICATION_TEMPLATE)
                .get(GET_ORDER_LIST_URL_PATH)
                .then()
                .spec(RESPONSE_SPECIFICATION_TEMPLATE);
    }

    @Step("Принять заказ")
    public ValidatableResponse acceptOrder(String orderId, String courierId) {
        if (orderId == null || orderId.isEmpty()) {
            return given()
                    .spec(REQUEST_SPECIFICATION_TEMPLATE)
                    .put(String.format(ACCEPT_ORDER_URL_PATH, courierId))
                    .then()
                    .spec(RESPONSE_SPECIFICATION_TEMPLATE);
        } else {
            return given()
                    .spec(REQUEST_SPECIFICATION_TEMPLATE)
                    .queryParam("courierId", courierId)
                    .put(String.format(ACCEPT_ORDER_URL_PATH, orderId))
                    .then()
                    .spec(RESPONSE_SPECIFICATION_TEMPLATE);
        }
    }

    @Step("Получить заказ по номеру")
    public ValidatableResponse getOrderById(String orderTrack){
        return given()
                .spec(REQUEST_SPECIFICATION_TEMPLATE)
                .queryParam("t", orderTrack)
                .get(GET_ORDER_BY_TRACK_URL_PATH)
                .then()
                .spec(RESPONSE_SPECIFICATION_TEMPLATE);
    }
}