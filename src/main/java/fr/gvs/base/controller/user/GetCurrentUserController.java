package fr.gvs.base.controller.user;

import fr.gvs.base.controller.user.model.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.gvs.base.model.User;
import fr.gvs.base.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Api(tags = {"User"})
@RestController
public class GetCurrentUserController {
    
    @Autowired
    private UserService userService;

    @ApiOperation("Récupère l'utilisateur couramment authentifié")
    @GetMapping("/api/user/current")
    @ResponseStatus(HttpStatus.OK)
    CurrentUserResponse get() {
        return userService.getAuthenticatedUser()
            .map(CurrentUserResponse::new)
            .orElse(new CurrentUserResponse());
    }

    @Data
    private static class CurrentUserResponse {

        CurrentUserResponse(User user) {
            this.user = new UserResponse(user);
            this.authenticated = true;
        }

        CurrentUserResponse() {
            user = null;
            authenticated = false;
        }

        UserResponse user;
        Boolean authenticated;
    }
}