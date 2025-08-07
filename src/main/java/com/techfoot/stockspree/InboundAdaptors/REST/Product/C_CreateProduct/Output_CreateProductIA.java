package com.techfoot.stockspree.InboundAdaptors.REST.Product.C_CreateProduct;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Output_CreateProductIA {
    private Boolean success;
    private String message;
    private List<String> errors;
}
