package com.techfoot.stockspree.InboundAdaptors.REST.Product.CreateMultipleProducts;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Output_CreateMultipleProductsIA {
    private Boolean success;
    private String message;
    private Integer statusCode;
    private List<String> error;
    private Integer instanceId;

    public Output_CreateMultipleProductsIA(Boolean success, String message, Integer statusCode, List<String> error) {
        this.success = success;
        this.message = message;
        this.statusCode = statusCode;
        this.error = error;
    }
}
