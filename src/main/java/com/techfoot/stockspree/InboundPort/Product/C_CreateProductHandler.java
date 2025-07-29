package com.techfoot.stockspree.InboundPort.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techfoot.stockspree.Business.Store.Store;
import com.techfoot.stockspree.InboundAdaptors.REST.C_CreateProduct.Input_CreateProductIA;
import com.techfoot.stockspree.InboundAdaptors.REST.C_CreateProduct.Output_CreateProductIA;

@Service
public class C_CreateProductHandler {

    @Autowired
    private Store store;

    public Output_CreateProductIA createProduct(Input_CreateProductIA input) {
        return store.product.createProduct(input);
    }
}
