package fr.gvs.base.exception;

public class Http400BadRequestException extends Exception {

    public Http400BadRequestException() {
    }

    public Http400BadRequestException(String msg) {
        super(msg);
    }
}
