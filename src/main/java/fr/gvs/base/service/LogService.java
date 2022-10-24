package fr.gvs.base.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.gvs.base.exception.Http500InternalServerErrorException;
import fr.gvs.base.model.Log;
import fr.gvs.base.model.User;
import fr.gvs.base.repository.LogRepository;

@Service
public class LogService {
    @Autowired
    private LogRepository logRepository;

    @Autowired
    private UserService userService;

    public List<Log> listAllLog() {
        return logRepository.findAll();
    }

    public List<Log> listAllLogSince(int days) {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_MONTH, -days);
        return logRepository.findAllByDateAfter(calendar.getTime());
    }

    public void log(User user, String action, String comment, String userAgent, String remoteIp) throws IOException {
        Log log = new Log();
        log.setDate(Calendar.getInstance().getTime());
        log.setUser(user);
        log.setAction(action);
        log.setComment(comment);
        log.setPublicIp(remoteIp);
        log.setUserAgent(userAgent);

        logRepository.saveAndFlush(log);
    }

    public void log(String action, String comment) throws Http500InternalServerErrorException, IOException {
        User user = userService.getAuthenticatedUser().orElseThrow(() -> new Http500InternalServerErrorException("Impossible de récupérer l'utilisateur authentifié."));
        log(user, action, comment, null, null);
    }
}
