import client.QAScooterCourierClient;
import io.restassured.response.ValidatableResponse;
import model.Courier;
import model.Credentials;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static model.constants.CourierCredentials.*;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Тесты удаления курьера")
public class DeleteCourierTest {
    private QAScooterCourierClient client;
    private Courier courier;
    private String courierId;

    @BeforeEach
    public void setUp(){
        client = new QAScooterCourierClient();
        courier = new Courier(COURIER_LOGIN, COURIER_PASSWORD, COURIER_FIRSTNAME);
        client.createCourier(courier);
    }

    @AfterEach
    public void tearDown(){
        Credentials credentials = Credentials.fromCourier(courier);
        courierId = client.getCourierId(credentials);
        client.deleteCourier(courierId);
    }

    @Test
    @DisplayName("Удаление курьера - успех")
    public void deleteCourier_success(){
        Credentials credentials = Credentials.fromCourier(courier);
        client.loginCourier(credentials);
        courierId = client.getCourierId(credentials);
        ValidatableResponse response = client.deleteCourier(courierId);

        response.assertThat().statusCode(200).and().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Удаление курьера - запрос без id - ошибка")
    public void deleteCourier_emptyId_expectError(){
        courierId = "";
        ValidatableResponse response = client.deleteCourier(courierId);

        response.assertThat().statusCode(400).and()
                .body("message", equalTo("Недостаточно данных для удаления курьера"));
    }

    @Test
    @DisplayName("Удаление курьера - запрос с несуществующим id - ошибка")
    public void deleteCourier_invalidId_expectError(){
        courierId = INVALID_COURIER_ID;
        ValidatableResponse response = client.deleteCourier(courierId);

        response.assertThat().statusCode(404).and().body("message", equalTo("Курьера с таким id нет."));
    }
}
