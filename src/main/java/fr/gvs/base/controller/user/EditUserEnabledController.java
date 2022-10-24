package fr.gvs.base.controller.user;

import java.io.IOException;

import javax.validation.Valid;

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
import fr.gvs.base.exception.Http409ConflictException;
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
public class EditUserEnabledController {
    @Autowired
    private UserService userService;
    @Autowired
    private LogService logService;

    @ApiOperation(value = "Suppression d'un compte utilisateur")
    @PutMapping(path = "/api/user/{id}/enabled")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void enabled(@PathVariable Long id, @Valid @RequestBody EnabledQuery query) throws Http400BadRequestException, Http403ForbiddenException, Http500InternalServerErrorException, IOException, Http401UnauthorizedException, Http409ConflictException {

        userService.assertAuthenticated();
        userService.assertRole(Role.SUPERADMIN);

        User user = userService.findUserById(id).orElseThrow(() -> new Http400BadRequestException("L'utilisateur à modifier n'a pas été trouvé."));

        if(user.getEnabled() && user.getRole() == Role.SUPERADMIN) {
            if(userService.countUserWithRole(Role.SUPERADMIN) < 2) {
                throw new Http409ConflictException("Il doit toujours rester au moins un Super Administrateur.");
            }
        }

        user.setEnabled(query.enabled);
        userService.modifyUser(user);

        if (user.getEnabled()){
            logService.log("Activation d'un utilisateur", "L'utilisateur " + user.getFirstname() + " " + user.getLastname() + " a été activé");
        } else {
            logService.log("Désactivation d'un utilisateur", "L'utilisateur " + user.getFirstname() + " " + user.getLastname() + " a été désactivé");
        }
    }

    @Data
    private static class EnabledQuery {
        Boolean enabled;
    }
}
