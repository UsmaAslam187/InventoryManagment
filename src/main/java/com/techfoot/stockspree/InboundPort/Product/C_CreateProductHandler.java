package com.techfoot.stockspree.InboundPort.Product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techfoot.stockspree.Business.Domain.Product;
import com.techfoot.stockspree.Business.Store.Store;
import com.techfoot.stockspree.InboundAdaptors.REST.Product.C_CreateProduct.Input_CreateProductIA;
import com.techfoot.stockspree.InboundAdaptors.REST.Product.C_CreateProduct.Output_CreateProductIA;

@Service
public class C_CreateProductHandler {

    @Autowired
    private Store store;

    public Output_CreateProductIA createProduct(Input_CreateProductIA input) {

        System.out.println("Creating product: " + input);
        Product productEntity = new Product();
        List<String> validationErrors = productEntity.setSimpleAttributesAndValidatePolicy(input);
        
        if (!validationErrors.isEmpty()) {
            // Create a response with validation errors
            System.out.println("Validation errors: " + validationErrors);
            return new Output_CreateProductIA(
                false, 
                "Product validation failed", 
                validationErrors
            );
        }
        
        return store.product.createProduct(input);
    }
}
