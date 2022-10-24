package fr.gvs.base.exception;

public class Http401UnauthorizedException extends Exception {
    
    public Http401UnauthorizedException() {
    }

    public Http401UnauthorizedException(String msg) {
        super(msg);
    }
}
