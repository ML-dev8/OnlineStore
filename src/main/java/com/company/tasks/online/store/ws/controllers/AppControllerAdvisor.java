package com.company.tasks.online.store.ws.controllers;

import com.company.tasks.online.store.ws.dto.ErrorResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

@Log4j2
@ControllerAdvice
public class AppControllerAdvisor {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(HttpServletRequest req, MethodArgumentNotValidException ex) {
        log.trace("RequestUrl: {}, raised exception: {}", req.getRequestURL(), ex);
//        List<String> errormgs = ex.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.toList());
        //return the first error
        String errorMsg = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(new ErrorResponse(errorMsg));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleValidationExceptions1(HttpServletRequest req, ConstraintViolationException ex) {
        log.trace("RequestUrl: {}, raised exception: {}", req.getRequestURL(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(new ErrorResponse(ex.getMessage().split(":")[1]));
    }
}
