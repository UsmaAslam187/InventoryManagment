package com.techfoot.stockspree.InboundAdaptors.REST.Product.C_CreateProduct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.techfoot.stockspree.InboundAdaptors.Configurations.CustomHttpRequestWrapper;
import com.techfoot.stockspree.InboundPort.Product.C_CreateProductHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@RestController
@RequestMapping("/billspree/products/create-product")
public class Adapter_CreateProductIA {
    
    @Autowired
    private C_CreateProductHandler handler;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private Validator validator;

    @PostMapping
    public Output_CreateProductIA createProduct(HttpServletRequest request) throws IOException {
        CustomHttpRequestWrapper wrappedRequest = (CustomHttpRequestWrapper) request.getAttribute("wrappedRequest");
        String requestBody = wrappedRequest.getBody();
        System.out.println("BODY -->" + requestBody);

        // Option 1: Use the static validateRequest method
        List<String> validationErrors = Input_CreateProductIA.validateRequest(requestBody);
        if (!validationErrors.isEmpty()) {
            return new Output_CreateProductIA(
                    false,
                    "Product validation failed",
                    validationErrors);
        }

        // Configure ObjectMapper to ignore unknown properties
        objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Input_CreateProductIA input = objectMapper.readValue(
                requestBody,
                Input_CreateProductIA.class);

        System.out.println("input " + input.toString());
        return handler.createProduct(input);
    }
}
