package fr.gvs.base.exception;

public class Http409ConflictException extends Exception {

    public Http409ConflictException() {
    }

    public Http409ConflictException(String msg) {
        super(msg);
    }
}
