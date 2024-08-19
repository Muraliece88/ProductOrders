package com.abn.nl.exceptions;

import jakarta.validation.ConstraintDefinitionException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(basePackages = "com.assign.abn")
public class GlobalExceptionHandlers {
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Errors> handleorderException(OrderNotFoundException ex) {
        log.error("exception occured{}", ex.getMessage());
        Errors errors=new Errors(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errors);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Errors> handleConstraintException(ConstraintViolationException ex) {
        log.error("exception occured{}", ex.getMessage());
        Errors errors=new Errors(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(errors);
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Errors> handleMethodAugumentInvalid(MissingServletRequestParameterException ex) {
        log.error("exception occured{}", ex.getMessage());
        Errors errors=new Errors(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(errors);
    }
    @ExceptionHandler(ConstraintDefinitionException.class)
    public ResponseEntity<Errors> handleBadrequest(ConstraintDefinitionException ex) {
        log.error("exception occured"+ex.getMessage());
        Errors errors=new Errors(HttpStatus.BAD_REQUEST.value(), ex.getMessage(),LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(errors);

    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Errors> handleMethodAugumentInvalid(MethodArgumentNotValidException ex) {
        log.error("exception occured"+ex.getMessage());
        Errors errors=new Errors(HttpStatus.BAD_REQUEST.value(), ex.getBindingResult().getFieldError().getDefaultMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(errors);
    }

   @ExceptionHandler(Exception.class)
    public ResponseEntity<Errors> handleOtherException(Exception ex) {

        log.error("exception occured"+ex.getMessage());
        Errors errors=new Errors(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Technical issues when operation is performed", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errors);


    }

}
