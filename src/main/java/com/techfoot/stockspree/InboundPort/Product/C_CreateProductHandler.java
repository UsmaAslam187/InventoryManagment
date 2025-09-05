package com.techfoot.stockspree.InboundPort.Product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techfoot.stockspree.Business.DataContracts.CreateProduct.CreateProductInput_IP;
import com.techfoot.stockspree.Business.DataContracts.CreateProduct.CreateProductOutput_IP;
import com.techfoot.stockspree.Business.Domain.Product;
import com.techfoot.stockspree.Business.Store.Store;

@Service
public class C_CreateProductHandler {

    @Autowired
    private Store store;

    public CreateProductOutput_IP createProduct(CreateProductInput_IP input) {

        Product productEntity = new Product();
        List<String> validationErrors = productEntity.setSimpleAttributesAndValidatePolicy(input.getProducts().get(0));
        
        if (!validationErrors.isEmpty()) {
            System.out.println("Validation errors: " + validationErrors);
            return new CreateProductOutput_IP(
                    false,
                    "Product validation failed",
                    validationErrors);
        }
        return store.product.createProduct(input);
    }
}
