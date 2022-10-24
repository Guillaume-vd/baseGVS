package fr.gvs.base.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import fr.gvs.base.controller.user.model.UserResponse;
import fr.gvs.base.exception.Http401UnauthorizedException;
import fr.gvs.base.service.LogService;
import fr.gvs.base.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.gvs.base.exception.Http500InternalServerErrorException;
import fr.gvs.base.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.net.UnknownHostException;

@Api(tags = {"User", "Security"})
@RestController
public class LoginController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;

    @ApiOperation(value = "Connexion d'un utilisateur")
    @PostMapping(path = "/api/login")
    @ResponseStatus(HttpStatus.OK)
    LoginResponse login(HttpServletRequest httpRequest, @Valid @RequestBody LoginQuery query) throws ServletException, Http401UnauthorizedException, Http500InternalServerErrorException, UnknownHostException {
        if(userService.isAuthenticated()) httpRequest.logout();

        try {
            httpRequest.login(query.email, query.password);
        } catch(ServletException e) {
            if(e.getCause() instanceof BadCredentialsException) 
                throw new Http401UnauthorizedException("Combinaison email/mot de passe invalide.");
            if(e.getCause() instanceof DisabledException) 
                throw new Http401UnauthorizedException("Ce compte a été désactivé.");
            throw e;
        }

        User user = userService.getAuthenticatedUser().orElseThrow(() -> new Http500InternalServerErrorException("Impossible de récupérer l'utilisateur authentifié."));

        try {
            logService.log(user, "Connection", null, httpRequest.getHeader("USER-AGENT"), httpRequest.getRemoteAddr());
        } catch (Exception e){
            e.printStackTrace();
        }

        return new LoginResponse(user);
    }

    @Data
    private static class LoginQuery {

        @NotBlank
        @Email
        String email;

        @NotBlank
        String password;
    }

    @Data
    private static class LoginResponse {

        LoginResponse(User user) {
            this.user = new UserResponse(user);
            this.authenticated = true;
        }

        UserResponse user;
        Boolean authenticated;
    }
}
