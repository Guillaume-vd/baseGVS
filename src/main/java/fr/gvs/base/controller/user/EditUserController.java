package fr.gvs.base.controller.user;

import java.io.IOException;
import java.util.ArrayList;

import javax.validation.Valid;
import javax.validation.constraints.Email;

import org.apache.commons.lang3.StringUtils;
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
public class EditUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;

    @ApiOperation(value = "Modification d'un compte utilisateur")
    @PutMapping(path = "/api/user/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void edit(@PathVariable Long id, @Valid @RequestBody EditQuery query) throws Http400BadRequestException, Http403ForbiddenException, Http500InternalServerErrorException, IOException, Http401UnauthorizedException, Http409ConflictException {

        userService.assertAuthenticated();
        userService.assertRole(Role.SUPERADMIN);

        User user = userService.findUserById(id).orElseThrow(() -> new Http400BadRequestException("L'utilisateur à modifier n'a pas été trouvé."));

        ArrayList<String> changes = new ArrayList<>();

        if(!StringUtils.isBlank(query.email) && !query.email.equals(user.getEmail())) {
            changes.add("Changement d'email de : " + user.getEmail() + " à " + query.email);
            user.setEmail(query.email);
        }
        if(query.role != null && query.role != user.getRole()) {
            if(user.getEnabled() && user.getRole() == Role.SUPERADMIN) {
                if(userService.countUserWithRole(Role.SUPERADMIN) < 2) {
                    throw new Http409ConflictException("Il doit toujours rester au moins un Super Administrateur.");
                }
            }
            changes.add("Changement de rôle de : " + user.getRole().name() + " à " + query.role.name());
            user.setRole(query.role);
        }
        if(!StringUtils.isBlank(query.firstname) && !query.firstname.equals(user.getFirstname())) {
            changes.add("Changement de prénom de : " + user.getFirstname() + " à " + query.firstname);
            user.setFirstname(query.firstname);
        }
        if(!StringUtils.isBlank(query.lastname) && !query.lastname.equals(user.getLastname())){
            changes.add("Changement de nom de : " + user.getLastname() + " à " + query.lastname);
            user.setLastname(query.lastname);
        }

        if(changes.size() < 1) return;

        userService.modifyUser(user);

        StringBuilder comment = new StringBuilder();
        comment.append("L'utilisateur ").append(user.getFirstname()).append(" ").append(user.getLastname()).append(" à eu ").append(changes.size()).append(" modification(s) :\n");

        for(int i = 0; i < changes.size(); i++) {
            comment.append(" - ").append(changes.get(i)).append("\n");
        }

        logService.log("Modification d'un utilisateur", comment.substring(0, comment.length()-1)); // remove the last \n char
    }

    @Data
    private static class EditQuery {
        @Email
        String email;
        Role role;
        String firstname;
        String lastname;
    }
}
