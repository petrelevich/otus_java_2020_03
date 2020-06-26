package ru.otus.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.http.HttpMethod;
import org.junit.jupiter.api.*;
import ru.otus.dao.UserDao;
import ru.otus.model.User;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.UserAuthService;

import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static ru.otus.server.utils.HttpUrlConnectionHelper.*;
import static ru.otus.server.utils.WebServerHelper.*;

@DisplayName("Тест сервера должен ")
class UsersWebServerImplTest {

    private static final int WEB_SERVER_PORT = 8989;
    private static final String WEB_SERVER_URL = "http://localhost:" + WEB_SERVER_PORT + "/";
    private static final String LOGIN_URL = "login";
    private static final String API_USER_URL = "api/user";

    private static final long DEFAULT_USER_ID = 1L;
    private static final String DEFAULT_USER_LOGIN = "user1";
    private static final String DEFAULT_USER_PASSWORD = "11111";
    private static final User DEFAULT_USER = new User(DEFAULT_USER_ID, "Vasya", DEFAULT_USER_LOGIN, DEFAULT_USER_PASSWORD);
    private static final String INCORRECT_USER_LOGIN = "BadUser";

    private static Gson gson;
    private static UsersWebServer webServer;

    @BeforeAll
    static void setUp() throws Exception {
        TemplateProcessor templateProcessor = mock(TemplateProcessor.class);
        UserDao userDao = mock(UserDao.class);
        UserAuthService userAuthService = mock(UserAuthService.class);

        given(userAuthService.authenticate(DEFAULT_USER_LOGIN, DEFAULT_USER_PASSWORD)).willReturn(true);
        given(userAuthService.authenticate(INCORRECT_USER_LOGIN, DEFAULT_USER_PASSWORD)).willReturn(false);
        given(userDao.findById(DEFAULT_USER_ID)).willReturn(Optional.of(DEFAULT_USER));

        gson = new GsonBuilder().serializeNulls().create();
        webServer = new UsersWebServerWithFilterBasedSecurity(WEB_SERVER_PORT, userAuthService, userDao, gson, templateProcessor);
        webServer.start();
    }

    @AfterAll
    static void tearDown() throws Exception {
        webServer.stop();
    }

    @DisplayName("возвращать 302 при запросе пользователя по id если не выполнен вход ")
    @Test
    void shouldReturnForbiddenStatusForUserRequestWhenUnauthorized() throws Exception {
        HttpURLConnection connection = sendRequest(buildUrl(WEB_SERVER_URL, API_USER_URL, null), HttpMethod.GET);
        connection.setInstanceFollowRedirects(false);
        int responseCode = connection.getResponseCode();
        assertThat(responseCode).isEqualTo(HttpURLConnection.HTTP_MOVED_TEMP);
    }

    @DisplayName("возвращать ID сессии при выполнении входа с верными данными")
    @Test
    void shouldReturnJSessionIdWhenLoggingInWithCorrectData() throws Exception {
        HttpCookie jSessionIdCookie = login(buildUrl(WEB_SERVER_URL, LOGIN_URL, null), DEFAULT_USER_LOGIN, DEFAULT_USER_PASSWORD);
        assertThat(jSessionIdCookie).isNotNull();
    }

    @DisplayName("не возвращать ID сессии при выполнении входа если данные входа не верны")
    @Test
    void shouldNotReturnJSessionIdWhenLoggingInWithIncorrectData() throws Exception {
        HttpCookie jSessionIdCookie = login(buildUrl(WEB_SERVER_URL, LOGIN_URL, null), INCORRECT_USER_LOGIN, DEFAULT_USER_PASSWORD);
        assertThat(jSessionIdCookie).isNull();
    }

    @DisplayName("возвращать корректные данные при запросе пользователя по id если вход выполнен")
    @Test
    void shouldReturnCorrectUserWhenAuthorized() throws IOException {
        HttpCookie jSessionIdCookie = login(buildUrl(WEB_SERVER_URL, LOGIN_URL, null), DEFAULT_USER_LOGIN, DEFAULT_USER_PASSWORD);

        HttpURLConnection connection = sendRequest(buildUrl(WEB_SERVER_URL, API_USER_URL, List.of(String.valueOf(DEFAULT_USER_ID))), HttpMethod.GET);
        connection.setRequestProperty(COOKIE_HEADER, String.format("%s=%s", jSessionIdCookie.getName(), jSessionIdCookie.getValue()));
        int responseCode = connection.getResponseCode();
        String response = readResponseFromConnection(connection);

        assertThat(responseCode).isEqualTo(HttpURLConnection.HTTP_OK);
        assertThat(response).isEqualTo(gson.toJson(DEFAULT_USER));
    }
}