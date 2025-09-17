package com.techfoot.stockspree.InboundPort.Product;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techfoot.stockspree.Business.DataContracts.GetSingleProduct.GetSingleProductInput_IP;
import com.techfoot.stockspree.Business.DataContracts.GetSingleProduct.GetSingleProductOutput_IP;
import com.techfoot.stockspree.Business.Store.Store;

@Service
public class Q_GetSingleProductHandler {

    @Autowired
    private Store store;

    public GetSingleProductOutput_IP getProduct(GetSingleProductInput_IP input) {
        return handleGetSingleProduct(input);
    }

    private GetSingleProductOutput_IP handleGetSingleProduct(GetSingleProductInput_IP input) {

        try {
            GetSingleProductOutput_IP products = store.product.getSingleProduct(input);
            GetSingleProductOutput_IP.Product product = products.getProduct();
            return new GetSingleProductOutput_IP(true, "Product retrieved successfully",
                    new ArrayList<>(), product);
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return new GetSingleProductOutput_IP(false, "Error: " + e.getMessage(), new ArrayList<>(), null);
        }
    }
}
