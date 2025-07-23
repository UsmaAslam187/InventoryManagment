package com.techfoot.stockspree.InboundAdaptors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import jakarta.validation.ConstraintViolationException;

public class GlobalExceptionHandling {
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<OutputResponseModel> handleConstraintViolationException(ConstraintViolationException error) {

        OutputResponseModel output = new OutputResponseModel();   

        output.setSuccess(false);
        output.setMessage("Error");
        output.setStatusCode(400);
        output.setError(error);

        return ResponseEntity.ok(output);
    }
}
