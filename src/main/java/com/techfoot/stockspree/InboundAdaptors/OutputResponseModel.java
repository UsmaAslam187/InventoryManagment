package com.techfoot.stockspree.InboundAdaptors;

import jakarta.validation.ConstraintViolationException;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutputResponseModel {
    private Boolean success;
    private String message;
    private Integer statusCode;
    private ConstraintViolationException error;
}
