import model.Courier;
import client.QAScooterCourierClient;
import io.restassured.response.ValidatableResponse;
import model.Credentials;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static model.constants.CourierCredentials.*;
import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Тесты на создание курьера")
public class CreateCourierTest {

  private QAScooterCourierClient client;
  private Courier courier;
  private String courierId;

  @BeforeEach
  public void setUp(){
    client = new QAScooterCourierClient();
    courier = new Courier(COURIER_LOGIN, COURIER_PASSWORD, COURIER_FIRSTNAME);
  }

  @AfterEach
  public void tearDown() {
    Credentials credentials = Credentials.fromCourier(courier);
    courierId = client.getCourierId(credentials);
    client.deleteCourier(courierId);
  }

  @Test
  @Order(1)
  @DisplayName("Создание курьера - успех")
  public void createCourier_success() {
    ValidatableResponse response = client.createCourier(courier);

    response.assertThat().statusCode(201).and().body("ok", equalTo(true));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("objectProvider")
  @Order(2)
  @DisplayName("Создание курьера – без логина или пароля – ошибка")
  public void createCourier_badCredentials_expectError(String testDescription, String login,
                                                       String password, String firstName) {
    client = new QAScooterCourierClient();
    courier = new Courier(login, password, firstName);
    ValidatableResponse response = client.createCourier(courier);

    response.assertThat().statusCode(400).and()
            .body("message", equalTo("Недостаточно данных для создания учетной записи"));

  }


  static Stream<Arguments> objectProvider() {
    return Stream.of(
            Arguments.of("Создание курьера без логина", EMPTY_LOGIN, COURIER_PASSWORD, COURIER_FIRSTNAME),
            Arguments.of("Создание курьера без пароля", COURIER_LOGIN, EMPTY_PASSWORD, COURIER_FIRSTNAME)
    );
  }

  @Test
  @Order(3)
  @DisplayName("Создание двух одинаковых курьеров - ошибка")
  public void createTwoIdenticalCouriers_expectError() {
    client.createCourier(courier);
    ValidatableResponse responseCourier = client.createCourier(courier);

    responseCourier.assertThat().statusCode(409).and()
            .body("message", equalTo("Этот логин уже используется."));
  }

}
