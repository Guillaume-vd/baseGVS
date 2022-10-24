package fr.gvs.base.exception;

public class Http500InternalServerErrorException extends Exception {

    public Http500InternalServerErrorException() {
    }

    public Http500InternalServerErrorException(String msg) {
        super(msg);
    }
}
