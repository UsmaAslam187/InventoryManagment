package com.techfoot.stockspree.InboundAdaptors.REST.C_CreateProduct;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techfoot.stockspree.InboundPort.C_CreateProductHandler;

@RestController
@RequestMapping("/api/products")
public class Adapter_CreateProductIA {
    private final C_CreateProductHandler handler = new C_CreateProductHandler();

    @PostMapping
    public Output_CreateProductIA createProduct(@RequestBody Input_CreateProductIA input) {
        return handler.createProduct(input);
    }
}
