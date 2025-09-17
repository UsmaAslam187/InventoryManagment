package com.techfoot.stockspree.InboundPort.Product;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techfoot.stockspree.Business.DataContracts.GetAllProducts.GetAllProductsInput_IP;
import com.techfoot.stockspree.Business.DataContracts.GetAllProducts.GetAllProductsOutput_IP;
import com.techfoot.stockspree.Business.Store.Store;

@Service
public class Q_GetAllProductsHandler {

    @Autowired
    private Store store;

    public GetAllProductsOutput_IP getAllProducts(GetAllProductsInput_IP input) {
        return handleGetAllProducts(input);
    }

    private GetAllProductsOutput_IP handleGetAllProducts(GetAllProductsInput_IP input) {
        try {
            GetAllProductsOutput_IP products = store.product.getAllProducts(input);
            return new GetAllProductsOutput_IP(products.getSuccess(), products.getMessage(),
                    products.getErrors(), products.getProducts(), products.getPage());
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return new GetAllProductsOutput_IP(false, "Error: " + e.getMessage(), new ArrayList<>(), null, null);
        }
    }
}
