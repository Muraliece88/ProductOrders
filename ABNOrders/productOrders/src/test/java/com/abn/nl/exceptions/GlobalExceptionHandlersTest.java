package com.abn.nl.exceptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.nio.file.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class GlobalHandlersTest {
    @InjectMocks
    private GlobalExceptionHandlers handlers;

    @Test
    void handleProdSerachEx() {
        ProductNotFoundException exe= Mockito.mock(ProductNotFoundException.class);
        ResponseEntity<Errors> rexResult=handlers.handleprodutException(exe);
        assertEquals(rexResult.getStatusCode().value(),500);
    }

    @Test
    void handleMethodAugumentInvalid() {
        MissingServletRequestParameterException exe= Mockito.mock(MissingServletRequestParameterException.class);
        ResponseEntity<Errors> rexResult=handlers.handleMethodAugumentInvalid(exe);
        assertEquals(rexResult.getStatusCode().value(),400);
    }

    @Test
    void handleBadrequest() {
        HandlerMethodValidationException exe= Mockito.mock(HandlerMethodValidationException.class);
        Object [] test=new Object[1];
        Mockito.when(exe.getDetailMessageArguments()).thenReturn(test);
        ResponseEntity<Errors> rexResult=handlers.handleBadrequest(exe);
        assertEquals(rexResult.getStatusCode().value(),400);
    }

    @Test
    void testHandleMethodAugumentInvalid() {
        MethodArgumentNotValidException exe= Mockito.mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult= Mockito.mock(BindingResult.class);
        FieldError fieldError= Mockito.mock(FieldError.class);
        Mockito.when( exe.getBindingResult()).thenReturn(bindingResult);
        Mockito.when( bindingResult.getFieldError()).thenReturn(fieldError);
        ResponseEntity<Errors> rexResult=handlers.handleMethodAugumentInvalid(exe);
        assertEquals(rexResult.getStatusCode().value(),400);
    }


    @Test
    void handleOtherException() {
        Exception exe= Mockito.mock(Exception.class);
        ResponseEntity<Errors> rexResult=handlers.handleOtherException(exe);
        assertEquals(rexResult.getStatusCode().value(),500);
    }
}
