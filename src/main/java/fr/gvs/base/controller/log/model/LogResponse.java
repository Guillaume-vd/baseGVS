package fr.gvs.base.controller.log.model;

import java.util.Date;

import fr.gvs.base.model.Log;
import lombok.Data;

@Data
public class LogResponse {
    public LogResponse(Log log){
        id = log.getId();
        date = log.getDate();
        user_id = log.getUser().getId();
        action = log.getAction();
        comment = log.getComment();
        publicIp = log.getPublicIp();
        userAgent = log.getUserAgent();
    }

    Long id;
    Date date;
    Long user_id;
    String action;
    String comment;
    String publicIp;
    String userAgent;
}
