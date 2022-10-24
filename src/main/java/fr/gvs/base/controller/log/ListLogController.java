package fr.gvs.base.controller.log;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.gvs.base.controller.log.model.LogResponse;
import fr.gvs.base.exception.Http401UnauthorizedException;
import fr.gvs.base.exception.Http403ForbiddenException;
import fr.gvs.base.model.Log;
import fr.gvs.base.security.Role;
import fr.gvs.base.service.LogService;
import fr.gvs.base.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = {"Log"})
@RestController
public class ListLogController {
    @Autowired
    private LogService logService;
    @Autowired
    private UserService userService;

    @ApiOperation("Récupération de tous les utilisateurs")
    @GetMapping("/api/logs")
    @ResponseStatus(HttpStatus.OK)
    List<LogResponse> list(@RequestParam(required = false) Integer since) throws Http403ForbiddenException, Http401UnauthorizedException {

        userService.assertAuthenticated();
        userService.assertRole(Role.SUPERADMIN);

        List<Log> logs;
        
        if(since == null || since <= 0) {
            logs = logService.listAllLog();
        } else {
            logs = logService.listAllLogSince(since);
        }

        return logs.stream().map(LogResponse::new).collect(Collectors.toList());
    }
}
