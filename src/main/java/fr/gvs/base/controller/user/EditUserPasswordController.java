package fr.gvs.base.controller.user;

import java.io.IOException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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
public class EditUserPasswordController {

    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;

    @ApiOperation(value = "Modification d'un mot de passe utilisateur")
    @PutMapping(path = "/api/user/{id}/password")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void edit(@PathVariable Long id, @Valid @RequestBody ChangePasswordQuery query) throws Http400BadRequestException, Http403ForbiddenException, Http500InternalServerErrorException, IOException, Http401UnauthorizedException {

        userService.assertAuthenticated();
        userService.assertRole(Role.SUPERADMIN);

        if(!query.password.equals(query.matching_password)) {
            throw new Http400BadRequestException("Les deux champs mot de passe ne sont pas identiques.");
        }

        User user = userService.findUserById(id).orElseThrow(() -> new Http400BadRequestException("L'utilisateur à modifier n'a pas été trouvé."));

        userService.changePassword(user, query.password);

        logService.log("Modification du mot de passe d'un utilisateur", "Le mot de passe de " + user.getFirstname() + " " + user.getLastname() + " a été modifié");
    }

    @Data
    private static class ChangePasswordQuery {

        @NotBlank
        @Size(min = 12)
        String password;

        @NotBlank
        @Size(min = 12)
        String matching_password;
    }
}
