import model.Courier;
import model.Credentials;
import client.QAScooterCourierClient;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static model.constants.CourierCredentials.*;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Тесты на логин курьера")
public class LoginCourierTest {
  private QAScooterCourierClient client;
  private Courier courier;
  private String courierId;

  @BeforeEach
  public  void setup() {
    client = new QAScooterCourierClient();
    courier = new Courier(COURIER_LOGIN, COURIER_PASSWORD, COURIER_FIRSTNAME);
    client.createCourier(courier);
  }

  @AfterEach
  public  void tearDown() {
    Credentials credentials = Credentials.fromCourier(courier);
    courierId = client.getCourierId(credentials);
    client.deleteCourier(courierId);
  }


  @Test
  @DisplayName("Логин курьера – успех")
  public void loginCourier_success() {
    Credentials credentials = Credentials.fromCourier(courier);
    ValidatableResponse response = client.loginCourier(credentials);

    response.assertThat().statusCode(200).and()
            .body("id", equalTo(Integer.parseInt(client.getCourierId(credentials))));
  }

  @Test
  @DisplayName("Логин курьера - несуществующая пара логин-пароль - ошибка")
  public void loginCourier_nonexistentCredentials_expectError(){
    Credentials credentials = new Credentials(NONEXISTENT_LOGIN, NONEXISTENT_PASSWORD);
    ValidatableResponse response = client.loginCourier(credentials);

    response.assertThat().statusCode(404).and()
            .body("message", equalTo("Учетная запись не найдена"));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("objectProvider")
  @DisplayName("Логин курьера - без логина или пароля – ошибка")
  public void loginCourier_badCredentials_expectError(String testDescription, String login,
                                                       String password) {
    Credentials credentials = new Credentials(login, password);
    ValidatableResponse response = client.loginCourier(credentials);

    response.assertThat().statusCode(400).and()
            .body("message", equalTo("Недостаточно данных для входа"));

  }

  static Stream<Arguments> objectProvider() {
    return Stream.of(
            Arguments.of("Авторизация курьера без логина", EMPTY_LOGIN, COURIER_PASSWORD),
            Arguments.of("Авторизация курьера без пароля", COURIER_LOGIN, EMPTY_PASSWORD)
    );
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("objectProviderInvalidCredentials")
  @DisplayName("Логин курьера - неверный логин или пароль – ошибка")
  public void loginCourier_invalidCredentials_expectError(String testDescription, String login,
                                                      String password) {
    Credentials credentials = new Credentials(login, password);
    ValidatableResponse response = client.loginCourier(credentials);

    response.assertThat().statusCode(404).and()
            .body("message", equalTo("Учетная запись не найдена"));

  }
  static Stream<Arguments> objectProviderInvalidCredentials() {
    return Stream.of(
            Arguments.of("Авторизация курьера с неверным логином", NONEXISTENT_LOGIN, COURIER_PASSWORD),
            Arguments.of("Авторизация курьера без неверным паролем", COURIER_LOGIN, NONEXISTENT_PASSWORD)
    );
  }
}
