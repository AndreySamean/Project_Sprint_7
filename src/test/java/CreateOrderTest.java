import client.QAScooterOrderClient;
import io.restassured.response.ValidatableResponse;
import model.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static model.constants.OrderDetails.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Тесты на создание заказа")
public class CreateOrderTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("objectProvider")
    @DisplayName("Создание заказа с различными цветами - успех")
    public void createOrder_success(String testDescription, String firstName, String lastName,
                                    String address, String metroStation, String phone,
                                    int rentTime, String deliveryDate, String comment, String[] color){
        Order order = new Order(firstName, lastName, address, metroStation,
                phone, rentTime, deliveryDate, comment, color);
        QAScooterOrderClient orderClient = new QAScooterOrderClient();
        ValidatableResponse response = orderClient.createOrder(order);

        response.assertThat().statusCode(201).and().body("track", instanceOf(Integer.class));
    }

    static Stream<Arguments> objectProvider() {
        String[] blackColor = {BLACK_COLOR};
        String[] greyColor = {GREY_COLOR};
        String[] bothColors = {BLACK_COLOR, GREY_COLOR};
        return Stream.of(
                Arguments.of("Создание заказа, цвет - чёрный", CUSTOMER_FIRST_NAME,
                        CUSTOMER_LAST_NAME, ADDRESS, METRO_STATION, PHONE, RENT_TIME,
                        DELIVERY_DATE, COMMENT, blackColor),
                Arguments.of("Создание заказа, цвет - серый", CUSTOMER_FIRST_NAME,
                        CUSTOMER_LAST_NAME, ADDRESS, METRO_STATION, PHONE, RENT_TIME,
                        DELIVERY_DATE, COMMENT, greyColor),
                Arguments.of("Создание заказа, цвет - отсутствует", CUSTOMER_FIRST_NAME,
                        CUSTOMER_LAST_NAME, ADDRESS, METRO_STATION, PHONE, RENT_TIME,
                        DELIVERY_DATE, COMMENT, null),
                Arguments.of("Создание заказа, цвет - все", CUSTOMER_FIRST_NAME,
                        CUSTOMER_LAST_NAME, ADDRESS, METRO_STATION, PHONE, RENT_TIME,
                        DELIVERY_DATE, COMMENT, bothColors)
        );
    }
}
