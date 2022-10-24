package fr.gvs.base.controller.user;

import java.util.List;
import java.util.stream.Collectors;

import fr.gvs.base.controller.user.model.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.gvs.base.exception.Http401UnauthorizedException;
import fr.gvs.base.exception.Http403ForbiddenException;
import fr.gvs.base.security.Role;
import fr.gvs.base.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = {"User"})
@RestController
public class ListUserController {
    @Autowired
    private UserService userService;

    @ApiOperation("Récupération de tous les utilisateurs")
    @GetMapping("/api/users")
    @ResponseStatus(HttpStatus.OK)
    List<UserResponse> list() throws Http403ForbiddenException, Http401UnauthorizedException {

        userService.assertAuthenticated();
        userService.assertRole(Role.SUPERADMIN);

        return userService.listAllUser().stream()
            .map(UserResponse::new)
            .collect(Collectors.toList());
    }
}
