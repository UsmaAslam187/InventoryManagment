package com.techfoot.stockspree.InboundPort.Product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.techfoot.stockspree.Business.DataContracts.GetAllProducts.GetAllProductsInput_IP;
import com.techfoot.stockspree.Business.DataContracts.GetAllProducts.GetAllProductsOutput_IP;

@Service
public class Q_GetAllProductsHandler {

    public GetAllProductsOutput_IP getAllProducts(GetAllProductsInput_IP input) {
        return handleGetAllProducts(input);
    }

    private GetAllProductsOutput_IP handleGetAllProducts(GetAllProductsInput_IP input) {
        // Placeholder implementation for getting all products
        List<GetAllProductsOutput_IP.Product> products = new ArrayList<>();

        // Mock products for demonstration
        GetAllProductsOutput_IP.Product product1 = new GetAllProductsOutput_IP.Product();
        product1.setCode("PROD001");
        product1.setName("Product 1");
        product1.setPrice(100);
        product1.setSalesAccount(1);
        product1.setPurchaseAccount(2);
        products.add(product1);

        return new GetAllProductsOutput_IP(true, "Products retrieved successfully",
                new ArrayList<>(), products);
    }
}
