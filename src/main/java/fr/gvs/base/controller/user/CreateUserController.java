package fr.gvs.base.controller.user;

import java.io.IOException;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.gvs.base.exception.Http400BadRequestException;
import fr.gvs.base.exception.Http401UnauthorizedException;
import fr.gvs.base.exception.Http403ForbiddenException;
import fr.gvs.base.exception.Http500InternalServerErrorException;
import fr.gvs.base.model.User;
import fr.gvs.base.security.Role;
import fr.gvs.base.service.LogService;
import fr.gvs.base.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Api(tags = {"User"})
@RestController
public class CreateUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;

    @ApiOperation(value = "Création d'un compte utilisateur")
    @PostMapping(path = "/api/user")
    @ResponseStatus(HttpStatus.CREATED)
    RegisterResponse create(@Valid @RequestBody RegisterQuery query) throws Http400BadRequestException, Http403ForbiddenException, Http500InternalServerErrorException, IOException, Http401UnauthorizedException {

        userService.assertAuthenticated();
        userService.assertRole(Role.SUPERADMIN);

        if(!query.password.equals(query.matching_password)) {
            throw new Http400BadRequestException("Les deux champs mot de passe ne sont pas identiques.");
        }

        User user = new User();
        user.setEmail(query.email);
        user.setFirstname(query.firstname);
        user.setLastname(query.lastname);
        user.setRole(query.role);

        user = userService.register(user, query.password);

        logService.log("Ajout d'un utilisateur", "L'utilisateur " + user.getFirstname() + " " + user.getLastname() + " a été ajouté");

        return new RegisterResponse(user);
    }

    @Data
    private static class RegisterQuery {

        String firstname;
        String lastname;

        @NotNull
        Role role;

        @NotBlank
        @Email
        String email;

        @NotBlank
        @Size(min = 12)
        String password;

        @NotBlank
        @Size(min = 12)
        String matching_password;
    }

    @Data
    private static class RegisterResponse {

        RegisterResponse(User user) {
            id = user.getId();
        }

        Long id;
    }
}
