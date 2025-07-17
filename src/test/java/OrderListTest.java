import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static model.constants.SpecificationTemplates.REQUEST_SPECIFICATION_TEMPLATE;
import static model.constants.SpecificationTemplates.RESPONSE_SPECIFICATION_TEMPLATE;
import static model.constants.UrlPath.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Тесты на список заказов")
public class OrderListTest {

    @Test
    @DisplayName("Получение списка заказов - успех")
    public void getOrderList_success() {

        List<String> expectedOrderKeys = List.of(
                "id", "courierId", "firstName", "lastName", "address",
                "metroStation", "phone", "rentTime", "deliveryDate",
                "track", "color", "comment", "createdAt", "updatedAt", "status"
        );
        ValidatableResponse response = given()
                .spec(REQUEST_SPECIFICATION_TEMPLATE)
                .get(GET_ORDER_LIST_URL_PATH)
                .then()
                .spec(RESPONSE_SPECIFICATION_TEMPLATE);

        response.assertThat().statusCode(200).and()
                .body("orders[0].keySet()", containsInAnyOrder(expectedOrderKeys.toArray()));
    }
}