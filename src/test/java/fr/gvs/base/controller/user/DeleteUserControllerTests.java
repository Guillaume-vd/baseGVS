package fr.gvs.base.controller.user;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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
import io.restassured.module.mockmvc.RestAssuredMockMvc;

@EnableWebMvc
@WebAppConfiguration
@SpringBootTest
public class DeleteUserControllerTests {
    @Mock
    UserService userService;

    @Mock
    LogService logService;

    @Autowired
    @InjectMocks
    DeleteUserController controller;

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
        user.setId(21l);
        user.setRole(Role.USER);
        user.setEnabled(true);

        doReturn(Optional.of(user)).when(userService).findUserById(eq(21l));

        given()
            .when()
            .delete("/api/user/21")
            .then()
            .status(HttpStatus.OK);

        verify(userService, times(1)).assertAuthenticated();
        verify(userService, times(1)).findUserById(anyLong());
        verify(userService, times(0)).countUserWithRole(any(Role.class));
        verify(userService, times(1)).deleteUser(eq(user));
        verify(logService, times(1)).log(anyString(), anyString());
    }

    @Test
    void normalSUPERADMIN() throws Http403ForbiddenException, Http500InternalServerErrorException, IOException, Http401UnauthorizedException {
        User user = new User();
        user.setId(21l);
        user.setRole(Role.SUPERADMIN);
        user.setEnabled(true);

        doReturn(Optional.of(user)).when(userService).findUserById(eq(21l));
        doReturn(2l).when(userService).countUserWithRole(eq(Role.SUPERADMIN));

        given()
            .when()
            .delete("/api/user/21")
            .then()
            .status(HttpStatus.OK);

        verify(userService, times(1)).assertAuthenticated();
        verify(userService, times(1)).findUserById(anyLong());
        verify(userService, times(1)).countUserWithRole(any(Role.class));
        verify(userService, times(1)).deleteUser(eq(user));
        verify(logService, times(1)).log(anyString(), anyString());
    }

    @Test
    void userNotFound() throws Http403ForbiddenException, Http500InternalServerErrorException, IOException, Http401UnauthorizedException {
        
        doReturn(Optional.ofNullable(null)).when(userService).findUserById(eq(21l));

        given()
            .when()
            .delete("/api/user/21")
            .then()
            .status(HttpStatus.BAD_REQUEST);

        verify(userService, times(1)).assertAuthenticated();
        verify(userService, times(1)).findUserById(anyLong());
        verify(userService, times(0)).countUserWithRole(any(Role.class));
        verify(userService, times(0)).deleteUser(any(User.class));
        verify(logService, times(0)).log(anyString(), anyString());
    }
}
