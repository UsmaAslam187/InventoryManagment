package com.techfoot.stockspree.InboundPort.Product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techfoot.stockspree.Business.DataContracts.CreateProducts.CreateProductsInput_IP;
import com.techfoot.stockspree.Business.DataContracts.CreateProducts.CreateProductsOutput_IP;
import com.techfoot.stockspree.Business.Domain.Product;
import com.techfoot.stockspree.Business.Store.Store;

@Service
public class C_CreateProductsHandler {

    @Autowired
    private Store store;

    public CreateProductsOutput_IP createProducts(CreateProductsInput_IP input) {

try {
        Product productEntity = new Product();
        List<String> validationErrors = new ArrayList<>();
        for (CreateProductsInput_IP.Product product : input.getProducts()) {
            List<String> productValidationErrors = productEntity.setSimpleAttributesAndValidatePolicy(product);
            validationErrors.addAll(productValidationErrors);
        }
        if (!validationErrors.isEmpty()) {
            System.out.println("Validation errors: " + validationErrors);
            return new CreateProductsOutput_IP(
                    false,
                    "Product validation failed",
                    validationErrors);
        }
        return store.product.createProducts(input);
    } catch (Exception e) {
        System.out.println("Error: " + e);
        return new CreateProductsOutput_IP(
            false,
            "Error: " + e.getMessage(),
            new ArrayList<>());
    }
}
}
