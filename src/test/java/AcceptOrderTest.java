import client.QAScooterCourierClient;
import client.QAScooterOrderClient;
import io.restassured.response.ValidatableResponse;
import model.Courier;
import model.Credentials;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static model.constants.CourierCredentials.*;
import static model.constants.OrderDetails.EMPTY_VALUE;
import static model.constants.OrderDetails.INVALID_ORDER_ID;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Тесты на приём заказа")
public class AcceptOrderTest {

    private QAScooterCourierClient client;
    private QAScooterOrderClient orderClient;
    private Courier courier;
    private String courierId;
    private String orderId;

    @BeforeEach
    public void setUp(){
        client = new QAScooterCourierClient();
        orderClient = new QAScooterOrderClient();
        courier = new Courier(COURIER_LOGIN, COURIER_PASSWORD, COURIER_FIRSTNAME);
        client.createCourier(courier);
        courierId = client.getCourierId(Credentials.fromCourier(courier));
        orderId = orderClient.getOrderList().extract().jsonPath().getString("orders[0].id");
    }

    @AfterEach
    public void tearDown(){
        client.deleteCourier(courierId);
    }

    @Test
    @DisplayName("Принять заказ - успех")
    public void acceptOrder_success(){
    ValidatableResponse response = orderClient.acceptOrder(orderId, courierId);

    response.assertThat().statusCode(200).and().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Принять заказ - без id курьера - ошибка")
    public void acceptOrder_emptyOrderId_expectError(){
        ValidatableResponse response = orderClient.acceptOrder(orderId, EMPTY_VALUE);

        response.assertThat().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Принять заказ - без id заказа - ошибка")
    public void acceptOrder_emptyCourierId_expectError(){
        ValidatableResponse response = orderClient.acceptOrder(EMPTY_VALUE, courierId);

        response.assertThat().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Принять заказ - неверный id заказа - ошибка")
    public void acceptOrder_invalidOrderId_expectError(){
        ValidatableResponse response = orderClient.acceptOrder(INVALID_ORDER_ID, courierId);

        response.assertThat().statusCode(404)
                .and().body("message", equalTo("Заказа с таким id не существует"));
    }

    @Test
    @DisplayName("Принять заказ - неверный id курьера - ошибка")
    public void acceptOrder_invalidCourierId_expectError(){
        ValidatableResponse response = orderClient.acceptOrder(orderId, INVALID_COURIER_ID);

        response.assertThat().statusCode(404)
                .and().body("message", equalTo("Курьера с таким id не существует"));
    }

}
