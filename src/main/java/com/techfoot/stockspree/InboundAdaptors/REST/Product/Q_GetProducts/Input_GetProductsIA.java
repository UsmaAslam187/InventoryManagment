package com.techfoot.stockspree.InboundAdaptors.REST.Product.Q_GetProducts;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Input_GetProductsIA {
    @JsonProperty("method")
    private String method;


    @JsonProperty("id")
    private String id;

    @JsonProperty("jsonrpc")
    private String jsonrpc;


    // public List<String> validate() {
    //     List<String> errors = new ArrayList<>();
    //     if (method == null || method.isEmpty()) {
    //         errors.add("Method is required");
    //     }
    //     if (data == null || data.isEmpty()) {
    //         errors.add("Data is required");
    //     }
    //     return errors;
    // }
}
