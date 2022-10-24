package fr.gvs.base.controller.user;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import fr.gvs.base.RestResponseExceptionHandler;
import fr.gvs.base.exception.Http403ForbiddenException;
import fr.gvs.base.model.User;
import fr.gvs.base.security.Role;
import fr.gvs.base.service.UserService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

@EnableWebMvc
@WebAppConfiguration
@SpringBootTest
public class GetCurrentUserControllerTests {
    @Mock
    UserService userService;

    @Autowired
    @InjectMocks
    GetCurrentUserController controller;

    AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        RestAssuredMockMvc.standaloneSetup(MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new RestResponseExceptionHandler()));
        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @AfterEach
    void close() {
        try {
            closeable.close();
        } catch (Exception e) { }
    }

    @Test
    void normal() throws Http403ForbiddenException, IOException {
        User user = new User();
        user.setId(5l);
        user.setEmail("email@ls.fr");
        user.setEnabled(true);
        user.setFirstname("firstname");
        user.setLastname("lastname");
        user.setRole(Role.SUPERVISOR);

        doReturn(Optional.of(user)).when(userService).getAuthenticatedUser();

        given()
            .when()
            .get("/api/user/current")
            .then()
            .status(HttpStatus.OK)
            .body("authenticated" , Matchers.equalTo(true))
            .body("user.id"       , Matchers.equalTo(5))
            .body("user.email"    , Matchers.equalTo("email@ls.fr"))
            .body("user.enabled"  , Matchers.equalTo(true))
            .body("user.firstname", Matchers.equalTo("firstname"))
            .body("user.lastname" , Matchers.equalTo("lastname"))
            .body("user.role"     , Matchers.equalTo(Role.SUPERVISOR.name()));

        verify(userService, times(1)).getAuthenticatedUser();
    }

    @Test
    void noCurrentUser() throws Http403ForbiddenException, IOException {

        doReturn(Optional.ofNullable(null)).when(userService).getAuthenticatedUser();

        given()
            .when()
            .get("/api/user/current")
            .then()
            .status(HttpStatus.OK)
            .body("authenticated", Matchers.equalTo(false))
            .body("user"         , Matchers.equalTo(null));

        verify(userService, times(1)).getAuthenticatedUser();
    }
}
