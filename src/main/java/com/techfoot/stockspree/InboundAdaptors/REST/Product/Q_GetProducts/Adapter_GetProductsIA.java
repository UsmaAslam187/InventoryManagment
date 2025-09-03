package com.techfoot.stockspree.InboundAdaptors.REST.Product.Q_GetProducts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techfoot.stockspree.InboundAdaptors.Configurations.RequestContext;
import com.techfoot.stockspree.InboundAdaptors.REST.Product.GetAllProducts.GetAllProductsIA;
import com.techfoot.stockspree.InboundAdaptors.REST.Product.GetSingleProduct.GetSingleProductIA;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/billspree/products/get-products")
public class Adapter_GetProductsIA {
    
    @Autowired
    private GetSingleProductIA singleProductController;
    
    @Autowired
    private GetAllProductsIA allProductsController;

    @PostMapping("/")
    public Object getProducts(HttpServletRequest request) {
        // Get the action from the RequestContext set by the interceptor
        String action = RequestContext.getCurrentAction();
        
        if (action == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "No action specified");
            errorResponse.put("error", List.of("Action field is required"));
            errorResponse.put("products", null);
            return errorResponse;
        }
        
        switch (action) {
            case "Retrieve Product" -> {
                return singleProductController.getSingleProduct(request);
            }
            case "Retrieve Products" -> {
                return allProductsController.getAllProducts(request);
            }
            default -> {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Unknown action: " + action);
                errorResponse.put("error", List.of("Action not supported"));
                errorResponse.put("products", null);
                return errorResponse;
            }
        }
    }
}