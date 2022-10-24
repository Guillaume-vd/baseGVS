package fr.gvs.base.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import fr.gvs.base.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Api(tags = {"User", "Security"})
@RestController
public class LogoutController {
    
    @Autowired
    private UserService userService;

    @ApiOperation(value = "Déconnexion d'un utilisateur")
    @PostMapping(path = "/api/logout")
    @ResponseStatus(HttpStatus.OK)
    LogoutResponse logout(HttpServletRequest httpRequest) throws ServletException {

        if(!userService.isAuthenticated()) return new LogoutResponse();

        httpRequest.logout();

        return new LogoutResponse();
    }

    @Data
    private static class LogoutResponse {

        Boolean authenticated = false;
    }
}
