package com.techfoot.stockspree.InboundPort.Product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.techfoot.stockspree.Business.DataContracts.GetSingleProduct.GetSingleProductInput_IP;
import com.techfoot.stockspree.Business.DataContracts.GetSingleProduct.GetSingleProductOutput_IP;

@Service
public class Q_GetSingleProductHandler {
    
    public GetSingleProductOutput_IP getProduct(GetSingleProductInput_IP input) {
        return handleGetSingleProduct(input);
    }
    private GetSingleProductOutput_IP handleGetSingleProduct(GetSingleProductInput_IP input) {
        // Placeholder implementation for getting single product
        GetSingleProductOutput_IP.Product product = new GetSingleProductOutput_IP.Product();
        product.setCode(input.getCode());
        product.setName("Sample Product - " + input.getCode());
        product.setPrice(100);
        product.setSalesAccount(1);
        product.setPurchaseAccount(2);
        
        return new GetSingleProductOutput_IP(true, "Product retrieved successfully", 
            new ArrayList<>(), product, null);
    }
}
