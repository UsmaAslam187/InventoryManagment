package com.techfoot.stockspree.InboundAdaptors.REST.Product.C_CreateProduct;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techfoot.stockspree.Business.DataContracts.CreateProduct.CreateProductInput_IP;
import com.techfoot.stockspree.Business.DataContracts.CreateProduct.CreateProductOutput_IP;
import com.techfoot.stockspree.InboundAdaptors.Configurations.CustomHttpRequestWrapper;
import com.techfoot.stockspree.InboundAdaptors.Configurations.SharedCustomDeserializer;
import com.techfoot.stockspree.InboundPort.Product.C_CreateProductHandler;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/billspree/products/create-product")
public class Adapter_CreateProductIA {
    
    @Autowired
    private C_CreateProductHandler handler;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private SharedCustomDeserializer sharedDeserializer;

    @PostMapping
    public Output_CreateProductIA createProduct(HttpServletRequest request) throws IOException {
        CustomHttpRequestWrapper wrappedRequest = (CustomHttpRequestWrapper) request.getAttribute("wrappedRequest");
        String requestBody = wrappedRequest != null ? wrappedRequest.getBody() : null;

        // Use shared deserializer to handle all validation errors
        SharedCustomDeserializer.DeserializationResult<Input_CreateProductIA> deserializationResult = 
            sharedDeserializer.deserialize(requestBody, Input_CreateProductIA.class, objectMapper);
        
        // Check if deserialization had errors
        if (deserializationResult.hasErrors() || !deserializationResult.getValidationErrors().isEmpty()) {
            return new Output_CreateProductIA(false, "Input validation failed", deserializationResult.getValidationErrors());
        }
        
        Input_CreateProductIA input = deserializationResult.getResult();
        
        // // Additional validation if needed
        // List<String> validationErrors = input.validate();
        // if (!validationErrors.isEmpty()) {
        //     return new Output_CreateProductIA(false, "Product validation failed", validationErrors);
        // }

        System.out.println("input " + input);
        CreateProductInput_IP inputIP = new CreateProductInput_IP();
        inputIP.setProducts(input.getProducts().stream().map(product -> new CreateProductInput_IP.Product(product.getName(), product.getCode(), product.getPrice(), product.getTax(), product.getType(), product.getSalesAccount(), product.getPurchaseAccount())).collect(Collectors.toList()));
        CreateProductOutput_IP outputIP = handler.createProduct(inputIP);
        return new Output_CreateProductIA(outputIP.getSuccess(), outputIP.getMessage(), outputIP.getErrors());
    }
}
