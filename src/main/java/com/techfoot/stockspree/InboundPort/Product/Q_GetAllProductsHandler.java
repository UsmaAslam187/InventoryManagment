package com.techfoot.stockspree.InboundPort.Product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.techfoot.stockspree.Business.DataContracts.GetAllProducts.Input_IP;
import com.techfoot.stockspree.Business.DataContracts.GetAllProducts.Output_IP;

@Service
public class Q_GetAllProductsHandler {
    
    public Output_IP getAllProducts(Input_IP input) {
        return handleGetAllProducts(input);
    }
    
    private Output_IP handleGetAllProducts(Input_IP input) {
        // Placeholder implementation for getting all products
        List<Output_IP.Product> products = new ArrayList<>();
        
        // Mock products for demonstration
        Output_IP.Product product1 = new Output_IP.Product();
        product1.setCode("PROD001");
        product1.setName("Product 1");
        product1.setPrice(100);
        product1.setSalesAccount(1);
        product1.setPurchaseAccount(2);
        products.add(product1);
     
        return new Output_IP(true, "Products retrieved successfully", 
            new ArrayList<>(), products);
    }
}
