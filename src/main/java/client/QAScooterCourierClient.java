package client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.Courier;
import model.Credentials;

import static io.restassured.RestAssured.given;
import static model.constants.SpecificationTemplates.REQUEST_SPECIFICATION_TEMPLATE;
import static model.constants.SpecificationTemplates.RESPONSE_SPECIFICATION_TEMPLATE;
import static model.constants.UrlPath.*;

public class QAScooterCourierClient {

  @Step("Создание курьера")
  public ValidatableResponse createCourier(Courier courier) {
    return given()
        .spec(REQUEST_SPECIFICATION_TEMPLATE)
        .body(courier)
        .post(CREATE_COURIER_URL_PATH)
        .then()
        .spec(RESPONSE_SPECIFICATION_TEMPLATE);
  }

  @Step("Логин курьера")
  public ValidatableResponse loginCourier(Credentials credentials) {
    return given()
        .spec(REQUEST_SPECIFICATION_TEMPLATE)
        .body(credentials)
        .post(LOGIN_COURIER_URL_PATH)
        .then()
        .spec(RESPONSE_SPECIFICATION_TEMPLATE);
  }

  @Step("Удаление курьера")
  public ValidatableResponse deleteCourier(String courierId) {
    return given()
            .spec(REQUEST_SPECIFICATION_TEMPLATE)
            .delete(String.format(DELETE_COURIER_URL_PATH, courierId))
            .then()
            .spec(RESPONSE_SPECIFICATION_TEMPLATE);
  }

  @Step("Получение id курьера")
  public String getCourierId(Credentials credentials){
    ValidatableResponse response = loginCourier(credentials);
    return  response.extract().jsonPath().getString("id");
  }
}
