package fr.gvs.base.exception;

public class Http403ForbiddenException extends Exception {
    
    public Http403ForbiddenException() {
    }

    public Http403ForbiddenException(String msg) {
        super(msg);
    }
}
