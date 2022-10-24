package fr.gvs.base.controller.user;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.List;

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
import fr.gvs.base.exception.Http401UnauthorizedException;
import fr.gvs.base.exception.Http403ForbiddenException;
import fr.gvs.base.exception.Http500InternalServerErrorException;
import fr.gvs.base.model.User;
import fr.gvs.base.security.Role;
import fr.gvs.base.service.UserService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

@EnableWebMvc
@WebAppConfiguration
@SpringBootTest
public class ListUserControllerTests {
    @Mock
    UserService userService;

    @Autowired
    @InjectMocks
    ListUserController controller;

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

    List<User> getUserList() {
        User user0 = new User();
        user0.setId(3l);
        user0.setEmail("user0@gvs.fr");
        user0.setFirstname("firstname0");
        user0.setLastname("lastname0");
        user0.setRole(Role.SUPERADMIN);
        user0.setEnabled(true);

        User user1 = new User();
        user1.setId(4l);
        user1.setEmail("user1@gvs.fr");
        user1.setFirstname("firstname1");
        user1.setLastname("lastname1");
        user1.setRole(Role.SUPERVISOR);
        user1.setEnabled(true);

        User user2 = new User();
        user2.setId(5l);
        user2.setEmail("user2@gvs.fr");
        user2.setFirstname("firstname2");
        user2.setLastname("lastname2");
        user2.setRole(Role.USER);
        user2.setEnabled(false);

        return List.of(user0, user1, user2);
    }

    @Test
    void normal() throws Http403ForbiddenException, Http500InternalServerErrorException, IOException, Http401UnauthorizedException {
        List<User> list = getUserList();

        doReturn(list).when(userService).listAllUser();

        given()
            .when()
            .get("/api/users")
            .then()
            .status(HttpStatus.OK)
            .body("[0].id",         Matchers.equalTo(3))
            .body("[0].email",      Matchers.equalTo("user0@gvs.fr"))
            .body("[0].firstname",  Matchers.equalTo("firstname0"))
            .body("[0].lastname",   Matchers.equalTo("lastname0"))
            .body("[0].enabled",    Matchers.equalTo(true))
            .body("[0].role",       Matchers.equalTo(Role.SUPERADMIN.name()))
            .body("[1].id",         Matchers.equalTo(4))
            .body("[1].email",      Matchers.equalTo("user1@gvs.fr"))
            .body("[1].firstname",  Matchers.equalTo("firstname1"))
            .body("[1].lastname",   Matchers.equalTo("lastname1"))
            .body("[1].enabled",    Matchers.equalTo(true))
            .body("[1].role",       Matchers.equalTo(Role.SUPERVISOR.name()))
            .body("[2].id",         Matchers.equalTo(5))
            .body("[2].email",      Matchers.equalTo("user2@gvs.fr"))
            .body("[2].firstname",  Matchers.equalTo("firstname2"))
            .body("[2].lastname",   Matchers.equalTo("lastname2"))
            .body("[2].enabled",    Matchers.equalTo(false))
            .body("[2].role",       Matchers.equalTo(Role.USER.name()))
            .body("[3]",            Matchers.nullValue());

        verify(userService, times(1)).assertAuthenticated();
        verify(userService, times(1)).listAllUser();
    }
}
