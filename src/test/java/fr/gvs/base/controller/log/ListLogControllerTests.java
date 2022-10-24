package fr.gvs.base.controller.log;


import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
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
import fr.gvs.base.model.Log;
import fr.gvs.base.model.User;
import fr.gvs.base.service.LogService;
import fr.gvs.base.service.UserService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

@EnableWebMvc
@WebAppConfiguration
@SpringBootTest
public class ListLogControllerTests {

	@Mock
    UserService userService;

	@Mock
	LogService logService;
    
    @Autowired
	@InjectMocks
	ListLogController controller;

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

    List<Log> getLogList() {

        Log log0 = new Log();
        log0.setId(3l);

        Date date0 = new GregorianCalendar(2020, 6 -1 /* /!\ Months are 0 based ! */, 10, 7, 18, 45).getTime();
        log0.setDate(date0);

        User user0 = new User();
        user0.setId(7l);
        log0.setUser(user0);

        log0.setAction("action");
        log0.setComment("comment");
        log0.setPublicIp("45.8.7.1");
        log0.setUserAgent("userAgent");

        Log log1 = new Log();
        log1.setId(6l);

        Date date1 = new GregorianCalendar(2021, 1 -1 /* /!\ Months are 0 based ! */, 6, 15, 23, 54).getTime();
        log1.setDate(date1);

        User user1 = new User();
        user1.setId(12l);
        log1.setUser(user1);

        log1.setAction("action_1");
        log1.setComment("1_comment");
        log1.setPublicIp("27.45.35.128");
        log1.setUserAgent("user_1_Agent");

        return List.of(log0, log1);
    }

	@Test
	void normalAll() throws Http403ForbiddenException, Http500InternalServerErrorException, IOException, Http401UnauthorizedException {
		
		List<Log> list = getLogList();

		doReturn(list).when(logService).listAllLog();

		given()
			.when()
			.get("/api/logs")
			.then()
			.status(HttpStatus.OK)
            .body("[0].id",        Matchers.equalTo(3))
            .body("[0].date",      Matchers.equalTo(list.get(0).getDate().getTime()))
            .body("[0].user_id",   Matchers.equalTo(7))
            .body("[0].action",    Matchers.equalTo("action"))
            .body("[0].comment",   Matchers.equalTo("comment"))
            .body("[0].publicIp",  Matchers.equalTo("45.8.7.1"))
            .body("[0].userAgent", Matchers.equalTo("userAgent"))
            .body("[1].id",        Matchers.equalTo(6))
            .body("[1].date",      Matchers.equalTo(list.get(1).getDate().getTime()))
            .body("[1].user_id",   Matchers.equalTo(12))
            .body("[1].action",    Matchers.equalTo("action_1"))
            .body("[1].comment",   Matchers.equalTo("1_comment"))
            .body("[1].publicIp",  Matchers.equalTo("27.45.35.128"))
            .body("[1].userAgent", Matchers.equalTo("user_1_Agent"))
            .body("[2]", Matchers.nullValue());

		verify(userService, times(1)).assertAuthenticated();
		verify(logService, times(1)).listAllLog();
	}

	@Test
	void normalSince0() throws Http403ForbiddenException, Http500InternalServerErrorException, IOException, Http401UnauthorizedException {
		
		List<Log> list = getLogList();

		doReturn(list).when(logService).listAllLog();

		given()
			.when()
			.get("/api/logs?since=0")
			.then()
			.status(HttpStatus.OK)
            .body("[0].id",        Matchers.equalTo(3))
            .body("[0].date",      Matchers.equalTo(list.get(0).getDate().getTime()))
            .body("[0].user_id",   Matchers.equalTo(7))
            .body("[0].action",    Matchers.equalTo("action"))
            .body("[0].comment",   Matchers.equalTo("comment"))
            .body("[0].publicIp",  Matchers.equalTo("45.8.7.1"))
            .body("[0].userAgent", Matchers.equalTo("userAgent"))
            .body("[1].id",        Matchers.equalTo(6))
            .body("[1].date",      Matchers.equalTo(list.get(1).getDate().getTime()))
            .body("[1].user_id",   Matchers.equalTo(12))
            .body("[1].action",    Matchers.equalTo("action_1"))
            .body("[1].comment",   Matchers.equalTo("1_comment"))
            .body("[1].publicIp",  Matchers.equalTo("27.45.35.128"))
            .body("[1].userAgent", Matchers.equalTo("user_1_Agent"))
            .body("[2]", Matchers.nullValue());

		verify(userService, times(1)).assertAuthenticated();
		verify(logService, times(1)).listAllLog();
	}

	@Test
	void normalSince() throws Http403ForbiddenException, Http500InternalServerErrorException, IOException, Http401UnauthorizedException {
		
		List<Log> list = getLogList();

		doReturn(list).when(logService).listAllLogSince(eq(7));

		given()
			.when()
			.get("/api/logs?since=7")
			.then()
			.status(HttpStatus.OK)
            .body("[0].id",        Matchers.equalTo(3))
            .body("[0].date",      Matchers.equalTo(list.get(0).getDate().getTime()))
            .body("[0].user_id",   Matchers.equalTo(7))
            .body("[0].action",    Matchers.equalTo("action"))
            .body("[0].comment",   Matchers.equalTo("comment"))
            .body("[0].publicIp",  Matchers.equalTo("45.8.7.1"))
            .body("[0].userAgent", Matchers.equalTo("userAgent"))
            .body("[1].id",        Matchers.equalTo(6))
            .body("[1].date",      Matchers.equalTo(list.get(1).getDate().getTime()))
            .body("[1].user_id",   Matchers.equalTo(12))
            .body("[1].action",    Matchers.equalTo("action_1"))
            .body("[1].comment",   Matchers.equalTo("1_comment"))
            .body("[1].publicIp",  Matchers.equalTo("27.45.35.128"))
            .body("[1].userAgent", Matchers.equalTo("user_1_Agent"))
            .body("[2]", Matchers.nullValue());

		verify(userService, times(1)).assertAuthenticated();
		verify(logService, times(1)).listAllLogSince(anyInt());
	}
}