package fr.gvs.base.controller.user;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

@Api(tags = {"User"})
@RestController
public class DeleteUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;

    @ApiOperation(value = "Suppression d'un compte utilisateur")
    @DeleteMapping(path = "/api/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    void delete(@PathVariable Long id) throws Http400BadRequestException, Http403ForbiddenException, Http500InternalServerErrorException, IOException, Http401UnauthorizedException, Http409ConflictException {

        userService.assertAuthenticated();
        userService.assertRole(Role.SUPERADMIN);

        User user = userService.findUserById(id).orElseThrow(() -> new Http400BadRequestException("L'utilisateur à supprimer n'a pas été trouvé."));

        if(user.getEnabled() && user.getRole() == Role.SUPERADMIN) {
            if(userService.countUserWithRole(Role.SUPERADMIN) < 2) {
                throw new Http409ConflictException("Il doit toujours rester au moins un Super Administrateur.");
            }
        }
        
        userService.deleteUser(user);

        logService.log("Suppression d'un utilisateur", "L'utilisateur " + user.getFirstname() + " " + user.getLastname() + " a été supprimé");
    }
}
