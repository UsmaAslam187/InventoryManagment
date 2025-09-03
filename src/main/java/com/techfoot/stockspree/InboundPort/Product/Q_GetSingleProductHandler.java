package com.techfoot.stockspree.InboundPort.Product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.techfoot.stockspree.Business.DataContracts.GetSingleProduct.Input_IP;
import com.techfoot.stockspree.Business.DataContracts.GetSingleProduct.Output_IP;

@Service
public class Q_GetSingleProductHandler {
    
    public Output_IP getProduct(Input_IP input) {
        return handleGetSingleProduct(input);
    }
    private Output_IP handleGetSingleProduct(Input_IP input) {
        // Placeholder implementation for getting single product
        Output_IP.Product product = new Output_IP.Product();
        product.setCode(input.getCode());
        product.setName("Sample Product - " + input.getCode());
        product.setPrice(100);
        product.setSalesAccount(1);
        product.setPurchaseAccount(2);
        
        return new Output_IP(true, "Product retrieved successfully", 
            new ArrayList<>(), product, null);
    }
}
