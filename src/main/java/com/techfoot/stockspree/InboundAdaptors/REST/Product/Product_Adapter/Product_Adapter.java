package com.techfoot.stockspree.InboundAdaptors.REST.Product.Product_Adapter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techfoot.stockspree.InboundAdaptors.Configurations.RequestContext;
import com.techfoot.stockspree.InboundAdaptors.REST.Product.CreateMultipleProducts.Adapter_CreateMultipleProductsIA;
import com.techfoot.stockspree.InboundAdaptors.REST.Product.CreateSingleProduct.Adapter_CreateSingleProductIA;
import com.techfoot.stockspree.InboundAdaptors.REST.Product.GetAllProducts.GetAllProductsIA;
import com.techfoot.stockspree.InboundAdaptors.REST.Product.GetSingleProduct.GetSingleProductIA;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/stockspree/product")
public class Product_Adapter {

    @Autowired
    private Adapter_CreateSingleProductIA createSingleProductController;

    @Autowired
    private Adapter_CreateMultipleProductsIA createMultipleProductsController;

    @Autowired
    private GetSingleProductIA singleProductController;

    @Autowired
    private GetAllProductsIA allProductsController;

    @PostMapping
    public Object createProduct(HttpServletRequest request) throws IOException {

        String action = RequestContext.getCurrentAction();
        String method = RequestContext.getCurrentMethod();
        if (method == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "No method specified");
            errorResponse.put("error", List.of("Method field is required"));
            errorResponse.put("products", null);
            return errorResponse;
        }
        if (action == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "No action specified");
            errorResponse.put("error", List.of("Action field is required"));
            errorResponse.put("products", null);
            return errorResponse;
        }
        switch (method) {
            case "Create" -> {
                switch (action) {
                    case "Create Product" -> {
                        return createSingleProductController.createSingleProduct(request);
                    }
                    case "Create Bulk Products" -> {
                        return createMultipleProductsController.createMultipleProducts(request);
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
            case "Retrieve" -> {
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
            default -> {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Unknown method: " + method);
                errorResponse.put("error", List.of("Method not supported"));
                errorResponse.put("products", null);
                return errorResponse;
            }
        }
    }
}
