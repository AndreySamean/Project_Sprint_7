import client.QAScooterOrderClient;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static model.constants.OrderDetails.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Тесты на получение заказа по номеру")
public class GetOrderByTrack {
    private QAScooterOrderClient orderClient;
    private String orderTrackId;

    @BeforeEach
    public void setUp(){
        orderClient = new QAScooterOrderClient();
        orderTrackId = orderClient.getOrderList().extract().jsonPath().getString("orders[0].track");
    }

    @Test
    @DisplayName("Получение заказа по номеру - успех")
    public void getOrder_success(){
        ValidatableResponse response = orderClient.getOrderById(orderTrackId);

        List<String> expectedOrderKeys = List.of(
                "id","firstName", "lastName", "address",
                "metroStation", "phone", "rentTime", "deliveryDate",
                "track", "color", "comment", "createdAt", "updatedAt", "status"
        );
        response.assertThat().statusCode(200);
        for (String key : expectedOrderKeys) {
            response.body("order", hasKey(key));
        }
    }

    @Test
    @DisplayName("Получение заказа по несуществующему номеру - ошибка")
    public void getOrder_invalidOrderTrackId_expectError(){
        ValidatableResponse response = orderClient.getOrderById(INVALID_ORDER_TRACK_ID);
        response.assertThat().statusCode(404).and()
                .body("message", equalTo("Заказ не найден"));
    }

    @Test
    @DisplayName("Получение заказа без номера - ошибка")
    public void getOrder_emptyOrderTrackId_expectError(){
        ValidatableResponse response = orderClient.getOrderById(EMPTY_VALUE);
        response.assertThat().statusCode(400).and()
                .body("message", equalTo("Недостаточно данных для поиска"));
    }
}
