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
import fr.gvs.base.service.LogService;
import fr.gvs.base.service.UserService;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

@EnableWebMvc
@WebAppConfiguration
@SpringBootTest
public class EditPasswordControllerTests {

	@Mock
    UserService userService;

	@Mock
	LogService logService;
    
    @Autowired
	@InjectMocks
	EditUserPasswordController controller;

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
	void normal() throws Http403ForbiddenException, Http500InternalServerErrorException, IOException, Http401UnauthorizedException {
		
		User user = new User();
		user.setId(5l);
		user.setFirstname("firstname");
		user.setLastname("lastname");

		doReturn(Optional.of(user)).when(userService).findUserById(eq(5l));

        String query =
          "{"
        + "\"password\": \"12letterpass\", "
        + "\"matching_password\": \"12letterpass\" "
        + "}";

		given()
			.body(query)
			.contentType(ContentType.JSON)
			.when()
			.put("/api/user/5/password")
			.then()
			.status(HttpStatus.ACCEPTED);

		verify(userService, times(1)).assertAuthenticated();
		verify(userService, times(1)).findUserById(anyLong());
		verify(userService, times(1)).changePassword(eq(user), eq("12letterpass"));
		verify(logService, times(1)).log(anyString(), anyString());
	}

	@Test
	void passwordFail() throws Http403ForbiddenException, Http500InternalServerErrorException, IOException, Http401UnauthorizedException {
		
        String query =
          "{"
        + "\"password\": \"12letterpass\", "
        + "\"matching_password\": \"12letterpasz\" "
        + "}";

		given()
			.body(query)
			.contentType(ContentType.JSON)
			.when()
			.put("/api/user/5/password")
			.then()
			.status(HttpStatus.BAD_REQUEST);

		verify(userService, times(1)).assertAuthenticated();
		verify(userService, times(0)).findUserById(anyLong());
		verify(userService, times(0)).changePassword(any(User.class), anyString());
		verify(logService, times(0)).log(anyString(), anyString());
	}

	@Test
	void passwordTooShort() throws Http403ForbiddenException, Http500InternalServerErrorException, IOException, Http401UnauthorizedException {
		
        String query =
          "{"
        + "\"password\": \"11letterpas\", "
        + "\"matching_password\": \"11letterpas\" "
        + "}";

		given()
			.body(query)
			.contentType(ContentType.JSON)
			.when()
			.put("/api/user/5/password")
			.then()
			.status(HttpStatus.BAD_REQUEST);

		verify(userService, times(0)).assertAuthenticated();
		verify(userService, times(0)).findUserById(anyLong());
		verify(userService, times(0)).changePassword(any(User.class), anyString());
		verify(logService, times(0)).log(anyString(), anyString());
	}
}
