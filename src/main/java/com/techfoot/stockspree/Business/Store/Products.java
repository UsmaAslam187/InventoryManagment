package com.techfoot.stockspree.Business.Store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techfoot.stockspree.InboundAdaptors.REST.C_CreateProduct.Input_CreateProductIA;
import com.techfoot.stockspree.InboundAdaptors.REST.C_CreateProduct.Output_CreateProductIA;
import com.techfoot.stockspree.OutboundPort.Persistent.ProductPorts.Port_CreateProductOP;

@Service
public class Products {
    @Autowired
    private Port_CreateProductOP port_CreateProductOP;

    public Output_CreateProductIA createProduct(Input_CreateProductIA product) {
        return port_CreateProductOP.createProduct(product);
    }
}
