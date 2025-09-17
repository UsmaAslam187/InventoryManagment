package com.techfoot.stockspree.InboundAdaptors.REST.Product.CreateSingleProduct;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techfoot.stockspree.Business.DataContracts.CreateProducts.CreateProductsInput_IP;
import com.techfoot.stockspree.Business.DataContracts.CreateProducts.CreateProductsOutput_IP;
import com.techfoot.stockspree.InboundAdaptors.Configurations.CustomHttpRequestWrapper;
import com.techfoot.stockspree.InboundAdaptors.Configurations.SharedCustomDeserializer;
import com.techfoot.stockspree.InboundPort.Product.C_CreateProductsHandler;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class Adapter_CreateSingleProductIA {

        @Autowired
        private C_CreateProductsHandler handler;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private SharedCustomDeserializer sharedDeserializer;

        public Output_CreateSingleProductIA createSingleProduct(HttpServletRequest request) throws IOException {
                CustomHttpRequestWrapper wrappedRequest = (CustomHttpRequestWrapper) request
                                .getAttribute("wrappedRequest");
                String requestBody = wrappedRequest != null ? wrappedRequest.getBody() : null;

                // Use shared deserializer to handle all validation errors
                SharedCustomDeserializer.DeserializationResult<Input_CreateSingleProductIA> deserializationResult = sharedDeserializer
                                .deserialize(requestBody, Input_CreateSingleProductIA.class, objectMapper);

                // Check if deserialization had errors
                if (deserializationResult.hasErrors() || !deserializationResult.getValidationErrors().isEmpty()) {
                        return new Output_CreateSingleProductIA(false, "Input validation failed",
                                        deserializationResult.getValidationErrors());
                }

                Input_CreateSingleProductIA input = deserializationResult.getResult();
                System.out.println("input " + input);
                CreateProductsInput_IP inputIP = new CreateProductsInput_IP();
                inputIP.setProducts(input.getProducts().stream()
                                .map(product -> new CreateProductsInput_IP.Product(product.getName(), product.getCode(),
                                                product.getPrice(), product.getTax(), product.getType(),
                                                product.getSalesAccount(),
                                                product.getPurchaseAccount()))
                                .collect(Collectors.toList()));
                CreateProductsOutput_IP outputIP = handler.createProducts(inputIP);
                return new Output_CreateSingleProductIA(outputIP.getSuccess(), outputIP.getMessage(),
                                outputIP.getErrors());
        }
}
