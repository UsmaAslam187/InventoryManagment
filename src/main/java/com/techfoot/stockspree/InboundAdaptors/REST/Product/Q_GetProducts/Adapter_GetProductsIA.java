package com.techfoot.stockspree.InboundAdaptors.REST.Product.Q_GetProducts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techfoot.stockspree.InboundPort.Product.Q_GetProductHandler;

@RestController
@RequestMapping("/billspree/products/get-products")
public class Adapter_GetProductsIA {
    
    @Autowired
    private Q_GetProductHandler handler;

    @GetMapping
    public Output_GetProductsIA getProducts() {
        return new Output_GetProductsIA();
    }
}