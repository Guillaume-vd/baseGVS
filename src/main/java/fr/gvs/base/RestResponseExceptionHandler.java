package fr.gvs.base;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import fr.gvs.base.exception.Http400BadRequestException;
import fr.gvs.base.exception.Http401UnauthorizedException;
import fr.gvs.base.exception.Http403ForbiddenException;
import fr.gvs.base.exception.Http409ConflictException;
import fr.gvs.base.exception.Http500InternalServerErrorException;
import lombok.Data;

@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    //400
    // handle the exception thown when the spring validator refuse an input
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders _headers, HttpStatus _status, WebRequest request) {
        
        StringBuilder infos = new  StringBuilder();
        for(FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            infos.append("'"+fieldError.getField()+"': "+fieldError.getDefaultMessage()+", ");
        }
        infos.delete(infos.length()-2, infos.length());

        return handleExceptionInternal(
                ex,
                new ErrorResponse(400, "Bad Request", "La requête contient "+ex.getBindingResult().getFieldErrors().size()+" contraintes non respectées : ["+infos.toString()+"]", new Date(), _headers.getLocation().toString()),
                getHeaders(),
                HttpStatus.BAD_REQUEST,
                request); 
    }

    // 400 Bad Request
    @ExceptionHandler({Http400BadRequestException.class, IllegalArgumentException.class})
    protected ResponseEntity<Object> handleHttp400BadRequestException(Exception ex, HttpServletRequest request) {

        return handleExceptionInternal(
                ex,
                new ErrorResponse(400, "Bad Request", ex.getMessage(), new Date(), request.getRequestURI()),
                getHeaders(),
                HttpStatus.BAD_REQUEST,
                new ServletWebRequest(request));
    }
    
    // 401 Unauthorized
    @ExceptionHandler({Http401UnauthorizedException.class})
    protected ResponseEntity<Object> handleHttp401UnauthorizedException(Exception ex, HttpServletRequest request) {

        return handleExceptionInternal(
                ex,
                new ErrorResponse(401, "Unauthorized", ex.getMessage(), new Date(), request.getRequestURI()),
                getHeaders(),
                HttpStatus.UNAUTHORIZED,
                new ServletWebRequest(request));
    }

    // 403 Forbidden
    @ExceptionHandler({Http403ForbiddenException.class})
    protected ResponseEntity<Object> handleHttp403ForbiddenException(Exception ex, HttpServletRequest request) {

        return handleExceptionInternal(
                ex,
                new ErrorResponse(403, "Forbidden", ex.getMessage(), new Date(), request.getRequestURI()),
                getHeaders(),
                HttpStatus.FORBIDDEN,
                new ServletWebRequest(request));
    }

    // 409 Conflict
    @ExceptionHandler({Http409ConflictException.class})
    protected ResponseEntity<Object> handleHttp409ConflictException(Exception ex, HttpServletRequest request) {

        return handleExceptionInternal(
                ex,
                new ErrorResponse(409, "Conflict", ex.getMessage(), new Date(), request.getRequestURI()),
                getHeaders(),
                HttpStatus.CONFLICT,
                new ServletWebRequest(request));
    }

    // 500 Internal Server Error
    @ExceptionHandler({Http500InternalServerErrorException.class})
    protected ResponseEntity<Object> handleHttp500InternalServerErrorException(Exception ex, HttpServletRequest request) {

        return handleExceptionInternal(
                ex,
                new ErrorResponse(500, "Internal Server Error", ex.getMessage(), new Date(), request.getRequestURI()),
                getHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                new ServletWebRequest(request));
    }

    // 500 (generic)
    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> handleInternalUnknownError(Exception ex, HttpServletRequest request) {

        return handleExceptionInternal(
                ex,
                new ErrorResponse(500, "Internal Server Error", ex.getMessage(), new Date(), request.getRequestURI()),
                getHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                new ServletWebRequest(request));
    }

    private HttpHeaders getHeaders()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    @Data
    private static class ErrorResponse {

        ErrorResponse(Integer status, String error, String message, Date timestamp, String path) {
            this.status = status;
            this.error = error;
            this.message = message;
            this.timestamp = timestamp;
            this.path = path;
        }

        Integer status;
        String error;
        String message;
        Date timestamp;
        String path;
    }
}
