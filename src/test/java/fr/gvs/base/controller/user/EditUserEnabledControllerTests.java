package fr.gvs.base.controller.user;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.Optional;

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
import fr.gvs.base.exception.Http401UnauthorizedException;
import fr.gvs.base.exception.Http403ForbiddenException;
import fr.gvs.base.exception.Http500InternalServerErrorException;
import fr.gvs.base.model.User;
import fr.gvs.base.security.Role;
import fr.gvs.base.service.LogService;
import fr.gvs.base.service.UserService;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

@EnableWebMvc
@WebAppConfiguration
@SpringBootTest
public class EditUserEnabledControllerTests {
    @Mock
    UserService userService;

    @Mock
    LogService logService;

    @Autowired
    @InjectMocks
    EditUserEnabledController controller;

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
    void normalUSER() throws Http403ForbiddenException, Http500InternalServerErrorException, IOException, Http401UnauthorizedException {
        User user = new User();
        user.setId(5l);
        user.setRole(Role.USER);
        user.setFirstname("firstname");
        user.setLastname("lastname");
        user.setEnabled(true);

        doReturn(Optional.of(user)).when(userService).findUserById(eq(5l));
        doAnswer(args -> {
            assertFalse(args.getArgument(0, User.class).getEnabled());
            return null;
        }).when(userService).modifyUser(any(User.class));

        String query =
          "{"
        + "\"enabled\": false "
        + "}";

        given()
            .body(query)
            .contentType(ContentType.JSON)
            .when()
            .put("/api/user/5/enabled")
            .then()
            .status(HttpStatus.ACCEPTED);

        verify(userService, times(1)).assertAuthenticated();
        verify(userService, times(1)).findUserById(anyLong());
        verify(userService, times(0)).countUserWithRole(any(Role.class));
        verify(userService, times(1)).modifyUser(any(User.class));
        verify(logService, times(1)).log(anyString(), anyString());
    }

    @Test
    void normalSUPERADMIN() throws Http403ForbiddenException, Http500InternalServerErrorException, IOException, Http401UnauthorizedException {
        User user = new User();
        user.setId(5l);
        user.setRole(Role.SUPERADMIN);
        user.setFirstname("firstname");
        user.setLastname("lastname");
        user.setEnabled(true);

        doReturn(Optional.of(user)).when(userService).findUserById(eq(5l));
        doReturn(2l).when(userService).countUserWithRole(eq(Role.SUPERADMIN));
        doAnswer(args -> {
            assertFalse(args.getArgument(0, User.class).getEnabled());
            return null;
        }).when(userService).modifyUser(any(User.class));

        String query =
          "{"
        + "\"enabled\": false "
        + "}";

        given()
            .body(query)
            .contentType(ContentType.JSON)
            .when()
            .put("/api/user/5/enabled")
            .then()
            .status(HttpStatus.ACCEPTED);

        verify(userService, times(1)).assertAuthenticated();
        verify(userService, times(1)).findUserById(anyLong());
        verify(userService, times(1)).countUserWithRole(any(Role.class));
        verify(userService, times(1)).modifyUser(any(User.class));
        verify(logService, times(1)).log(anyString(), anyString());
    }

    @Test
    void userNotFound() throws Http403ForbiddenException, Http500InternalServerErrorException, IOException, Http401UnauthorizedException {

        doReturn(Optional.ofNullable(null)).when(userService).findUserById(eq(5l));

        String query =
          "{"
        + "\"enabled\": false "
        + "}";

        given()
            .body(query)
            .contentType(ContentType.JSON)
            .when()
            .put("/api/user/5/enabled")
            .then()
            .status(HttpStatus.BAD_REQUEST);

        verify(userService, times(1)).assertAuthenticated();
        verify(userService, times(1)).findUserById(anyLong());
        verify(userService, times(0)).countUserWithRole(any(Role.class));
        verify(userService, times(0)).modifyUser(any(User.class));
        verify(logService, times(0)).log(anyString(), anyString());
    }
}
