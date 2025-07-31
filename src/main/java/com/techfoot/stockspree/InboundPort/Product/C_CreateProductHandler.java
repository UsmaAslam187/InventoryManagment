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

        System.out.println("Creating product: " + input);
        Product product = new Product();
        product.setName(input.getName());
        product.setCode(input.getCode());
        product.setPrice(input.getPrice());
        product.setTax(input.getTax());
        product.setType(input.getType());
        product.setSalesAccount(input.getSalesAccount());
        return store.product.createProduct(input);
    }
}
