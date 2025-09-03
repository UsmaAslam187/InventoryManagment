package com.techfoot.stockspree.InboundAdaptors.REST.Product.GetAllProducts;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techfoot.stockspree.Business.DataContracts.GetAllProducts.Input_IP;
import com.techfoot.stockspree.Business.DataContracts.GetAllProducts.Output_IP;
import com.techfoot.stockspree.InboundAdaptors.Configurations.CustomHttpRequestWrapper;
import com.techfoot.stockspree.InboundAdaptors.Configurations.RequestContext;
import com.techfoot.stockspree.InboundAdaptors.Configurations.SharedCustomDeserializer;
import com.techfoot.stockspree.InboundPort.Product.Q_GetAllProductsHandler;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class GetAllProductsIA {
    
    @Autowired
    private Q_GetAllProductsHandler handler;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private SharedCustomDeserializer sharedDeserializer;
    
    public Output_GetAllProductsIA getAllProducts(HttpServletRequest request) {
        
        String action = RequestContext.getCurrentAction();
        if (!"Retrieve Products".equals(action)) {
            return null; 
        }
        
        CustomHttpRequestWrapper wrappedRequest = (CustomHttpRequestWrapper) request.getAttribute("wrappedRequest");
        String requestBody = wrappedRequest != null ? wrappedRequest.getBody() : null;

        SharedCustomDeserializer.DeserializationResult<Input_GetAllProductsIA> deserializationResult = 
            sharedDeserializer.deserialize(requestBody, Input_GetAllProductsIA.class, objectMapper);
        
        if (deserializationResult.hasErrors() || !deserializationResult.getValidationErrors().isEmpty()) {
            return new Output_GetAllProductsIA(false, "Input validation failed", deserializationResult.getValidationErrors(), null);
        }
        
        Input_GetAllProductsIA input = deserializationResult.getResult();
        Input_IP inputIP = new Input_IP();
        inputIP.setSearchedValue(input.getSearchedValue());
        inputIP.setPage(new Input_IP.Page(input.getPage().getSize(), input.getPage().getTotalElements(), input.getPage().getTotalPages(), input.getPage().getPageNumber(), input.getPage().getSearchedValue(), input.getPage().getCsvExport()));
        Output_IP outputIP = handler.getAllProducts(inputIP);
        
        List<Output_GetAllProductsIA.Product> products = new ArrayList<>();
        if (outputIP.getProducts() != null) {
            for (Output_IP.Product ipProduct : outputIP.getProducts()) {
                Output_GetAllProductsIA.Product product = new Output_GetAllProductsIA.Product();
                product.setCode(ipProduct.getCode());
                product.setName(ipProduct.getName());
                product.setPrice(ipProduct.getPrice());
                product.setSalesAccount(ipProduct.getSalesAccount());
                product.setPurchaseAccount(ipProduct.getPurchaseAccount());
                products.add(product);
            }
        }
        
        return new Output_GetAllProductsIA(outputIP.getSuccess(), outputIP.getMessage(), 
            outputIP.getErrors(), products);
    }
} 