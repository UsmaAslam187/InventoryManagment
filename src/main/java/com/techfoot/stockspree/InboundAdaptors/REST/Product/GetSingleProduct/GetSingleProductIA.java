package com.techfoot.stockspree.InboundAdaptors.REST.Product.GetSingleProduct;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techfoot.stockspree.Business.DataContracts.GetSingleProduct.GetSingleProductInput_IP;
import com.techfoot.stockspree.Business.DataContracts.GetSingleProduct.GetSingleProductOutput_IP;
import com.techfoot.stockspree.InboundAdaptors.Configurations.CustomHttpRequestWrapper;
import com.techfoot.stockspree.InboundAdaptors.Configurations.RequestContext;
import com.techfoot.stockspree.InboundAdaptors.Configurations.SharedCustomDeserializer;
import com.techfoot.stockspree.InboundPort.Product.Q_GetSingleProductHandler;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class GetSingleProductIA {

    @Autowired
    private Q_GetSingleProductHandler handler;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SharedCustomDeserializer sharedDeserializer;

    public Output_GetSingleProductIA getSingleProduct(HttpServletRequest request) {
        String action = RequestContext.getCurrentAction();
        if (!"Retrieve Product".equals(action)) {
            return null;
        }
        CustomHttpRequestWrapper wrappedRequest = (CustomHttpRequestWrapper) request.getAttribute("wrappedRequest");
        String requestBody = wrappedRequest != null ? wrappedRequest.getBody() : null;

        SharedCustomDeserializer.DeserializationResult<Input_GetSingleProductIA> deserializationResult = sharedDeserializer
                .deserialize(requestBody, Input_GetSingleProductIA.class, objectMapper);
        if (deserializationResult.hasErrors() || !deserializationResult.getValidationErrors().isEmpty()) {
            return new Output_GetSingleProductIA(false, "Input validation failed",
                    deserializationResult.getValidationErrors(), null);
        }
        Input_GetSingleProductIA input = deserializationResult.getResult();

        GetSingleProductInput_IP inputIP = new GetSingleProductInput_IP();
        if (input.getCode() != null) {
            inputIP.setCode(input.getCode());
        } else {
            return new Output_GetSingleProductIA(false, "Input validation failed",
                    Arrays.asList("Code is required"), null);
        }
        GetSingleProductOutput_IP outputIP = handler.getProduct(inputIP);

        if (outputIP.getProduct() != null) {
            Output_GetSingleProductIA.Product product = new Output_GetSingleProductIA.Product();
            product.setCode(outputIP.getProduct().getCode());
            product.setName(outputIP.getProduct().getName());
            product.setPrice(outputIP.getProduct().getPrice());
            product.setSalesAccount(outputIP.getProduct().getSalesAccount());
            product.setPurchaseAccount(outputIP.getProduct().getPurchaseAccount());

            return new Output_GetSingleProductIA(true, outputIP.getMessage(),
                    outputIP.getErrors(), product);
        }

        return new Output_GetSingleProductIA(outputIP.getSuccess(), outputIP.getMessage(),
                outputIP.getErrors(), null);
    }
}